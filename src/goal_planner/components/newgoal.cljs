(ns goal-planner.components.newgoal
  (:require [reagent.core :as reagent :refer [atom]]
            [goal-planner.state.state :refer [handle-state-change]]
            [goal-planner.scripts.dates :refer [get-date-by-days]]
            [goal-planner.scripts.localforageApi :as api]
            ["moment" :as moment]))

(defn save-goal [goal]
  "Saves our goal to localStorage"
  (api/add-goal @goal))

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
    (swap! details assoc-in [:milestones index (key "date")] value)) ; TODO see if this works!

(defn update-milestone-value [index details value]
  "updates value of a milestone at 'index'"
  (swap! details assoc-in [:milestones index :value] value))

(defn render [state]
  (let [details (atom {:title "" :criteria "" :current 0
                       :days 0 :weeks 0 :years 1
                       :milestones []})]
    (fn []
      [:div.New.ViewPage.View {:class (:newgoal (:activeView @state))}
        [:div.New.header
          [:p {:on-click #(handle-state-change "update-current-view" "home")} "<- Back"]
          [:p "Add New Goal"]]
        [:h2 "I Want To..."]
        [:input {:type "Text" :placeholder "Goal Name" :on-change #(swap! details conj {:title (-> % .-target .-value)})}]
        [:h3.borderText "Goal Completion"]
        [:input {:type "Text" :placeholder "End Criteria" :on-change #(generate-default-milestones details (-> % .-target .-value))}]
        [:h3.borderText "Timeline"]
        [:div.timelineItem
          [:input.timelineInput {:type "text" :on-change #(swap! details conj {:days (-> % .-target .-value)})}]
          [:p.timelineLabel "Days"]]
        [:div.timelineItem
          [:input.timelineInput {:type "text" :on-change #(swap! details conj {:weeks (-> % .-target .-value)})}]
          [:p.timelineLabel "Weeks"]]
        [:div.timelineItem
           [:input.timelineInput {:type "text" :defaultValue 1 :on-change #(swap! details conj {:years (-> % .-target .-value)})}]
           [:p.timelineLabel "years"]]
        [:div.milestonesWrapper
          [:h3.borderText "Milestones"]
          (if (<  0 (count (:criteria @details)))
            (let [milestones (:milestones @details)]
              (doall (map-indexed (fn [index milestone]
                [:div.milestoneItem {:key (str index "-" (:value milestone))} ;we append the value here so the dispaly updates otherwise the same key renders the same content
                  [:input.milestoneDate {:type "text" :on-change #(update-milestone-date index details (-> % .-target .-value)) :defaultValue (:date milestone)}]
                  [:input.milestoneValue {:type "text" :on-change #(update-milestone-value index details (-> % .-target .-value)) :defaultValue (:value milestone)}]
                  [:p.removeMilestone {:on-click #(remove-milestone details index)} "-"]]
              ) milestones))))
          (if (<  0 (count (:criteria @details))) ; TODO make this wrapped in the one above
            [:button.addMilestone.secondary {:on-click #(add-milestone details)} "+"])]
        [:button.primary.saveGoal {:on-click #(save-goal details)} "Save Goal"]])))


