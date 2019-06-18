(ns goal-planner.scripts.datepicker
  (:require [reagent.core :as reagent :refer [atom]]
            ["moment" :as moment]))

(defonce months ["January" "February" "March" "April" "May"
                 "June" "July" "August" "September" "October"
                 "November" "December"])

(defonce defaultStyles {
  :position "fixed"
  :width "100%"
  :height "100%"
  :text-align "center"
  :background-color "rgba(255,255,255,.95)"
  :display "flex"
  :flex-direction "column"
  :justify-content "center"
  :top 0
  :left 0
  :z-index -999})

(defn update-passed-store [type val store]
  "Updates the values of the passed in atom expected {:day :month :year} map"
  (swap! store assoc-in [:end (keyword type)] val))

(defn generate-css [styles]
  "simple helper to add the stles to the domElement"
  (str "style=\""(clojure.string/join " " (map (fn [[key val]] (str (name key) ": " val)) styles))"\""))

(defn generate-months []
  (map (fn [month]
           [:option {:value month :key month} month]) months))

(defn generate-years []
  (map (fn [year]
           [:option {:value year :key year} year]) (range 2019 2050)))

(defn generate-days [date]
  (map (fn [day]
           [:option {:value day :key day} day]) (range 1
                                                  (+ 1(.daysInMonth (moment (str (:year @date) "-" (:month @date)) "YYYY-MMMM"))))))

(defn update-month [val store date]
  (update-passed-store "month" val store)
  (swap! date conj {:month val}))

(defn update-year [val store date]
  (update-passed-store "year" val store)
  (swap! date conj {:year val}))


(defn open-datepicker []
  (let [bodyElem (.getElementById js/document "datepicker")]
    (aset  (.-style bodyElem) "z-index" "900")))

(defn close-datepicker []
  (let [bodyElem (.getElementById js/document "datepicker")]
    (aset  (.-style bodyElem) "z-index" "-999")))

(defn generate-html [store date]
  [:div#datepicker {:style defaultStyles}
    [:div#datepicker.datepickerinnner
      [:select {:default-value (:end (:month @store)) :on-change #(update-month (-> % .-target .-value) store date)
                :style {:webkit-appearance "none" :padding "8px" :text-align "center" :margin "0 8px"}}
        (generate-months)]
      [:select {:on-change #(update-passed-store "day" (-> % .-target .-value) store)
               :style {:webkit-appearance "none" :padding "8px" :text-align "center" :margin "0 8px"}}
        (generate-days date)]
      [:select {:on-change #(update-year (-> % .-target .-value) store date)
                :style {:webkit-appearance "none" :padding "8px" :text-align "center" :margin "0 8px"}}
        (generate-years)]
      [:div.buttonWrapper {:style {:width "100%" :margin-top "32px"}}
        [:button.primary  {:on-click #(close-datepicker)} "Update"]]]])

(defn datepicker [store]
  "responsible for rending the datepicker to the screen"
  (let [vals (:end @store)
        date (atom {:month "january" :year 2019})]
    (fn []
      [:div
        [:input {:type "text"
                 :value (str (:month (:end @store)) " " (:day (:end @store)) ", " (:year (:end @store)))
                 :on-click #(open-datepicker)}]
        (generate-html store date)])))