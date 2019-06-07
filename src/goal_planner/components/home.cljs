(ns goal-planner.components.home
  (:require [reagent.core :as reagent :refer [atom]]
            [goal-planner.state.state :refer [handle-state-change]]
            [goal-planner.components.goal :as goal]))

(defn open-goal-page [goal]
  "Opens teh goal display page"
  (handle-state-change "update-active-goal" goal)
  (handle-state-change "update-current-view" "goalpage"))

(defn render [state]
  [:div.Home.View
    [:div.Home.header
      [:p "Your Goals"]]
    (doall (for [goal (:goals @state)]
      [:div {:key (:title goal) :on-click #(open-goal-page goal)}
        [goal/render (:title goal) {:current 0.5 :criteria (:criteria goal)}] ; TODO we need a way to track progress!
    ]))
    [:p.addGoalButton {:on-click  #(handle-state-change "update-current-view" "newgoal")} "+ Add Goal"]])

