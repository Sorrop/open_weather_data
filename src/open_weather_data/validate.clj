(ns open-weather-data.validate
  (:require [open-weather-data.state.cache :refer [cache]]
            [open-weather-data.utils :refer [remote-call]]))

(defn find-place-id [[city-name country-name country-code]]
  (some #(when (and (= (:city-name %) city-name)
                    (or (= (:country-name %) country-name)
                        (= (:country-code %) country-code)))
           %)
        (:city-matchings @cache)))

(defn validate-cities [cities]
  (zipmap cities (map find-place-id cities)))


(defn conform [cities]
  (let [validated (validate-cities cities)]
    (-> (group-by (fn [[[city-name country-name country-code] entry]]
                    (if (nil? entry)
                      :invalid
                      :valid))
                  validated)
        (update :valid #(->> (map second %)
                             (into [])))
        (update :invalid #(map first %)))))
