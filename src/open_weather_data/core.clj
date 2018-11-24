(ns open-weather-data.core
  (:require [clojure.java.jdbc :as jdbc]
            [clj-http.client :as client]
            [open-weather-data.utils :refer [remote-call]])
  (:gen-class))

(def db-spec {:dbtype "postgresql"
              :dbname "open_weather"
              :host "localhost"
              :user "chris"
              :password "123456"})

(def places
  [["Athens" "Greece" "GR"]
   ["Paris" "France" "FR"]
   ["London" "UK" "GB"]
   ["Madrid" "Spain" "ES"]
   ["Moscow" "Russia" "RU"]
   ["Rome" "Italy" "IT"]])

(def city-matchings
  (let [s (slurp (clojure.java.io/resource "city_matchings.edn"))]
    (clojure.edn/read-string s)))

(defn find-place-id [[city-name country-name country-code]]
  (some #(when (and (= (:city-name %) city-name)
                    (or (= (:country-name %) country-name)
                        (= (:country-code %) country-code)))
           %)
        city-matchings))

(def open-weather-api
  {:api-key "fe608999ec2ecf72e44d973606ddc575"
   :host "http://api.openweathermap.org/data/2.5/"})

(comment
  (jdbc/query db-spec ["SELECT * FROM current_reports"])
  (let [host (:host open-weather-api)
        app-id (:api-key open-weather-api)
        endpoint "/weather"
        params {:id 264371
                :units "metric"}
        form (remote-call host endpoint
                          app-id
                          params
                          println)]
    form))

(comment
  "response"
  {:coord {:lon -3.7,
           :lat 40.42},
   :cod 200,
   :name "Madrid",
   :dt 1542992400,   ----> time of calculation unix epoch utc
   :wind {:speed 4.1,
          :deg 230},
   :id 3117735,
   :weather [
             {:id 801,
              :main "Clouds",
              :description "few clouds",
              :icon 02n}],
   :clouds {:all 20},
   :sys {:type 1,
         :id 5505,
         :message 0.0282,
         :country ES,
         :sunrise 1542957038,
         :sunset 1542991907},
   :base "stations",
   :main {:temp 9,
          :pressure 1015,
          :humidity 70,
          :temp_min 7,
          :temp_max 10},
   :visibility 10000}
  )

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))
