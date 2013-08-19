clojure-rest-example
====================


RUNNING WEBSERVER

lein ring server



RUNNING DATOMIC

cd datomic
bin/transactor config/samples/free-transactor-template.properties





RUNNING TESTS

Run tests with the function "(run-tests namespaces...)":

user=> (use 'clojure.test)
nil
user=> (run-tests)

(run-tests 'your.namespace 'some.other.namespace)

If you don't specify any namespaces, the current namespace is
used.  To run all tests in all namespaces, use "(run-all-tests)".

or lein test clojure-rest-example.test.testname





TEST REST API VIA CURL

to test web service externally:

./bin/curl-test
