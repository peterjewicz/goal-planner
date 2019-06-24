(ns goal-planner.core
    (:require [reagent.core :as reagent :refer [atom]]
              [goal-planner.components.home :as home]
              [goal-planner.state.state :refer [state handle-state-change]]
              [goal-planner.components.newgoal :as newgoal]
              [goal-planner.components.goalPage :as goalPage]
              [goal-planner.components.history :as history]
              [goal-planner.scripts.localforageApi :as api]))


(enable-console-print!)


(api/get-initial-data) ; grabs the initial data from storage

;; define your app data so that it doesn't get over-written on reload
; TODO we need to do a check to figure out if we are in scroll mode herer
(defn Main []
  [:div.Main
   [home/render state]
   [newgoal/render state]
   [goalPage/render state]
   [history/render state]])


(reagent/render-component [Main]
                          (. js/document (getElementById "app")))

(defn on-js-reload [])
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)

