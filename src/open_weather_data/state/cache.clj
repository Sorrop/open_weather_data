(ns open-weather-data.state.cache
  (:require [mount.core :refer [defstate]]
            [open-weather-data.state.app-config :refer [app-config]]
            [clojure.java.io :as io]
            [clojure.edn :as edn]))

(defstate cache
  :start (let [{:keys [city-matchings-file]} (:resources app-config)]
           (atom {:city-matchings (-> city-matchings-file
                                      io/resource
                                      io/file
                                      slurp
                                      edn/read-string)}))
  :stop (reset! cache {}))
