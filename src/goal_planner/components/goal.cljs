(ns goal-planner.components.goal
  (:require [reagent.core :as reagent :refer [atom]]))

(defn generate-percent [current total]
  (int (* 100 (/ current total))))

(defn render [title goal]
  [:div.Goal
    [:div.Goal.left
      [:h3 title]]
    [:div.Goal.right
      [:div.Goal.bar
        [:div.Goal.bar.filled {:style {:width (str (generate-percent (:current goal) (:criteria goal)) "%")}}]]
      [:p (str (generate-percent (:current goal) (:criteria goal)) "%")]]])