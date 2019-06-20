(ns goal-planner.scripts.progress
    (:require ["moment" :as moment]))

; TODO need to cleanup both of these reduces probably don't need them both
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
  (if (>= (get-total-progress goal) (:criteria goal))
    true
    false))

; TODO we can combine the next two into a generic date checker
(defn calculate-overdue [endDateMap]
  "Checks whether a given date has past will return true if given date is later"
  (let [momentComplete (moment (str (.format (.month (moment) (:month endDateMap)) "M") "/" (:day endDateMap) "/" (:year endDateMap)))]
    (.isAfter momentComplete (moment))))

(defn check-if-date-is-later [first second]
  (.isAfter first second))

(defn get-total-completed [goals]
  "returns how many of goals are compelted"
  (reduce (fn [total goal]
    (if (:completedOn goal)
      (inc total)
      total)) 0  goals))

(defn get-total-overdue [goals]
  "returns the total amount of goals past their due date"
  (reduce (fn [total goal]
    (if (and (not (calculate-overdue (:end goal))) (not (:completedOn goal)))
      (inc total)
      total)) 0  goals))

(defn get-total-completed-on-time [goals]
  "Checks completed goals vs their due date and determins if they were completed on time or not"
  (reduce (fn [total goal]
    (if (and (:completedOn goal) (check-if-date-is-later
                                   (moment (str (.format (.month (moment) (:month (:end goal))) "M") "/" (:day (:end goal)) "/" (:year (:end goal))))
                                   (moment (:completedOn goal))))
      (inc total)
      total)) 0  goals))
