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



(defn find-by
  "Returns the unique entity identified by attr and val."
  [attr val]
  (q '[:find ?e
        :in $ ?attr ?val
        :where [?e ?attr ?val]]
      (d/db connection) attr val))



(defn get-customer-id [skyid]
   (println "getting customer id for skyid " skyid)
  (let [id (ffirst (find-by :customer/skyid skyid ))]
    (do (println "get-customer-id returning: " id)
      id)
    )
)



(defn get-entities [idlist]
  (println "get-customers [] idlist: " idlist)
    (if (empty? idlist)
      nil
      (let [recording (d/touch (-> connection db (d/entity (first idlist))))
          results  (cons recording (get-entities (rest idlist)))
          ]
        (do (println "get-recordings retuning results: " results)
          results
        )
      )
    )

)

(defn get-all-customers
  "retrieve all customers from datomic"
  []
  (let [idlist (q '[:find ?c :where [?c :customer/skyid ?skyid] ] (d/db connection))
    results (get-entities (map first idlist))]

    (do (println "get-all-customers [] result count: " (count results))
      results
      )
    )

)

(defn get-customer [skyid]
    (println "get-customer[] id=" skyid)
    ;(println ":customer/name: " (:customer/name customer))
    ;(println ":customer/skyid: " (:customer/skyid customer))
    (let [customer (d/touch (-> connection db (d/entity (get-customer-id skyid))))]
      (do (println "get-customer returning: " customer))
      customer
    )

 )




(defn add-data [data]
    (println "add-data[] data: " data)
    ;(not (nil?   @(d/transact connection data) ))
    (let [result (d/transact connection data)]
      (do (println "add-data result: " result))
      result
      )

)


(defn add-recording [rec]
     (add-data [{:recording/search_term (:search_term rec),
                :db/id #db/id[:db.part/user -1],
                :recording/skyid (:skyid rec),
                :customer/_recording (get-customer-id (:skyid rec))}] )
  )




(defn get-entities "return entities for given ids" [idlist]
  ;(println "get-entities [] idlist: " idlist)
    (if (empty? idlist)
      nil
      (let [id (first idlist)
            entity (d/touch (-> connection db (d/entity id)))
          results  (cons entity (get-entities (rest idlist)))  ]
       results
      )
    )
)


(defn get-customer-recordings
  "returns recordings for a given customer's skyid"
  [skyid]
  (println "creating query...")

  (let [recidlist (q '[:find  ?r
                     :in $ ?skyid
                     :where [?r :recording/skyid ?skyid ]
                            [?r :recording/search_term ?rst ]
                    ]
                    (db connection) skyid)
        ]
    (println "get-customer-recordings[] recidlist: " recidlist)
     (get-entities (map first recidlist))
  )
)

