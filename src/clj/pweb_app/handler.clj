(ns pweb-app.handler
  (:require [pweb-app.middleware :as middleware]
            [pweb-app.layout :refer [error-page]]
            [pweb-app.routes.home :refer [home-routes]]
            [pweb-app.routes.ws :refer [websocket-routes]]
            [compojure.core :refer [routes wrap-routes]]
            [ring.util.http-response :as response]
            [compojure.route :as route]
            [pweb-app.env :refer [defaults]]
            [mount.core :as mount]))

(mount/defstate init-app
  :start ((or (:init defaults) identity))
  :stop  ((or (:stop defaults) identity)))

(def app-routes
  ""
  (routes
    websocket-routes
    (-> #'home-routes
        (wrap-routes middleware/wrap-csrf)
        (wrap-routes middleware/wrap-formats))
    (route/not-found
      (:body
        (error-page {:status 404
                     :title "page not found"})))))

(def app (middleware/wrap-base #'app-routes))