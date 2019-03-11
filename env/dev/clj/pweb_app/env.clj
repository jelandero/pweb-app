(ns pweb-app.env
  (:require [selmer.parser :as parser]
            [clojure.tools.logging :as log]
            [pweb-app.dev-middleware :refer [wrap-dev]]))

(def defaults
  {:init
   (fn []
     (parser/cache-off!)
     (log/info "\n-=[pweb-app started successfully using the development profile]=-"))
   :stop
   (fn []
     (log/info "\n-=[pweb-app has shut down successfully]=-"))
   :middleware wrap-dev})
