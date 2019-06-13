(ns goal-planner.scripts.datepicker
  (:require ["moment" :as moment]))

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
  (str "style=\""(clojure.string/join " " (map (fn [[key val]] (str (name key) ": " val)) styles))"\""))

(defn generate-months []
  (map (fn [month]
           [:option {:value month :key month} month]) months))

(defn generate-years []
  (map (fn [year]
           [:option {:value year :key year} year]) (range 2019 2050)))

(defn generate-days []
  (map (fn [day]
           [:option {:value day :key day} day]) (range 1 32)))


(defn open-datepicker []
  (let [bodyElem (.getElementById js/document "datepicker")]
    (aset  (.-style bodyElem) "z-index" "900")))

(defn close-datepicker []
  (let [bodyElem (.getElementById js/document "datepicker")]
    (aset  (.-style bodyElem) "z-index" "-999")))

(defn update-passed-store [type val store]
  "Updates the values of the passed in atom expected {:day :month :year} map"
  (swap! store assoc-in [:end (keyword type)] val)
)

(defn generate-html [store]
  [:div#datepicker {:style defaultStyles}
    [:p  {:on-click #(close-datepicker)} "x"]
    [:div#datepicker.datepickerinnner
    [:select {:default-value (:end (:month @store)) :on-change #(update-passed-store "month" (-> % .-target .-value) store)}
      (generate-months)]
    [:select {:on-change #(update-passed-store "day" (-> % .-target .-value) store)}
      (generate-days)]
    [:select {:on-change #(update-passed-store "year" (-> % .-target .-value) store)}
      (generate-years)]]])

(defn datepicker [store]
  "responsible for rending the datepicker to the screen"
  (let [vals (:end @store)]
    (fn []
      [:div
        [:input {:type "text"
                 :value (str (:month (:end @store)) " " (:day (:end @store)) ", " (:year (:end @store)))
                 :on-click #(open-datepicker)}]
        (generate-html store)])))