(ns clojure-rest-example.test.datomic
  (:use clojure.test
        clojure-rest-example.datomic))

(deftest test-datomic
  (testing "add stuff"
    (println "running test...")
    (let [conn (dbinit) ]
      (is (= (add-data conn) true))
      (is (= (run-query conn) true))
    )
  )
)
