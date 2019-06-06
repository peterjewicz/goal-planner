(ns goal-planner.scripts.localforageApi
    (:require [goal-planner.state.state :refer [handle-state-change]]))

(defn add-metadata [details]
  "Adds the extra metadata we want to track on each item"
  (conj details {:used false :created (js/Date.)}))

; (defn get-initial-data-by-type [type]
;   "Quries an entity from out localstorage and adds it to local store"
;   (.then (.getItem (.-localforage js/window) type) (fn [value]
;                                                     (handle-state-change "update-entity" {:type type :value value}))))
;
; (defn pull-initial-data []
;   "this function gets all the current saved data and pushes it to the store"
;   (go (get-initial-data-by-type "cities"))
;   (go (get-initial-data-by-type "npcs"))
;   (go (get-initial-data-by-type "locations"))
;   (go (get-initial-data-by-type "items"))
;   (go (get-initial-data-by-type "hooks"))
;   (go (get-initial-data-by-type "lists")))

(defn add-goal [goal]
  "add an item to localstorage by pulling the [], adding to it, and overwriting"
  ; TODO logical checks wither here or in the actual component
    (.then (.getItem (.-localforage js/window) "goals") (fn [value]
      (let [currentStorage (js->clj value :keywordize-keys true)]
        (print (conj currentStorage goal))
        (.then (.setItem (.-localforage js/window) "goals" (clj->js (conj currentStorage goal)) (fn []
          (js/alert "Goal Added"))))))))

