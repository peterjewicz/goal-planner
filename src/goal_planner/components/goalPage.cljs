(ns goal-planner.components.goalPage
  (:require [reagent.core :as reagent :refer [atom]]
            [goal-planner.state.state :refer [handle-state-change]]
            [goal-planner.scripts.localforageApi :as api]
            [goal-planner.components.badge :as badge]
            [goal-planner.scripts.progress :as progress]
            [goog.string :as gstring]
            [cljs-time.core :refer [now date-time]]
            [cljs-time.format :refer [formatter parse unparse]]))

(defn check-if-completed [goal progress]
  "Checks if the current progress would be added makes the goal complete and passes back either false or today's date"
  (if ( >= (+ (int (:value @progress)) (int (progress/get-total-progress goal))) (:criteria goal))
    (unparse (formatter "MM/dd/yyyy") (now))
    false))

(defn add-progress [goal progress]
  (api/update-goal
  (conj ; adds our completed on flag
    (update-in goal [:progress] conj {:value (:value @progress) :date (unparse (formatter "MM/dd/yyyy") (now)) :note (:note @progress)})
    {:completedOn (check-if-completed goal progress)}))
  (reset! progress {:value 0 :note ""})) ;TODO update input too and cleanup

(defn delete-goal [goal]
  "calls the API to delete a goal"
  (api/delete-goal goal))

(defn archive-goal [goal]
  "archie goal hides the goal from view but keeps its stats"
  (api/archive-goal goal))

(defn display-note-text [note]
  "conditionally shows the '-' or not if no note"
  (if (> (count note) 0)
    (str " - " note)
    ""))


(defn render [state]
  (let [
        progress (atom {:value 0 :note ""})]
    (fn []
      (let [goal (:activeGoal @state)]
        [:div.GoalPage.ViewPage.View {:class (:goalpage (:activeView @state))}
          [:div.GoalPage.header
            [:div.header__imageWrapper {:on-click #(handle-state-change "update-current-view" "home")}
              [:img.backButton {:src "back.png"}]]
            [:p (:title goal)]]
          [:div.GoalPage.content
            [:div.GoalPage.titleWrapper
              [:h2.GoalPage.title (:title goal)]
              [badge/render goal]]
            [:p.GoalPage.DueDate (str "Complete By: " (:month (:end goal)) " " (:day (:end goal)) ", " (:year (:end goal)))]
            [:p.GoalPage.total (str "Goal: " (:criteria goal))]
            [:div.GoalPage.progress.currentProgess
              [:h3.borderText "Current Progess"]
              (doall (for [progress (:progress goal)]
                [:div.GoalPage.progress.progressItem {:key (str (:value progress) "-" (rand-int 10000) )}
                  [:p (str "Amount: "(:value progress)
                      (gstring/unescapeEntities "&nbsp;")(gstring/unescapeEntities "&nbsp;")
                      (gstring/unescapeEntities "&nbsp;")(gstring/unescapeEntities "&nbsp;")
                      (gstring/unescapeEntities "&nbsp;") "On: "  (:date progress)
                      (display-note-text (:note progress)))]]))]
            [:div.GoalPage.progress
              [:div.GoalPage.progress.addProgress
                [:h3.borderText "Add Progress"]
                [:input {:type "text" :placeholder "Progress" :value (:value @progress) :on-change #(swap! progress conj {:value (-> % .-target .-value)})}]
                [:input {:type "text" :placeholder "Note" :value (:note @progress) :on-change #(swap! progress conj {:note (-> % .-target .-value)})}]
                [:button.primary {:on-click #(add-progress goal progress) :style {:display "block"}} "Save Progres"]]]
            [:div.GoalPage.deleteWrapper
              [:div.Goalpage.deleteWrapper.buttonWrapper
                [:button.danger {:on-click #(delete-goal goal)} "Delete"]]
              [:p.delete "Completely remove a goal and all its stats. This action is permanent!"]]
            [:div.GoalPage.archiveWrapper
              [:div.Goalpage.archiveWrapper.buttonWrapper
                [:button.secondary {:on-click #(archive-goal goal)} "Archive"]]
              [:p "Archived goals will be hidden from your view but still count on the stats page. Use this to cleanup completed or failed goals."]]
            [:div.adSpacer]]]))))