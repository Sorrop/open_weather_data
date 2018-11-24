(defproject open_weather_data "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.9.0"]
                 [org.postgresql/postgresql "42.1.4"]
                 [org.clojure/java.jdbc "0.7.8"]
                 [clj-http "3.9.1"]
                 [org.clojure/core.async "0.4.490"]
                 [cheshire "5.8.1"]
                 [mount "0.1.14"]
                 [clj-postgresql "0.7.0"]
                 [org.clojure/tools.namespace "0.3.0-alpha4"]
                 [clj-time "0.15.0"]]
  :main ^:skip-aot open-weather-data.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
