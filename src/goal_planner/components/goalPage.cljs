(ns goal-planner.components.goalPage
  (:require [reagent.core :as reagent :refer [atom]]
            [goal-planner.state.state :refer [handle-state-change]]
            [goal-planner.scripts.localforageApi :as api]))

(defn add-progress [goal progress]
  (api/update-goal (update-in goal [:progress] conj {:value @progress :date "now"}))
  (reset! progress {:value 0})) ;TODO update input too and cleanup

(defn render [state]
  (fn []
    (let [goal (:activeGoal @state)
          progress (atom 0)]
      [:div.GoalPage.ViewPage.View {:class (:goalpage (:activeView @state))}
        [:div.GoalPage.header
          [:p {:on-click #(handle-state-change "update-current-view" "home")} "<- Back"]
          [:p (:title goal)]]
        [:h1 (:title goal)]
        [:div.GoalPage.progress
          [:h3.borderText "Add Progress"]
          [:input {:type "text" :placeholder "Progress" :on-change #(reset! progress (-> % .-target .-value))}]
          [:button.primary {:on-click #(add-progress goal progress)} "Save Progres"]]])))