(ns open-weather-data.state.database
  (:require [mount.core :refer [defstate]]
            [open-weather-data.state.app-config :refer [app-config]]
            [clj-postgresql.core :as pg]))

(defstate database
  :start (let [dbspec (:database app-config)]
           (pg/pool :host (:host dbspec)
                    :user (:user dbspec)
                    :dbname (:dbname dbspec)
                    :password (:password dbspec)))
  :stop (pg/close! database))

