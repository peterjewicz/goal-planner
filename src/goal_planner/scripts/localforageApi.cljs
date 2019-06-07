(ns goal-planner.scripts.localforageApi
    (:require [goal-planner.state.state :refer [handle-state-change]]))

(defn add-metadata [details]
  "Adds the extra metadata we want to track on each item"
  (conj details {:used false :created (js/Date.)}))

(defn get-initial-data []
  "Queries our initial data - projects"
  (.then (.getItem (.-localforage js/window) "goals") (fn [value]
                                                    (handle-state-change "update-goals" (js->clj value :keywordize-keys true)))))

(defn add-goal [goal]
  "add an item to localstorage by pulling the [], adding to it, and overwriting"
  ; TODO logical checks wither here or in the actual component
    (.then (.getItem (.-localforage js/window) "goals") (fn [value]
      (let [currentStorage (js->clj value :keywordize-keys true)]
        (print (conj currentStorage goal))
        (.then (.setItem (.-localforage js/window) "goals" (clj->js (conj currentStorage goal)) (fn []
          (js/alert "Goal Added"))))))))

