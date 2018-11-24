(ns open-weather-data.state.app-config
  (:require [clojure.edn :as edn]
            [mount.core :refer [defstate]]))

(defstate app-config
  :start (edn/read-string (slurp "config.edn"))
  :stop {})
