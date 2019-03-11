(ns pweb-app.env
  (:require [clojure.tools.logging :as log]))

(def defaults
  {:init
   (fn []
     (log/info "\n-=[pweb-app started successfully]=-"))
   :stop
   (fn []
     (log/info "\n-=[pweb-app has shut down successfully]=-"))
   :middleware identity})
