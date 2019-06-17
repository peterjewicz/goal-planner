(ns goal-planner.components.history
  (:require [reagent.core :as reagent :refer [atom]]
            [goal-planner.state.state :refer [handle-state-change]]
            [goal-planner.scripts.progress :as progress]))

(defn render [state]
  [:div.History.ViewPage.View {:class (:history (:activeView @state))}
    [:div.History.header
      [:div.header__imageWrapper {:on-click #(handle-state-change "update-current-view" "home")}
        [:img.backButton {:src "back.png"}]]
      [:p "History"]]
    [:div.History.body
      [:h4 (str "Goal Total: " (count (:goals @state)))]
      [:h4 (str "% Completed: " (* 100 (/ (progress/get-total-completed (:goals @state)) (count (:goals @state))))) "%"]
      [:h4 (str "% Overdue: " (* 100 (/ (progress/get-total-overdue (:goals @state)) (count (:goals @state))))) "%"]
      [:h4 (str "How Often Do You Complete On Time? " (* 100 (/ (progress/get-total-completed-on-time (:goals @state)) (count (:goals @state)))) "%")]
    ]
  ])


