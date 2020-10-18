# component-tests-docker-mvn-verify

The goal of this pet project was originally to try to setup a spring-boot project with a component test launched after 
build using maven verify.

Since it has outgrown its original purpose and now is a support for experimenting microservices testing and other 
microservices related things. 

# Start

`mvn clean verify` will package the application, building the dokcer images of the 2 services : "service-a" and "service-b", 
and execute a blackbox component-level test of "service-a".

# service-b testkit

There is also another microservice, `service-b`, which can be started with the profile `test`, that allows any other 
service to perform integration test with `service-b` without any infrastructure dependencies for `service-b` : databases, 
message queue, etc.

It is basically a component-level test double that uses h2 instead of mysql as a datastore, and expose rest endpoints for 
creating test data for any upstream service under integration test against `service-b`.