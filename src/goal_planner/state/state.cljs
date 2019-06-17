(ns goal-planner.state.state
  (:require [reagent.core :as reagent :refer [atom]]))


; Holds a reference to all the current Items in the Database
; :activeView also contains things like view-all-cities & view-individual-city
(defonce state (atom {:activeView {  ; This gets erased as state changes but I left it here as a reminder for expected values
                                   :home "visible"
                                   :newgoal false
                                   :goalpage false
                                   :history false}
                       :goals []
                       :milestones []
                       :progress []
                       :activeGoal {}}))


(defn update-current-view [payload]
  "Handles changing the view to the next selected page"
  (swap! state conj {:activeView {(keyword payload) "visible"}}))

(defn update-goals [goals]
  (swap! state conj {:goals goals}))

(defn update-active-goal [goal]
  (swap! state conj {:activeGoal goal}))


; (defn update-state-item [payload]
;   "Updates a specific key in the store with a val - we pass in the whole state instance each time"
;   (swap! state conj {(keyword (:key payload)) (:val payload)}))

(defn handle-state-change [action payload]
  "Accept an action function to dispatch and passes it the current payload"
  (let [fn-var ((ns-publics 'goal-planner.state.state) (symbol action))]
       (fn-var payload)))
