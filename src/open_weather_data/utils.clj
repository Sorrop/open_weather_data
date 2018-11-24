(ns open-weather-data.utils
  (:require [cheshire.core :refer [parse-string]]
            [clj-http.client :as client]))

(defn remote-call [host endpoint api-key params callback]
  (client/get (str host endpoint)
              {:async? true
               :query-params (merge params
                                    {:appid api-key})
               :accept "json"}
              (fn [response]
                (let [body (:body response)]
                  (-> body
                      (parse-string true)
                      callback)))
              (fn [exception]
                (println "exception message is: "
                         (.getMessage exception)))))
