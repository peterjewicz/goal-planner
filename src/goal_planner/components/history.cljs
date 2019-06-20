(ns goal-planner.components.history
  (:require [reagent.core :as reagent :refer [atom]]
            [goal-planner.state.state :refer [handle-state-change]]
            [goal-planner.scripts.progress :as progress]
            [goog.string :as gstring]
            [goog.string.format]))

(defn format-number [number]
  "To 2 decimal places"
  (gstring/format "%.2f" number))

(defn render [state]
  [:div.History.ViewPage.View {:class (:history (:activeView @state))}
    [:div.History.header
      [:div.header__imageWrapper {:on-click #(handle-state-change "update-current-view" "home")}
        [:img.backButton {:src "back.png"}]]
      [:p "History"]]
    [:div.History.body
      (if (> (count (:goals @state)) 0) ; if not goals display a fun message
        [:div
          [:h4 (str "Goal Total: " (count (:goals @state)))]
          [:h4 (str "Completed: " (format-number (* 100 (/ (progress/get-total-completed (:goals @state)) (count (:goals @state))))) "%")]
          [:h4 (str "Overdue: " (format-number (* 100 (/ (progress/get-total-overdue (:goals @state)) (count (:goals @state))))) "%")]
          [:h4 (str "Completed On Time: " (format-number (* 100 (/ (progress/get-total-completed-on-time (:goals @state)) (count (:goals @state))))) "%")]]
        [:div [:h3 {:style {:text-align "center" :font-weight 600}} "No Goals Added"]])]]) ;else


