(ns open-weather-data.core
  (:require [clojure.java.jdbc :as jdbc]
            [clj-http.client :as client]
            [open-weather-data.utils :refer [remote-call]]
            [open-weather-data.state.app-config :refer [app-config]]
            [open-weather-data.state.database :refer [database]]
            [open-weather-data.state.cache :refer [cache]]
            [open-weather-data.state.persistence :refer [persistence-listener]]
            [open-weather-data.validate :refer [conform]]
            [open-weather-data.fetch :refer [fetch-current]]
            [mount.core :as mount]
            [clojure.tools.namespace.repl :as tn])
  (:gen-class))


;; FIXME: move to dev namespace
(tn/set-refresh-dirs "src")

(defn go []
  (mount/start)
  :ready)

(defn reset []
  (mount/stop)
  (tn/refresh :after 'open-weather-data.core/go))

(go)

(def places
  [["Athens" "Greece" "GR"]
   ["Paris" "France" "FR"]
   ["London" "UK" "GB"]
   ["Madrid" "Spain" "ES"]
   ["Moscow" "Russia" "RU"]
   #_["Rome" "Italy" "IT"]])


(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))
