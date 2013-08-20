(ns clojure-rest-example.datomic
  (:use [clojure.test]))

(use '[datomic.api :only [q db] :as d])
(use 'clojure.pprint)


(defn uuid [] (str (java.util.UUID/randomUUID)))


(println "starting datomic stuff..")
(def uri "datomic:free://localhost:4334//customer")
(d/create-database uri)
(def conn (d/connect uri))

(println "reading schema")
(def schema-tx (read-string (slurp "data/datomic_schema.dtm")))
(println "schema-tx:")
(pprint schema-tx)

@(d/transact conn schema-tx)

(println "adding data...")
;; add some data:
;; -1 used as tempid for :recording/search_term, then re-used for parent data :customer/recording
(def data-tx [
  {:recording/search_term "Dexter", :db/id #db/id[:db.part/user -1]}
  {:customer/skyid (uuid), :customer/name "Billy Bob Thornton", :db/id #db/id[:db.part/user], :customer/recording #db/id[:db.part/user -1]}
])

(println "commit transaction for data add...")
@(d/transact conn data-tx)
(println "transaction committed :-)")



(println "creating query...")
(def results (q '[:find ?n :where [?n :customer/name]] (db conn)))
(println "query result count: " (count results))
(println "result type: " (type results))
(println "results: " results)
(println "first results: " (first results))

(def id (ffirst results))
(def entity (-> conn db (d/entity id)))

;; display the entity map's keys
(println "keys/entity: " (keys entity))


;; display the value of the entity's customer name
(println ":customer/name: " (:customer/name entity))
