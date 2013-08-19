(ns clojure-rest-example.riak
      (:require [clojurewerkz.welle.core :as riak])
)





  ;; connects to a Riak node at http://127.0.0.1:8098/riak
(riak/connect!)

;; connects to a Riak node at the given endpoint, client id will be generated
(riak/connect! "http://localhost:8098/riak")

;; the same as the previous example but also uses provided client id
;(riak/connect! "http://riak.data.megacorp.internal:8098/riak" "myapp-client.0001")

    (defn uuid [] (str (java.util.UUID/randomUUID)))
