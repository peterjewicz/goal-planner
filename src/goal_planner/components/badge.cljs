(ns goal-planner.components.badge
  (:require [goal-planner.scripts.progress :refer [get-total-progress calculate-completion calculate-overdue]]))

(defn render [goal]
  "renders a badge for active - overdue - complete"
  (if (:end goal)
    [:div.Badge
      (if (calculate-completion goal)
        [:p.success "Complete"]
        (if (calculate-overdue (:end goal))
          [:p.success "In Progress"]
          [:p.failure "Overdue"]))]))