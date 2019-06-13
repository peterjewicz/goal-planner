(ns goal-planner.components.badge
  (:require ["moment" :as moment]))

(defn calculate-overdue [endDateMap]
  "Checks whether a given date has past"
  (let [momentComplete (moment (str (.format (.month (moment) (:month endDateMap)) "M") "/" (:day endDateMap) "/" (:year endDateMap)))]
    (.isAfter momentComplete (moment))))

(defn render [endDateMap]
  "renders a badge for active - overdue - complete"
  (if endDateMap
    [:div.Badge
      (if (calculate-overdue endDateMap)
        [:p.success "In Progress"]
        [:p.failure "Overdue"])]))