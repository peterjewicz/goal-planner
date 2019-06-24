(ns goal-planner.components.home
  (:require [reagent.core :as reagent :refer [atom]]
            [goal-planner.state.state :refer [handle-state-change]]
            [goal-planner.components.goal :as goal]
            [goal-planner.scripts.progress :as progressScripts]))

(defn open-goal-page [goal]
  "Opens teh goal display page"
  (handle-state-change "update-active-goal" goal)
  (handle-state-change "update-current-view" "goalpage"))

(defn render [state]
  [:div.Home.View
    [:div.Home.header
      [:div.header__imageWrapper {:on-click #(handle-state-change "update-current-view" "history")}
        [:img.backButton {:src "stats.png" :style {:width "25px"}}]]
      [:p "Your Goals"]]
    (if (= (count (:goals @state)) 0)
      [:h3 {:style {:text-align "center" :font-weight 600}} "No Goals Added"])
    (doall (for [goal (:goals @state)]
      [:div {:key (:title goal) :on-click #(open-goal-page goal)}
        [goal/render (:title goal) {:current (progressScripts/get-progress (:progress goal)) :criteria (:criteria goal)}] ; TODO we need a way to track progress!
    ]))
    [:p.addGoalButton {:on-click  #(handle-state-change "update-current-view" "newgoal")} "+ Add Goal"]
    [:div.adSpacer]])

