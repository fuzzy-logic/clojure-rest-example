(ns clojure-rest-example.test.datomic
  (:use clojure.test
        clojure-rest-example.datomic))

(def cust1-skyid 123)
(def cust2-skyid 456)
(def cust3-skyid 789)



(defn test-customer-data[]
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

(deftest test-add-recording
  (testing "add new recording"
    (println "running test...")


    (def cust1-newrec {:skyid cust1-skyid :search_term "Eastenders"})
    (is (= (add-data customer) true))
  )
)


;(deftest test-convert-rec-map
;  (testing "convert-rec-map")
;  (def recordinglist [{:search_term "Dexter"} {:search_term "Game of Thrones"} {:search_term "Breaking Bad"}])
;  (def result (convert-rec-map 123 recordinglist))
;  (println "result metedata: " (meta result))
;  (is (seq? result))
;  (is (map? (first result)))
;  (is (= (:recording/search_term (first result)) "Breaking Bad"))
;  (is (= (:recording/search_term (nth result 1)) "Game of Thrones"))
;   (is (= (:recording/search_term (nth result 2)) "Dexter"))
;)


