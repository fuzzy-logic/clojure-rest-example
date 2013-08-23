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
  (let [skyid (uuid)
        data-tx [
    {:customer/skyid skyid, :customer/name (:name (:customer data)), :db/id #db/id[:db.part/user -1] }
    {:recording/search_term (:search_term (:recording data)), :db/id #db/id[:db.part/user -2], :recording/skyid skyid, :customer/_recording #db/id[:db.part/user -1]}
    {:recording/search_term "other show", :db/id #db/id[:db.part/user -3], :recording/skyid skyid, :customer/_recording #db/id[:db.part/user -1]}

  ]]


    (println "commit transaction for data add...")
    (not (nil? @(d/transact connection data-tx)))
  )

)




(defn run-query []
  (println "creating query...")

  (let [results (q '[:find  ?skyid ?rst
                     :where [?c :customer/skyid ?skyid ]
                           [?c :customer/recording ?r ]
                           [?r :recording/skyid ?skyid ]
                           [?r :recording/search_term ?rst ]

                     ] (db connection))
        id (ffirst results)
        rec-id (rest (first results))
        entity (d/touch (-> connection db (d/entity id)))
        ;rec-entity (d/touch (-> connection db (d/entity rec-id)))
        ]

    (println "query result count: " (count results))
    (println "result type: " (type results))
    (println "results: " results)
    (println "first results: " (first results))
    (println "rec-id: " rec-id)
    ;(println "rec-entity: " (d/touch (-> connection db (d/entity rec-id))))
    ;; display the entity map's keys
    (println "keys/entity: " (keys entity))
    ;(println "keys/rec-entity: " (keys rec-entity))

    ;; display the value of the entity's customer name
    (println ":customer/name: " (:customer/name entity))
    (println ":customer/recording: " (:customer/recording entity))
    entity
  )
)

