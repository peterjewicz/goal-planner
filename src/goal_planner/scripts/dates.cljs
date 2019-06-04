(ns goal-planner.scripts.dates
  (:require ["moment" :as moment]))


(defn get-date-by-days [days]
  "returns the date x days in the future"
  (.format (.add (moment) 5 "days") "MM-DD-YYYY")
)
; moment(startdate, "DD-MM-YYYY").add(5, 'days');
; TODO leap years
(defn get-days-from-goal-obj [goal]
  "takes our 'new goal' object and gets days from timeline properties"
  (+ (:days goal) (* 7 (:weeks goal)) (* 365 (:years goal))))