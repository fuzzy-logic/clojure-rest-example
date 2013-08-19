(ns clojure-rest-example.test.datomic
  (:use clojure.test
        clojure-rest-example.datomic))

(deftest test-datomic
  (testing "add stuff"
    (let [person (+ 1 1)]
      (is (= person 2))
    )
  )
)
