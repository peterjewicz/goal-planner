(ns goal-planner.components.home
  (:require [reagent.core :as reagent :refer [atom]]
            [goal-planner.state.state :refer [handle-state-change]]))


(defn render []
  [:div.Home.View
   [:h3 "Your Goals"]
   [:p "Goal list here..."]
   [:p.addGoalButton {:on-click  #(handle-state-change "update-current-view" "newgoal")} "+ Add Goal"]])

