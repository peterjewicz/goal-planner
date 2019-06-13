(ns goal-planner.scripts.localforageApi
    (:require [goal-planner.state.state :refer [handle-state-change]]))

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
        (.then (.setItem (.-localforage js/window) "goals" (clj->js (conj currentStorage goal)) (fn []
          (get-initial-data)
          (js/alert "Goal Added"))))))))

(defn update-goal [goal]
  "updates a goal by finding it then overriding it"
  (.then (.getItem (.-localforage js/window) "goals") (fn [value]
    (let [currentStorage (js->clj value :keywordize-keys true)]
      (loop [i 0] ; Little cleaner than doall/for so we only iterate as needed this probably won't get too big anyways
        (if (= (:title (nth currentStorage i)) (:title goal))
          (.then (.setItem (.-localforage js/window) "goals" (clj->js (conj (assoc currentStorage i goal))) (fn []
            (get-initial-data)
            (handle-state-change "update-active-goal" goal)
            (js/alert "Progress Updated!"))))
          (recur (inc i))))))))

