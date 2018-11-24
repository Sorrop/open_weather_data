(ns open-weather-data.state.persistence
  (:require [clojure.core.async :as async :refer [chan close! <! go-loop]]
            [open-weather-data.state.database :refer [database]]
            [open-weather-data.db-ops :refer [db-operation]]
            [mount.core :refer [defstate]]))

(defn listen-to-persistence-events []
  (let [pers-chan (chan)]
    (async/go-loop []
      (when-let [[action city data cities-count] (<! pers-chan)]
        (db-operation action city data database)
        (recur)))
    pers-chan))

(defstate persistence-listener
  :start (when database
           (listen-to-persistence-events))
  :stop (async/close! persistence-listener))
