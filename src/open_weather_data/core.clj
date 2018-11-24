(ns open-weather-data.core
  (:require [clojure.java.jdbc :as jdbc]
            [clj-http.client :as client]
            [open-weather-data.utils :refer [remote-call]]
            [open-weather-data.state.app-config :refer [app-config]]
            [open-weather-data.state.database :refer [database]]
            [open-weather-data.state.cache :refer [cache]]
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

#_(def places
    [["Athens" "Greece" "GR"]
     ["Paris" "France" "FR"]
     ["London" "UK" "GB"]
     ["Madrid" "Spain" "ES"]
     ["Moscow" "Russia" "RU"]
     ["Rome" "Italy" "IT"]])

(defn find-place-id [[city-name country-name country-code]]
  (some #(when (and (= (:city-name %) city-name)
                    (or (= (:country-name %) country-name)
                        (= (:country-code %) country-code)))
           %)
        (:city-matchings @cache)))


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

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))
