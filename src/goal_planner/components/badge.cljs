(ns goal-planner.components.badge
  (:require ["moment" :as moment]))

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

(defn render [goal]
  "renders a badge for active - overdue - complete"
  (if (:end goal)
    [:div.Badge
      (if (calculate-completion goal)
        [:p.success "Complete"]
        (if (calculate-overdue (:end goal))
          [:p.success "In Progress"]
          [:p.failure "Overdue"])
      )]))