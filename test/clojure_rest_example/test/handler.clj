(ns clojure-rest-example.test.handler
  (:use clojure.test
        ring.mock.request
        clojure-rest-example.handler))

(deftest test-app

  ;(testing "main route"
  ;  (let [response (app (request :get "/"))]
  ;    (is (= (:status response) 200))
  ;    (is (= (:body response) "Hello World"))))

  ;(testing "not-found route"
   ; (let [response (app (request :get "/invalid"))]
    ;  (is (= (:status response) 404)))))

 ;(testing "all customers"
 ;   (let [response (app (request :get "/documents"))]
 ;     (println "response: " response)
 ;     (is (= (:status response) 200))
 ;     (is (not (nil? (:body response) )))
 ;   )
 ; )

 ;  (testing "get one customer"
 ;   (let [response (app (request :get "/documents/4140a31a-d638-44aa-89b2-503500b9dbf9"))]
 ;     (println "response: " response)
 ;     (is (= (:status response) 200))
 ;     (is (not (nil? (:body response) )))
 ;   )
 ; )


    (testing "create new recording"
    (let [response (app (request :post "/customer/4140a31a-d638-44aa-89b2-503500b9dbf9/recording" :body "{\"search_term\": \"Malcolm in the Middle\", \"skyid\": \"4140a31a-d638-44aa-89b2-503500b9dbf9\"}"))]
      (println "response: " response)
      (is (= (:status response) 200))
      (is (not (nil? (:body response) )))
    )
  )
)
