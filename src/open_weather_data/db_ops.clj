(ns open-weather-data.db-ops
  (:require [clojure.java.jdbc :as jdbc]
            [clojure.java.shell :as shell]
            [cheshire.core :refer [generate-string]]
            [clj-time.core :as t]
            [clj-time.local :as l]
            [clj-time.coerce :as c]
            [clj-time.jdbc])
  (:import [org.postgresql.util PGobject]))


(defmulti db-operation
  (fn [action city data db]
    action))

(defmethod db-operation :current
  [action city data db]
  (let [{:keys [city-name id country-name country-code]} city
        {:keys [temp]} (:main data)]
    (let [sql "INSERT INTO current_reports(city_name, city_id, country_name, country_code, temperature)
                  VALUES(?,?,?,?,?)
               ON CONFLICT (city_id)
               DO UPDATE SET city_name=?, city_id=?, country_name=?, country_code=?, temperature=?"
          params [city-name id country-name country-code temp]
          sqlvec (->> (concat [sql] params params)
                      (into []))]
      (jdbc/execute! db sqlvec))))

(defn json-obj [data]
  (doto (PGobject.)
    (.setType "json")
    (.setValue (generate-string data))))

(defn insert-in-json-table! [db city data]
  (let [{:keys [city-name id country-name country-code]} city]
    (jdbc/insert! db "forecasts"
                  {:city_name city-name
                   :city_id id
                   :country_name country-name
                   :country_code country-code
                   :forecasts (json-obj data)})))

(defn insert-in-normal-table! [db city data]
  (let [{:keys [city-name id country-name country-code]} city
        rows  (mapv (fn [datum]
                      {:city_id id
                       :city_name city-name
                       :country_name country-name
                       :country_code country-code
                       :forecast_date (-> datum
                                          :dt
                                          (c/from-epoch)
                                          (c/to-sql-time))
                       :forecast_temp (:temp datum)})
                    data)]
    (jdbc/insert-multi! db "forecasts" rows)))

(defmethod db-operation :forecast
  [action city data db]
  #_(insert-in-json-table! db city data)
  (insert-in-normal-table! db city data))

(defn table<->csv [app-config table-name file-name direction]
  (let [root-dir (get app-config :csv-files-dir)
        sql (str "\\copy "
                 table-name
                 (if (= direction :from)
                   " FROM "
                   " TO ")
                 (str \' root-dir file-name ".csv" \')
                 " DELIMITER ',' CSV HEADER")]
    (shell/sh "psql"
              "-d"
              (get-in app-config
                      [:database :dbname])
              "-c"
              sql)))
