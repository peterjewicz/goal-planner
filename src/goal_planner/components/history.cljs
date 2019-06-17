(ns goal-planner.components.history
  (:require [reagent.core :as reagent :refer [atom]]
            [goal-planner.state.state :refer [handle-state-change]]))

(defn render [state]
  [:div.History.ViewPage.View {:class (:history (:activeView @state))}
    [:div.History.header
      [:div.header__imageWrapper {:on-click #(handle-state-change "update-current-view" "home")}
        [:img.backButton {:src "back.png"}]]
      [:p "History"]]

  ; what we need to show
  ; Get Total of goals

  ;Show % completed - Total
  ;Show % overdue - total
  ;Ontime completed - Completed on date needed show % of completed that were on time
  ])


