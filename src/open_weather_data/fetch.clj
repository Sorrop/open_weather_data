(ns open-weather-data.fetch
  (:require [clojure.core.async :refer [put!]]
            [open-weather-data.state.app-config :refer [app-config]]
            [open-weather-data.state.database :refer [database]]
            [open-weather-data.state.persistence :refer [persistence-listener]]
            [open-weather-data.utils :refer [remote-call]]
            [cheshire.core :refer [parse-string]]))


(defn fetch-current [conformed-cities]
  (let [host (-> app-config
                 :open-weather-api
                 :host)
        app-id (-> app-config
                   :open-weather-api
                   :api-key)
        endpoint "/weather"
        cities-count (count conformed-cities)]
    (doseq [city conformed-cities
            :let [params {:id (:id city)
                          :units "metric"}]]
      (remote-call host
                   endpoint
                   app-id
                   params
                   (fn [data]
                     (put! persistence-listener [:current city data cities-count]))))))


(defn filter-forecasts [data]
  (let [daily (:list data)]
    (map (fn [datum]
           {:temp (-> datum
                      :main
                      :temp)
            :date-txt (:dt_txt datum)
            :dt (:dt datum)})
         daily)))

(defn fetch-forecast [conformed-cities]
  (let [host (-> app-config
                 :open-weather-api
                 :host)
        app-id (-> app-config
                   :open-weather-api
                   :api-key)
        endpoint "/forecast"
        cities-count (count conformed-cities)]
    (doseq [city conformed-cities
            :let [params {:id (:id city)
                          :units "metric"}]]
      (remote-call host
                   endpoint
                   app-id
                   params
                   (fn [data]
                     (put! persistence-listener
                           [:forecast
                            city
                            (filter-forecasts data)
                            cities-count]))))))

(comment
  (let [host (-> app-config
                 :open-weather-api
                 :host)
        app-id (-> app-config
                   :open-weather-api
                   :api-key)
        endpoint "/forecast"
        params {:id 524901
                :units "metric"}]
    (remote-call host
                 endpoint
                 app-id
                 params
                 (fn [data]
                   (put! persistence-listener [:forecast
                                               city
                                               (filter-forecasts data)
                                               cities-count])))))
