(ns clojure-rest-example.test.datomic
  (:use clojure.test
        clojure-rest-example.datomic))

;(deftest test-datomic
;  (testing "add stuff"
;    (println "running test...")
;    (dbinit)
;    (def customer {:customer {:name "Billy Bob Thornton"} :recording {:search_term "Dexter"} })
;    (is (= (add-data customer) true))
;    (def entity (run-query))
;    (is (= (keys entity)  '(:customer/recording :customer/name :customer/skyid) ))
;    (is (= (:customer/name entity)  (:name (:customer customer)) ))
;    ;(is (= (:recording/search_term entity)  (:search_term (:recording customer)) ))
;  )
;)


(deftest test-convert-rec-map
  (testing "convert-rec-map")
  (def recordinglist [{:search_term "Dexter"} {:search_term "Game of Thrones"} {:search_term "Breaking Bad"}])
  (def result (convert-rec-map 123 recordinglist))
  (println "result metedata: " (meta result))
  (is (seq? result))
  (is (map? (first result)))
  (is (= (:recording/search_term (first result)) "Breaking Bad"))
  (is (= (:recording/search_term (nth result 1)) "Game of Thrones"))
   (is (= (:recording/search_term (nth result 2)) "Dexter"))
)
