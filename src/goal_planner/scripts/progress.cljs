(ns goal-planner.scripts.progress)


(defn get-progress [progress]
  (reduce-kv
    (fn [result index progress]
      (+ (int result) (int (:value progress)))
    )[] progress))