(ns goal-planner.scripts.datepicker
  (:require [reagent.core :as reagent :refer [atom]]
            ["moment" :as moment]))


; we call the datepicker it should default to todays date for now
; should drop down - need three rows year month day
; If jan it should have 31 days
; as you switch months it should change how many day you have
; (let [bodyElem (.-body js/document)]
;
; )
(defonce months ["January" "February" "March" "April" "May"
                 "June" "July" "August" "September" "October"
                 "November" "December"])

(defonce defaultStyles {
  :position "fixed"
  :width "100%"
  :height "100%"
  :padding 16
  :text-align "center"
  :background "white"
  :display "flex"
  :flex-direction "column"
  :justify-content "center"
  :top 0
  :left 0
  :z-index -999})

(defn generate-css [styles]
  "simple helper to add the stles to the domElement"
  (str "style=\""(clojure.string/join " "  (map (fn [[key val]] (str (name key) ": " val)) styles))"\""))

(defn generate-months []
  (map (fn [month]
          (str "<option value='"month"'>"month"</option>")) months))
(defn generate-years []
  (map (fn [year]
          (str "<option value='"year"'>"year"</option>")) (range 2019 2050)))

(defn generate-days []
  (map (fn [day]
          (str "<option value='"day"'>"day"</option>")) (range 1 32)))

; (defn generate-html []
;   "Generates datepicker html"
;   (str "<div id=\"datepicker\" "(generate-css defaultStyles)">
;           <div class=\"datepicker__inner\">
;             <select onChange='"'(dateChange)"'>"
;               (generate-months)
;           "</select>
;             <select>"
;               (generate-days)
;             "</select>
;             <select>"
;               (generate-years)
;             "</select>
;           </div>
;         </div>"))
; .style.color
(defn generate-html []
  [:div#datepicker {:style defaultStyles}
    [:p "test"]])

(defn open-datepicker []
  (let [bodyElem (.getElementById js/document "datepicker")]
    ; (print (aget  (.-style bodyElem) "z-index"))
    (aset  (.-style bodyElem) "z-index" "900")
))
(defn datepicker [dateChange]
  "responsible for rending the datepicker to the screen"
  [:div
    [:input {:type "text" :on-click #(open-datepicker)}]
    (generate-html)]
  )
; :on-click #(open-datepicker dateChange)
; :on-change #(js/alert (-> % .-target .-value))