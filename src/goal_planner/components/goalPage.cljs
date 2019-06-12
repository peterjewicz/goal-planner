(ns goal-planner.components.goalPage
  (:require [reagent.core :as reagent :refer [atom]]
            [goal-planner.state.state :refer [handle-state-change]]
            [goal-planner.scripts.localforageApi :as api]
            [cljs-time.core :refer [now date-time]]
            [cljs-time.format :refer [formatter parse unparse]]))

(defn add-progress [goal progress]
  (api/update-goal (update-in goal [:progress] conj {:value (:value @progress) :date (unparse (formatter "MM/dd/yyyy") (now)) :note (:note @progress)}))
  (reset! progress {:value 0})) ;TODO update input too and cleanup


(defn render [state]
  (fn []
    (let [goal (:activeGoal @state)
          progress (atom {:value 0 :note ""})]
      [:div.GoalPage.ViewPage.View {:class (:goalpage (:activeView @state))}
        [:div.GoalPage.header
          [:p {:on-click #(handle-state-change "update-current-view" "home")} "<- Back"]
          [:p (:title goal)]]
        [:h2.GoalPage.title (:title goal)]
        [:div.GoalPage.progress.currentProgess
          [:h3.borderText "Current Progess"]
          (doall (for [progress (:progress goal)]
            [:div.GoalPage.progress.progressItem
              [:p {:key (str (:value progress) "-" (:date progress) )} (str (:value progress) " - "  (:date progress) " - " (:note progress))]
            ]))]
        [:div.GoalPage.progress
          [:div.GoalPage.progress.addProgress
            [:h3.borderText "Add Progress"]
            [:input {:type "text" :placeholder "Progress" :on-change #(swap! progress conj {:value (-> % .-target .-value)})}]
            [:input {:type "text" :placeholder "Note" :on-change #(swap! progress conj {:note (-> % .-target .-value)})}]
            [:button.primary {:on-click #(add-progress goal progress)} "Save Progres"]]



]])))