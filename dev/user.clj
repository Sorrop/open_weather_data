(ns user
  (:require [mount.core :as mount]
            [clojure.tools.namespace.repl :as tn]))


(tn/set-refresh-dirs "src")

(defn go []
  (tn/refresh :after 'mount.core/start))

(defn reset []
  (mount/stop)
  (tn/refresh :after 'user/go))

#_(go)
