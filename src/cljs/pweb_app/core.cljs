(ns pweb-app.core
  (:require [reagent.core :as reagent :refer [atom]]
            [pweb-app.ws :as ws]
            [ajax.core :refer [GET POST]]))

(defonce message (atom []))

(defn message-list []
  [:ul.content
   (for [{:keys [timestamp message name]} @message]
     ^{:key timestamp}
     [:li
      [:time (.toLocaleString timestamp)]
      [:p message]
      [:p " - " name]])])

(defn get-messages []
  (GET "/messages"
    {:headers {"Accepts" "application/transit+json"}
     :handler #(reset! message (vec %))}))

(defn message-form []
    (let [fields (atom nil)]
        (fn []
            [:div.container
                [:div.form-group
                    [:p "Name:"
                        [:input.form-control 
                            {:type :text
                             :placeholder "Type in your name"
                             :value (:name @fields)
                             :on-change #(swap! fields assoc :name (-> % .-target .-value))}]]
                    [:p "Message:"
                        [:textarea.form-control
                            {:rows 4
                             :cols 50
                             :placeholder "Start a message"
                             :value (:message @fields)
                             :on-change #(swap! fields assoc :message (-> % .-target .-value))}]]
                    [:input.btn.btn-primary 
                        { :type :submit
                          :on-click #(ws/send-messages! @fields)
                          :value "comment"}]]])))

(defn home-page 
  ""
  []
  (get-messages)
  [:div.container
      [:div.row
          [:div.col
              [:h2 "Welcome to 2DA Guestbook"]]]
      [:div.row
          [:div.col
             [message-list]]]
      [:div.row
          [:div.col
             [message-form]]]])

(defn about-page
  ""
  []
  [:div.container
    [:div.row
      [:div.col
        [:h2 "About This Application"]]]])

(defn update-messages! 
  ""
  [{:keys [name message timestamp]}]
  (get-messages))

(defn mount-components []
  (reagent/render-component [#'home-page] (.getElementById js/document "app")))

(defn init! []
  (ws/make-websocket! (str "ws://" (.-host js/location) "/ws") 
                       update-messages!)
  (mount-components))