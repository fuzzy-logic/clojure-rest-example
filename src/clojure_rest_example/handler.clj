(ns clojure-rest-example.handler
      (:import com.mchange.v2.c3p0.ComboPooledDataSource)
      (:use compojure.core)
      (:use cheshire.core)
      (:use clojure-rest-example.jdbc)
      (:use ring.util.response)
      (:require [compojure.handler :as handler]
                [ring.middleware.json :as middleware]
                [clojure.java.jdbc :as sql]
                [compojure.route :as route]
                [clojure-rest-example.datomic :as d]))


    (defn get-all-documents []
      (response (d/get-all-customers) )
    )

    (defn get-document [id]
      (let
          [results (d/get-customer id)]
          (if  (empty? results)
            {:status 404}
            (response (first results) )
           )
         ))

    (defn create-new-document [doc]
      (let [id (uuid)]
        (sql/with-connection (db-connection)
          (let [document (assoc doc "id" id)]
            (sql/insert-record :documents document)))
        (get-document id)))

    (defn update-document [id doc]
        (sql/with-connection (db-connection)
          (let [document (assoc doc "id" id)]
            (sql/update-values :documents ["id=?" id] document)))
        (get-document id))

    (defn delete-document [id]
      (sql/with-connection (db-connection)
        (sql/delete-rows :documents ["id=?" id]))
      {:status 204})

    (defroutes app-routes
      (context "/documents" [] (defroutes documents-routes
        (GET  "/" [] (get-all-documents))
        (POST "/" {body :body} (create-new-document body))
        (context "/:id" [id] (defroutes document-routes
          (GET    "/" [] (get-document id))
          (PUT    "/" {body :body} (update-document id body))
          (DELETE "/" [] (delete-document id))))))
      (route/not-found "Not Found"))

    (def app
        (-> (handler/api app-routes)
            (middleware/wrap-json-body)
            (middleware/wrap-json-response)))
