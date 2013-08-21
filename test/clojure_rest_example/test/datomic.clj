(ns clojure-rest-example.test.datomic
  (:use clojure.test
        clojure-rest-example.datomic))

(deftest test-datomic
  (testing "add stuff"
    (println "running test...")
    (let [conn (dbinit) ]
      (is (not (= (add-data conn) nil)))
      (def entity (run-query conn))
      (is (= (keys entity)  '(:customer/recording :customer/name :customer/skyid) ))
    )
  )
)
