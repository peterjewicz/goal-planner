(ns goal-planner.components.home
  (:require [reagent.core :as reagent :refer [atom]]
            [goal-planner.state.state :refer [handle-state-change]]
            [goal-planner.components.goal :as goal]))


(defn render []
  [:div.Home.View
    [:div.Home.header
      [:p "Your Goals"]
    ]
    [goal/render "Title" {:current 30 :criteria 90}]
    [goal/render "Title" {:current 20 :criteria 90}]
    [goal/render "Title" {:current 60 :criteria 90}]
    [goal/render "Title" {:current 10 :criteria 90}]
    [:p.addGoalButton {:on-click  #(handle-state-change "update-current-view" "newgoal")} "+ Add Goal"]])

