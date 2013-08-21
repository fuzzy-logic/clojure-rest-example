(ns clojure-rest-example.datomic
  (:use [clojure.test]))

(use '[datomic.api :only [q db] :as d])
(use 'clojure.pprint)


(def datomic-uri "datomic:free://localhost:4334//customer")


(defn uuid [] (str (java.util.UUID/randomUUID)))

(defn dbinit []
  (println "setting up datomic ...")
  (d/create-database datomic-uri)

  (let [conn (d/connect datomic-uri)
       schema-file (read-string (slurp "data/datomic_schema.dtm"))]
      (println "reading schema from file: " schema-file)
      (println "commit schema transaction results: " @(d/transact conn schema-file))
      conn
  )
)


(defn add-data [conn]
  (println "adding data...")
  ;; add some data:
  ;; -1 used as tempid for :recording/search_term, then re-used for parent data :customer/recording
  (def data-tx [
    {:recording/search_term "Dexter", :db/id #db/id[:db.part/user -1]}
    {:customer/skyid (uuid), :customer/name "Billy Bob Thornton", :db/id #db/id[:db.part/user], :customer/recording #db/id[:db.part/user -1]}
  ])

  (println "commit transaction for data add...")
  @(d/transact conn data-tx)
)




(defn run-query [conn]
  (println "creating query...")

  (let [results (q '[:find ?n :where [?n :customer/name]] (db conn))
        id (ffirst results)
        entity (-> conn db (d/entity id))
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

