(ns clojure-rest-example.datomic
  (:use [clojure.test]))

(use '[datomic.api :only [q db] :as d])
(use 'clojure.pprint)


(def datomic-uri "datomic:free://localhost:4334//customer")

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


(defn uuid [] (str (java.util.UUID/randomUUID)))



(defn- create-rec-map [customerid recordings counter newrecs]
   (println "create-rec-map[] counter: " counter ", recordings: " recordings ", newrecs: " newrecs)
      (if (empty? recordings)
        newrecs
        (let [rec (first recordings)
            rec-data {:recording/search_term (:search_term rec),
                      :db/id #db/id[:db.part/user counter],
                      :recording/skyid (:skyid rec),
                      :customer/_recording #db/id[:db.part/user customerid]}
             ]
            (recur customerid (rest recordings) (dec counter) (cons rec-data newrecs))
        )
      )

)


(defn convert-rec-map [customerid recordings]
       (create-rec-map customerid recordings -1 [])
  )






(defn add-data [data]
  (println "adding data: " data)
  ;; add some data:
  ;; -1 used as tempid for :customer/
  ;; -1 customer id then re-used for linking :recording/ sub-entities (a bit like a foreign key)
  ;; :recording/ bi-directional realtionship maintained via traversal using ":customer/_recording" reference attribute and customer -1 id
  (let [skyid (uuid)
        data-tx [
    {:customer/skyid skyid, :customer/name (:name (:customer data)), :db/id #db/id[:db.part/user -1] }
    {:recording/search_term (:search_term (:recording data)), :db/id #db/id[:db.part/user -2], :recording/skyid skyid, :customer/_recording #db/id[:db.part/user -1]}


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

