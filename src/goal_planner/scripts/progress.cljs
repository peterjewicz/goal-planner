(ns goal-planner.scripts.progress
    (:require ["moment" :as moment]))

; TODO need to cleanup both of these reduces
(defn get-progress [progress]
  (reduce-kv
    (fn [result index progress]
      (+ (int result) (int (:value progress)))
    )[] progress))

(defn get-total-progress [goal]
  "Adds progress enteries together to get total"
  (reduce (fn [total progress]
            (+ total (int (:value progress)))) 0 (:progress goal)))

(defn calculate-completion [goal]
  "checks whether progress surpasses goal"
  (if (> (get-total-progress goal) (:criteria goal))
    true
    false))

(defn calculate-overdue [endDateMap]
  "Checks whether a given date has past"
  (let [momentComplete (moment (str (.format (.month (moment) (:month endDateMap)) "M") "/" (:day endDateMap) "/" (:year endDateMap)))]
    (.isAfter momentComplete (moment))))