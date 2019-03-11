(ns pweb-app.routes.home
  (:require [pweb-app.layout :as layout]
            [pweb-app.db.core :as db]
            [pweb-app.routes.ws :as ws]
            [compojure.core :refer [defroutes GET POST]]
            [ring.util.http-response :as response]
            [ring.util.response :refer [response]]
            [clojure.java.io :as io]))

(defn home-page 
  ""
  [request]
  (layout/render request "home.html"))

(defn about-page 
  ""
  [request]
  (layout/render request "about.html"))

(defn dog-page
  ""
  [request]
  (layout/render request "dogs.html"))

(defroutes home-routes
  (GET "/" request (home-page request))
  (GET "/about" request (about-page request))
  (GET "/dogs" request (dog-page request))
  (GET "/docs" []
       (-> (response/ok (-> "docs/docs.md" io/resource slurp))
           (response/header "Content-Type" "text/plain; charset=utf-8")))
  (GET "/messages" [] (response (db/get-messages))))

