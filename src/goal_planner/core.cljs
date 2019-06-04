(ns goal-planner.core
    (:require [reagent.core :as reagent :refer [atom]]
              [goal-planner.components.home :as home]
              [goal-planner.state.state :refer [state handle-state-change]]
              [goal-planner.components.newgoal :as newgoal]))


(enable-console-print!)

(println "This text is printed from src/goal-planner/core.cljs. Go ahead and edit it and see reloading in action.")

;; define your app data so that it doesn't get over-written on reload

(defonce app-state (atom {:text "Hello world!"}))


(defn Main []
  [:div.Main
   [home/render]
   [newgoal/render state]])


(reagent/render-component [Main]
                          (. js/document (getElementById "app")))

(defn on-js-reload [])
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)

