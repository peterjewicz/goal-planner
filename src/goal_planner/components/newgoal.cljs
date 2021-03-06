(ns goal-planner.components.newgoal
  (:require [reagent.core :as reagent :refer [atom]]
            [goal-planner.state.state :refer [handle-state-change]]
            [goal-planner.scripts.dates :refer [get-date-by-days]]
            [goal-planner.scripts.localforageApi :as api]
            [goal-planner.scripts.datepicker :as datepicker]
            [fancy_alert.core :as fancy-alert]
            ["moment" :as moment]))

(defn check-string-blank [string]
  "returns bool based on if a string is blank or not"
  (if (clojure.string/blank? string)
    false
    true))

(defn handle-save [goal]
  "Checks the promise from api and either saves the result or alerts title already taken"
  (.then (api/add-goal (conj @goal {:start (.getTime (js/Date.))})) (fn [result] ; locallForage returns a promise
    (if result
      (reset! goal {:title "" :criteria ""
                           :start ""
                           :end {:day "1" :month "January" :year "2019"}
                           :milestones []
                           :progress []})
      (fancy-alert/fancy-alert
        {:text "Title Is Already Taken!" :hideAfterN false
         :styles {:background "white;" :border "2px solid #2f8ffb;" :width "300px;" :margin-left "-150px;" :z-index "999;" :color "black;"}
         :buttonProperties {:buttonText "Okay"}})))))

(defn save-goal [goal]
  "Saves our goal to localStorage"
  (if (and (integer? (js/parseInt (:criteria @goal))) (check-string-blank (:title @goal)) (check-string-blank (:criteria @goal))) ; TODO move these to a pre in the API
     (handle-save goal)
      (fancy-alert/fancy-alert
        {:text "Completion (Must be a number) and Title are Required" :hideAfterN false
         :styles {:background "white;" :border "2px solid #2f8ffb;" :width "300px;" :margin-left "-150px;" :z-index "999;" :color "black;"}
         :buttonProperties {:buttonText "Okay"}})))

(defn generate-default-milestones [details value]
  "generates 4 default milestones based on goal"
  (swap! details conj {:criteria value})

  (let [quarterValue (/ value 4)]
    (swap! details conj {:milestones [{:date "1/2/18" :value quarterValue} ; TODO we need to actually generate dates
                 {:date "1/3/18" :value (* 2 quarterValue)}
                 {:date "1/4/18" :value (* 3 quarterValue)}
                 {:date "1/5/18" :value (* 4 quarterValue)}]})))

(defn add-milestone [details]
  "adds a new milestone by adding a blank map to the details atom"
  (swap! details conj {:milestones (conj (:milestones @details) {:date "" :value ""})}))

(defn remove-milestone [details index]
  "removes a milestone by removing index from details atom"
  (let [milestones (:milestones @details)]
  (swap! details conj {:milestones (into [](concat (subvec milestones 0 index)
            (subvec milestones (inc index))))})))

(defn update-milestone-date [index details value] ; TODO we can turn this into an ....-field by passing which key to find
  "Updates the date value of a milestone at 'index'"
    (swap! details assoc-in [:milestones index :date] value))

(defn update-milestone-value [index details value]
  "updates value of a milestone at 'index'"
  (swap! details assoc-in [:milestones index :value] value))

(defn render [state]
  (let [details (atom {:title "" :criteria ""
                       :start ""
                       :end {:day "1" :month "January" :year "2019"}
                       :milestones []
                       :progress []})] ; TODO Think we can safely remove
    (fn []
      [:div.New.ViewPage.View {:class (:newgoal (:activeView @state))}
        [:div.New.header
          [:div.header__imageWrapper {:on-click #(handle-state-change "update-current-view" "home")}
            [:img.backButton {:src "back.png"}]]
          [:p "Add New Goal"]]
        [:div.New.content
          [:h2 "I Want To..."]
          [:input {:type "Text" :placeholder "Goal Name" :value (:title @details) :on-change #(swap! details conj {:title (-> % .-target .-value)})}]
          [:h3.borderText "Goal Completion"]
          [:input {:type "number" :placeholder "End Criteria" :value (:criteria @details) :on-change #(generate-default-milestones details (-> % .-target .-value))}]
          [:h3.borderText "Due Date"]
          [datepicker/datepicker details]
          ; DISABLE MILESTONES FOR NOW we can bring them back later if they make sense
          ; [:div.milestonesWrapper
          ;   [:h3.borderText "Milestones"]
          ;   (if (<  0 (count (:criteria @details)))
          ;     (let [milestones (:milestones @details)]
          ;       (doall (map-indexed (fn [index milestone]
          ;         [:div.milestoneItem {:key (str index "-" (:value milestone))} ;we append the value here so the dispaly updates otherwise the same key renders the same content
          ;           [:input.milestoneDate {:type "text" :on-change #(update-milestone-date index details (-> % .-target .-value)) :defaultValue (:date milestone)}]
          ;           [:input.milestoneValue {:type "text" :on-change #(update-milestone-value index details (-> % .-target .-value)) :defaultValue (:value milestone)}]
          ;           [:p.removeMilestone {:on-click #(remove-milestone details index)} "-"]]
          ;       ) milestones))))
          ;   (if (<  0 (count (:criteria @details))) ; TODO make this wrapped in the one above
          ;     [:button.addMilestone.secondary {:on-click #(add-milestone details)} "+"])]
          [:button.primary.saveGoal {:on-click #(save-goal details)} "Save Goal"]]])))


