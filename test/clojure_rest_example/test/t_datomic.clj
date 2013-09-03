(ns clojure-rest-example.test.t_datomic
  (:use clojure.test
        clojure-rest-example.datomic))

(def cust1-skyid (uuid))
(def cust2-skyid (uuid))
(def cust3-skyid (uuid))

(defn lazy-contains? [collection key]
  (some #{key} collection)
)

(def test-customer-data
    ;; add some data:
  ;; -1 used as tempid for :customer/
  ;; -1 customer id then re-used for linking :recording/ sub-entities (a bit like a foreign key)
  ;; :recording/ bi-directional realtionship maintained via traversal using ":customer/_recording" reference attribute and customer -1 id
  [  {:customer/skyid cust1-skyid, :customer/name "Bob Right", :db/id #db/id[:db.part/user -1] }
    {:recording/search_term "Breaking Bad", :db/id #db/id[:db.part/user -11], :recording/skyid cust1-skyid, :customer/_recording #db/id[:db.part/user -1]}
    {:recording/search_term "Game of Thrones", :db/id #db/id[:db.part/user -12], :recording/skyid cust1-skyid, :customer/_recording #db/id[:db.part/user -1]}

    {:customer/skyid cust2-skyid, :customer/name "Jane Ayre", :db/id #db/id[:db.part/user -2] }
    {:recording/search_term "Playhouse presents", :db/id #db/id[:db.part/user -21], :recording/skyid cust2-skyid, :customer/_recording #db/id[:db.part/user -2]}
    {:recording/search_term "Downton Abbey", :db/id #db/id[:db.part/user -22], :recording/skyid cust2-skyid, :customer/_recording #db/id[:db.part/user -2]}

    {:customer/skyid cust3-skyid, :customer/name "Ashley Booth", :db/id #db/id[:db.part/user -3] }
    {:recording/search_term "Top Gear", :db/id #db/id[:db.part/user -31], :recording/skyid cust3-skyid, :customer/_recording #db/id[:db.part/user -3]}
    {:recording/search_term "QI", :db/id #db/id[:db.part/user -32], :recording/skyid cust3-skyid, :customer/_recording #db/id[:db.part/user -3]}   ]


  )


(dbinit)
(add-data test-customer-data)




(deftest test-get-customer-id
  (testing "testing get-customer-id"
    (println "running test-get-customer-id...")
    (let [custid (get-customer-id cust1-skyid)]
      (is (number? custid))
    )
  )
)



(deftest test-get-all-customers
  (testing "testing test-get-all-customers"
    (let [allcustomers (get-all-customers)]
      (println "customers: " allcustomers)
      (is (not (nil? allcustomers)))
      (is (seq allcustomers))
      (is (lazy-contains? (map :customer/name allcustomers) "Jane Ayre"))
    )
  )
)



(deftest test-add-recording
  (testing "add new recording entity"
    (let [cust1-newrec {:skyid cust1-skyid :search_term "Eastenders"}
          newrec-response (add-recording cust1-newrec)]
      (println "test: cust1-newrec: " cust1-newrec)
      (println "test: newrec-response: " newrec-response)
      (is (not (nil? newrec-response)))
      (is (not (nil? (:tx-data newrec-response))))
      (is (lazy-contains? (map :recording/search_term (get-customer-recordings cust1-skyid)) "Eastenders" ))
    )
  )
)


(deftest test-delete-recoding
  (testing "delete recoding by id"
    (let [newrec (add-recording {:skyid cust1-skyid :search_term "Eastenders"})
          rec-id (first (vals (:tempids newrec)))
          rec (first (get-entities [rec-id]))]
      (println "test: added recording newrec: " newrec)
      (println "test: rec-id: " rec-id)
      (println "test: rec map: " rec)
      (is (> (count rec) 1))
      (is (delete-recording rec-id))
      (is (= (count (get-entities [rec-id])) 1))
      )
    )
)









