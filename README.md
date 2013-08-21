clojure-rest-example
====================


RUNNING WEBSERVER

lein ring server



RUNNING DATOMIC FREE IN MEMORY EDITION
download and unpack datomic http://downloads.datomic.com/free.html
cd datomic
bin/transactor config/samples/free-transactor-template.properties



RUNNING TESTS

You *can* run tests with the function "(run-tests namespaces...)" but it doesn't work for me.

user=> (use 'clojure.test)
nil
user=> (run-tests)

user=> (run-tests 'your.namespace 'some.other.namespace)

If you don't specify any namespaces, the current namespace is
used.  To run all tests in all namespaces, use "(run-all-tests)".

This works for me from the command line:

lein test clojure-rest-example.test.testname



TEST REST API VIA CURL

to test web service externally:

./bin/curl-test
