(defproject clojure-rest-example "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :repositories {"clojars.org" "http://clojars.org/repo"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [compojure "1.1.5"]
                 [ring/ring-json "0.1.2"]
                 [c3p0/c3p0 "0.9.1.2"]
                 [org.clojure/java.jdbc "0.2.3"]
                 [com.datomic/datomic-free "0.8.4138"]
                 [com.h2database/h2 "1.3.168"]
                 [com.novemberain/welle "1.6.0-beta1"]
                 [cheshire "4.0.3"]
  ]
  :plugins [[lein-ring "0.8.5"]]
  :ring {:handler clojure-rest-example.handler/app}
  :profiles
  {:dev {:dependencies [[ring-mock "0.1.5"]]}})
