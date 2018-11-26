(ns open-weather-data.s3-ops
  (:require [amazonica.aws.s3 :as s3]
            [amazonica.aws.s3transfer :as s3transfer]
            [amazonica.core :refer [with-credential
                                    defcredential]]
            [clojure.java.io :as io]))



(defn send-to-s3 [app-config file-name]
  (let [csv-path (:csv-files-dir app-config)
        {:keys [creds bucket]} (:aws-config app-config)
        {:keys [access-key
                secret-key
                endpoint]} creds]
    (with-credential [access-key secret-key endpoint]
      (let [prog-listener (fn [{:keys [event] :as prog}]
                            (when-not (nil? event)
                              (case event
                                :started (println (str "Start transfer of "
                                                       file-name))
                                :transfered (println (str "Transfered "
                                                          (:bytes-transferred prog)
                                                          " bytes for "
                                                          file-name))
                                :completed (println "Completed transfer of " file-name))))
            upl (s3transfer/upload bucket
                                   file-name
                                   (io/file (str csv-path file-name)))]
        ((:add-progress-listener upl) prog-listener)))))

(defn receive-from-s3 [app-config file-name]
  (let [csv-path (:csv-files-dir app-config)
        {:keys [creds bucket]} (:aws-config app-config)
        {:keys [access-key
                secret-key
                endpoint]} creds]
    (with-credential [access-key secret-key endpoint]
      (let [prog-listener (fn [{:keys [event] :as prog}]
                            (when-not (nil? event)
                              (case event
                                :started (println (str "Start transfer of "
                                                       file-name))
                                :transfered (println (str "Transfered "
                                                          (:bytes-transferred prog)
                                                          " bytes for "
                                                          file-name))
                                :completed (println "Completed transfer of " file-name))))
            upl (s3transfer/download bucket
                                     file-name
                                     (io/file (str csv-path file-name)))]
        ((:add-progress-listener upl) prog-listener)))))
