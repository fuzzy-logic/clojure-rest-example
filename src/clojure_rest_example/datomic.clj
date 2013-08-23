(ns clojure-rest-example.datomic
  (:use [clojure.test]))

(use '[datomic.api :only [q db] :as d])
(use 'clojure.pprint)


(def datomic-uri "datomic:free://localhost:4334//customer")

(def connection (d/connect datomic-uri)  )

(defn dbinit []
  (println "setting up datomic db & schema...")
  (d/create-database datomic-uri)
  (let [schema-file (read-string (slurp "data/datomic_schema.dtm"))
       transaction @(d/transact connection schema-file)]
    ;(println "read schema from file: " schema-file)
    ;(println "commit schema transaction results: " transaction)
  )
)


(defn uuid [] (str (java.util.UUID/randomUUID)))


(defn get-customer-id [skyid]
   (println "getting customer id for skyid " skyid)
  (ffirst (q '[:find  ?c :where [?c :customer/skyid skyid ] ] (db connection))  )
)




(defn get-customer [skyid]
    ;(println "customer keys/entity: " (keys customer))
    ;(println ":customer/name: " (:customer/name customer))
    ;(println ":customer/skyid: " (:customer/skyid customer))
    (d/touch (-> connection db (d/entity (get-customer-id skyid))))
 )





(defn add-data [data]
    (println "commit transaction for data add...")
    (println "data: " data)
    (not (nil?   @(d/transact connection data) ))

)


;(defn- create-rec-map [customerid recordings counter newrecs]
;   (println "create-rec-map[] counter: " counter ", recordings: " recordings ", newrecs: " newrecs)
;      (if (empty? recordings)
;        newrecs
;        (let [rec (first recordings)
;            rec-data {:recording/search_term (:search_term rec),
;                      :db/id #db/id[:db.part/user counter-val],
;                      :recording/skyid (:skyid rec),
;                      :customer/_recording #db/id[:db.part/user customerid]}
;             ]
;            (recur customerid (rest recordings) (dec counter) (cons rec-data newrecs))
;        )
;      )
;)


;(defn convert-rec-map [customerid recordings]
;       (create-rec-map customerid recordings -1 [])
;  )


(defn add-recording [rec]
     (add-data {:recording/search_term (:search_term rec),
                :db/id #db/id[:db.part/user -1],
                :recording/skyid (:sky-id rec),
                :customer/_recording (get-customer (:sky-id rec))} )
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

