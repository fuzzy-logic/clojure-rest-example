clojure-rest-example
====================


to run:

lein ring server



to test web service externally:

./bin/curl-test





RUNNING TESTS

Run tests with the function "(run-tests namespaces...)":

(run-tests 'your.namespace 'some.other.namespace)

If you don't specify any namespaces, the current namespace is
used.  To run all tests in all namespaces, use "(run-all-tests)".
