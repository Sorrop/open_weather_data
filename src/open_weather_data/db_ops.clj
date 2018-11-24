(ns open-weather-data.db-ops
  (:require [clojure.java.jdbc :as jdbc]
            [clj-time.core :as t]
            [clj-time.local :as l]
            [clj-time.coerce :as c]))


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
                   :temperature temp
                   :created_at (c/to-sql-time (l/local-now))})))
