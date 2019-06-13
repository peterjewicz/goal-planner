(ns goal-planner.components.goalPage
  (:require [reagent.core :as reagent :refer [atom]]
            [goal-planner.state.state :refer [handle-state-change]]
            [goal-planner.scripts.localforageApi :as api]
            [goal-planner.components.badge :as badge]
            [cljs-time.core :refer [now date-time]]
            [cljs-time.format :refer [formatter parse unparse]]))

(defn add-progress [goal progress]
  (api/update-goal (update-in goal [:progress] conj {:value (:value @progress) :date (unparse (formatter "MM/dd/yyyy") (now)) :note (:note @progress)}))
  (reset! progress {:value 0})) ;TODO update input too and cleanup

(defn calculate-completion [goal]
  "Adds up the progress enteries to determine whether goal is complete or not"
  (reduce (fn [total progress]
            (+ total (int (:value progress)))) 0 (:progress goal)))


(defn render [state]
  (fn []
    (let [goal (:activeGoal @state)
          progress (atom {:value 0 :note ""})]
      (calculate-completion goal)
      [:div.GoalPage.ViewPage.View {:class (:goalpage (:activeView @state))}
        [:div.GoalPage.header
          [:p {:on-click #(handle-state-change "update-current-view" "home")} "<- Back"]
          [:p (:title goal)]]
        [:div.GoalPage.titleWrapper
          [:h2.GoalPage.title (:title goal)]
          [badge/render (:end goal)]]
        [:p.GoalPage.DueDate (str "Complete By: " (:month (:end goal)) " " (:day (:end goal)) ", " (:year (:end goal)))]
        [:div.GoalPage.progress.currentProgess
          [:h3.borderText "Current Progess"]
          (doall (for [progress (:progress goal)]
            [:div.GoalPage.progress.progressItem {:key (str (:value progress) "-" (:date progress) )}
              [:p (str (:value progress) " - "  (:date progress) " - " (:note progress))]
            ]))]
        [:div.GoalPage.progress
          [:div.GoalPage.progress.addProgress
            [:h3.borderText "Add Progress"]
            [:input {:type "text" :placeholder "Progress" :on-change #(swap! progress conj {:value (-> % .-target .-value)})}]
            [:input {:type "text" :placeholder "Note" :on-change #(swap! progress conj {:note (-> % .-target .-value)})}]
            [:button.primary {:on-click #(add-progress goal progress)} "Save Progres"]]]])))