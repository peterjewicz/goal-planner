(ns goal-planner.scripts.localforageApi
    (:require [goal-planner.state.state :refer [handle-state-change]]
              [fancy_alert.core :as fancy-alert]))

(defn add-metadata [details]
  "Adds the extra metadata we want to track on each item"
  (conj details {:used false :created (js/Date.)}))

(defn get-initial-data []
  "Queries our initial data - projects - also called whenever a new item is added"
  (.then (.getItem (.-localforage js/window) "goals") (fn [value]
                                                    (handle-state-change "update-goals" (js->clj value :keywordize-keys true)))))

(defn add-goal [goal]
  "add an item to localstorage by pulling the [], adding to it, and overwriting"
  ; TODO logical checks wither here or in the actual component
    (.then (.getItem (.-localforage js/window) "goals") (fn [value]
      (let [currentStorage (js->clj value :keywordize-keys true)]
        (loop [i 0] ; Little cleaner than doall/for so we only iterate as needed this probably won't get too big anyways
          ; firt we check if I is at the current length or we can get an out of bounds errosr
          (if (= (count currentStorage) i)
            (.then (.setItem (.-localforage js/window) "goals" (clj->js (conj currentStorage goal)) (fn []
              (get-initial-data)
              (fancy-alert/fancy-alert
                {:text "Goal Added" :hideAfterN false
                 :styles {:background "white;" :border "2px solid #2f8ffb;" :width "300px;" :margin-left "-150px;" :z-index "999;" :color "black;"}
                 :buttonProperties {:buttonText "Okay"}})
              true)))
              (if (= (:title (nth currentStorage i)) (:title goal))
                false
                (recur (inc i)))))))))

(defn update-goal [goal]
  "updates a goal by finding it then overriding it"
  (.then (.getItem (.-localforage js/window) "goals") (fn [value]
    (let [currentStorage (js->clj value :keywordize-keys true)]
      (loop [i 0] ; Little cleaner than doall/for so we only iterate as needed this probably won't get too big anyways
        (if (= (:title (nth currentStorage i)) (:title goal))
          (.then (.setItem (.-localforage js/window) "goals" (clj->js (conj (assoc currentStorage i goal))) (fn []
            (get-initial-data)
            (handle-state-change "update-active-goal" goal)
            (fancy-alert/fancy-alert
              {:text "Progress Updated" :hideAfterN false
               :styles {:background "white;" :border "2px solid #2f8ffb;" :width "300px;" :margin-left "-150px;" :z-index "999;" :color "black;"}
               :buttonProperties {:buttonText "Okay"}}))))
          (recur (inc i))))))))


; NEXT TWO FUNCTIONS HANDLE DELETIONS
(defn filter-goals-by-title [title currentStorage]
  "filters out goal that matches the given tile"
  (filter (fn [goal]
            (if (= (:title goal) title)
              false
              true)) currentStorage))

(defn delete-goal [goal]
  (.then (.getItem (.-localforage js/window) "goals") (fn [value]
    (let [currentStorage (js->clj value :keywordize-keys true)]
      (.then (.setItem (.-localforage js/window) "goals" (clj->js (filter-goals-by-title (:title goal) currentStorage)) (fn []
        (get-initial-data)
        (handle-state-change "update-active-goal" {})
        (handle-state-change "update-current-view" "home")
        (fancy-alert/fancy-alert
          {:text "Goal Deleted" :hideAfterN false
           :styles {:background "white;" :border "2px solid #2f8ffb;" :width "300px;" :margin-left "-150px;" :z-index "999;" :color "black;"}
           :buttonProperties {:buttonText "Okay"}}))))))))

(defn archive-goal [goal]
  (.then (.getItem (.-localforage js/window) "goals") (fn [value]
    (let [currentStorage (js->clj value :keywordize-keys true)
          archivedGoal (conj goal {:archived true})]
      (loop [i 0] ; Little cleaner than doall/for so we only iterate as needed this probably won't get too big anyways
        (if (= (:title (nth currentStorage i)) (:title goal))
          (.then (.setItem (.-localforage js/window) "goals" (clj->js (conj (assoc currentStorage i archivedGoal))) (fn []
            (get-initial-data)
            (handle-state-change "update-active-goal" {})
            (handle-state-change "update-current-view" "home")
            (fancy-alert/fancy-alert
              {:text "Goal Archived" :hideAfterN false
               :styles {:background "white;" :border "2px solid #2f8ffb;" :width "300px;" :margin-left "-150px;" :z-index "999;" :color "black;"}
               :buttonProperties {:buttonText "Okay"}}))))
          (recur (inc i))))))))
