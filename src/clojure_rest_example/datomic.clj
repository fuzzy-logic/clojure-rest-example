(ns clojure-rest-example.datomic
  (:use [clojure.test]))

(use '[datomic.api :only [q db] :as d])
(use 'clojure.pprint)


(def datomic-uri "datomic:free://localhost:4334//customer")


(defn uuid [] (str (java.util.UUID/randomUUID)))

(def connection (d/connect datomic-uri)  )

(defn dbinit []
  (println "setting up datomic ...")
  (d/create-database datomic-uri)
  (let [schema-file (read-string (slurp "data/datomic_schema.dtm"))
       transaction @(d/transact connection schema-file)]
    (println "read schema from file: " schema-file)
    (println "commit schema transaction results: " transaction)
  )
)


(defn add-data [data]
  (println "adding data: " data)
  ;; add some data:
  ;; -1 used as tempid for :recording/search_term, then re-used for parent data :customer/recording
  (let [data-tx [
    {:recording/search_term (:search_term (:recording data)), :db/id #db/id[:db.part/user -1]}
    {:customer/skyid (uuid), :customer/name (:name (:customer data)), :db/id #db/id[:db.part/user], :customer/recording #db/id[:db.part/user -1]}
  ]]

    (println "commit transaction for data add...")
    (not (nil? @(d/transact connection data-tx)))
  )

)




(defn run-query []
  (println "creating query...")

  (let [results (q '[:find ?n :where [?n :customer/name]] (db connection))
        id (ffirst results)
        entity (-> connection db (d/entity id))
        ]

    (println "query result count: " (count results))
    (println "result type: " (type results))
    (println "results: " results)
    (println "first results: " (first results))
    ;; display the entity map's keys
    (println "keys/entity: " (keys entity))

    ;; display the value of the entity's customer name
    (println ":customer/name: " (:customer/name entity))
    entity
  )
)

