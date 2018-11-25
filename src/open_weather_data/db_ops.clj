(ns open-weather-data.db-ops
  (:require [clojure.java.jdbc :as jdbc]
            [cheshire.core :refer [generate-string]]
            [clj-time.core :as t]
            [clj-time.local :as l]
            [clj-time.coerce :as c])
  (:import [org.postgresql.util PGobject]))


(defmulti db-operation
  (fn [action city data db]
    action))

(defmethod db-operation :current
  [action city data db]
  (let [{:keys [city-name id country-name country-code]} city
        {:keys [temp]} (:main data)]
    (jdbc/insert! db "current_reports"
                  {:city_name city-name
                   :city_id id
                   :country_name country-name
                   :country_code country-code
                   :temperature temp})))

(defn json-obj [data]
  (doto (PGobject.)
    (.setType "json")
    (.setValue (generate-string data))))

(defmethod db-operation :forecast
  [action city data db]
  (let [{:keys [city-name id country-name country-code]} city]
    (jdbc/insert! db "forecasts"
                  {:city_name city-name
                   :city_id id
                   :country_name country-name
                   :country_code country-code
                   :forecasts (json-obj data)})))
