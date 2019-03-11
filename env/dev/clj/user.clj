(ns user
  (:require [pweb-app.config :refer [env]]
            [clojure.spec.alpha :as s]
            [expound.alpha :as expound]
            [mount.core :as mount]
            [pweb-app.figwheel :refer [start-fw stop-fw cljs]]
            [pweb-app.core :refer [start-app]]
            [pweb-app.db.core]
            [conman.core :as conman]
            [luminus-migrations.core :as migrations]))

(alter-var-root #'s/*explain-out* (constantly expound/printer))

(defn start []
  (mount/start-without #'pweb-app.core/repl-server))

(defn stop []
  (mount/stop-except #'pweb-app.core/repl-server))

(defn restart []
  (stop)
  (start))

(defn restart-db []
  (mount/stop #'pweb-app.db.core/*db*)
  (mount/start #'pweb-app.db.core/*db*)
  (binding [*ns* 'pweb-app.db.core]
    (conman/bind-connection pweb-app.db.core/*db* "sql/queries.sql")))

(defn reset-db []
  (migrations/migrate ["reset"] (select-keys env [:database-url])))

(defn migrate []
  (migrations/migrate ["migrate"] (select-keys env [:database-url])))

(defn rollback []
  (migrations/migrate ["rollback"] (select-keys env [:database-url])))

(defn create-migration [name]
  (migrations/create name (select-keys env [:database-url])))


