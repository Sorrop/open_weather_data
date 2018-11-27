(ns open-weather-data.core
  (:require [clojure.java.jdbc :as jdbc]
            [clj-http.client :as client]
            [open-weather-data.utils :refer [remote-call]]
            [open-weather-data.validate :refer [conform]]
            [open-weather-data.fetch :refer [fetch-current
                                             fetch-forecast]]
            [mount.core :as mount]
            [clojure.tools.namespace.repl :as tn])
  (:gen-class))


(def places
  [["Athens" "Greece" "GR"]
   ["Paris" "France" "FR"]
   ["London" "UK" "GB"]
   ["Madrid" "Spain" "ES"]
   ["Moscow" "Russia" "RU"]
   #_["Rome" "Italy" "IT"]])

(defn retrieve-current [places]
  (let [cities (conform places)]
    (fetch-current (:valid cities))))

(defn retrieve-forecasts [places]
  (let [cities (conform places)]
    (fetch-forecast (:valid cities))))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))
