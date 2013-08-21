(ns clojure-rest-example.test.datomic
  (:use clojure.test
        clojure-rest-example.datomic))

(deftest test-datomic
  (testing "add stuff"
    (println "running test...")
    (dbinit)
    (def customer {:customer {:name "Billy Bob Thornton"} :recording {:search_term "Dexter"}})
    (is (= (add-data customer) true))
    (def entity (run-query))
    (is (= (keys entity)  '(:customer/recording :customer/name :customer/skyid) ))
    (is (= (:customer/name entity)  (:name (:customer customer)) ))
    ;(is (= (:recording/search_term entity)  (:search_term (:recording customer)) ))
  )
)
