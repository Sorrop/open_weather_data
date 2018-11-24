(ns open-weather-data.city-matchings
  (:require [clojure.edn :as edn]
            [clojure.java.io :as io]
            [cheshire.core :refer [generate-string
                                   parse-string
                                   parse-stream]]))


(defn read-json-resource [json]
  (with-open [instr (-> json
                        io/resource
                        io/file
                        io/input-stream)]
    (->> (parse-stream (io/reader instr) keyword)
         (into []))))

(defn filter-cities [data])

(defn code->name [country-codes]
  (->> country-codes
       (mapcat (fn [country]
                 [(:alpha-2 country) (:name country)]))
       (apply hash-map)))

(defn write-city-data [city-data file-name]
  (with-open [w (-> (str "resources/"
                         file-name)
                    io/file
                    io/writer)]
    (binding [*print-length* nil
              *out* w]
      (pr city-data))))

(defn produce-city-matchings [{:keys [cities-ids-file
                                      country-codes-file]}]
  (let [city-data (read-json-resource cities-ids-file)
        country-codes (read-json-resource country-codes-file)
        code-name (code->name country-codes)]
    (->> (pmap (fn [{:keys [name id country]}]
                 {:city-name name
                  :id id
                  :country-code country
                  :country-name (get code-name country "NOT-FOUND")})
               city-data)
         (into []))))

(defn city-matchings-task [resources output]
  (try (let [data (produce-city-matchings resources)]
         (write-city-data data output))
       (catch Exception e (str "Error. Caught exception: "
                               (.getMessage e)))))

(comment
  (city-matchings-task
   {:cities-ids-file "current.city.list.json"
    :country-codes-file "all_316_country_codes.json"}
   "city_matchings.edn")

  (let [s (slurp "resources/city_matchings.edn")
        d (edn/read-string s)]
    (some #(= (:country-name %) "NOT-FOUND") d)))
