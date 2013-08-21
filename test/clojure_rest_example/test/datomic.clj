(ns clojure-rest-example.test.datomic
  (:use clojure.test
        clojure-rest-example.datomic))

(deftest test-datomic
  (testing "add stuff"
    (println "running test...")
    (dbinit)
    (is (not (= (add-data) nil)))
    (def entity (run-query))
    (is (= (keys entity)  '(:customer/recording :customer/name :customer/skyid) ))
    (is (= (:customer/name entity)  "Billy Bob Thornton" ))
  )
)
