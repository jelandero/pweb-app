(ns pweb-app.routes.ws
  (:require [compojure.core :refer [GET defroutes wrap-routes]]
            [clojure.tools.logging :as log]
            [cognitect.transit :as transit]
            [pweb-app.db.core :as db]
            [immutant.web.async :as async]))

(defonce channels (atom #{}))

(defn save-message! 
  [message]
  (do
    (db/save-message! message)
    message))

(defn decode-transit [message]
  (let [in (java.io.ByteArrayInputStream. (.getBytes message))
        reader (transit/reader in :json)]
    (transit/read reader)))

(defn notify-clients! 
  ""
  [channel msg]
  (let [response (-> msg
                     decode-transit
                     (assoc :timestamp (java.util.Date.))
                     save-message!)]
  (doseq [channel @channels]
    (async/send! channel msg))))

(defn connect! 
  ""
  [channel]
  (log/info "channel open")
  (swap! channels conj channel))

(defn disconnect! 
  ""
  [channel {:keys [code reason]}]
  (log/info "close code:" code "reason:" reason)
  (swap! channels #(remove #{channel} %)))

(def websocket-callbacks
  "WebSocket callback functions"
  {:on-open connect!
   :on-close disconnect!
   :on-message notify-clients!})

(defn ws-handler [request]
  (async/as-channel request websocket-callbacks))

(defroutes websocket-routes
  (GET "/ws" [] ws-handler))