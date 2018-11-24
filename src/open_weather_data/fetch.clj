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
      #_(put! persistence-chan [1 2 3 4])
      (remote-call host
                   endpoint
                   app-id
                   params
                   (fn [data]
                     (put! persistence-listener [:current city data cities-count]))))))
