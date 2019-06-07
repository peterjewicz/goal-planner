(ns goal-planner.components.goalPage
  (:require [reagent.core :as reagent :refer [atom]]
            [goal-planner.state.state :refer [handle-state-change]]))




(defn render [state]
  (fn []
    (let [goal (:activeGoal @state)]
      [:div.GoalPage.ViewPage.View {:class (:goalpage (:activeView @state))}
        [:div.GoalPage.header
          [:p {:on-click #(handle-state-change "update-current-view" "home")} "<- Back"]
          [:p (:title goal)]]
        [:h1 (:title goal)]
    ])))