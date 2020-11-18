:fr: Sommaire / :gb: Table of Contents
=================

<!--ts-->
   * [Documentation en français](#fr-description-du-projet)
   * [English Documentation (coming soon)](#gb-project-description-coming-soon )

---

# :fr: Description du projet

Le but de ce projet était originellement uniquement de mettre en place des tests niveau composant, dockerisés sur un 
projet SpringBoot très simple, type CRUD.

Il a un peu débordé son but original et aura servis à poc-er différents "patterns" de test, et autres librairies
(`spring-data-rest` par exemple)

# Utilisation

## Lancement et utilisation de l'application

### Lancement de l'application

Lancer kafka et mysql: `docker-compose -f serviceA/src/test/resources/docker-compose-development-test.yml down && docker-compose -f serviceA/src/test/resources/docker-compose-development-test.yml up`
Lancer le composant
Lancer l'application: 

- `cd serviceA; mvn spring-boot:run;`
- via l'IDE (classe `ServiceA`, du module maven u même nom)

### Ajouter un contact

`curl -X POST -H "Content-type: application/json" -d '{"name":"joseph","email":"toto@yopmail.com","phoneNumber":"0836656565"}' localhost:8080/contacts`

### Lister les contacts

`curl localhost:8080/contacts`

### Mettre à jour un contact

`curl -X PUT -H "Content-type: application/json" -d '{"name":"joseph","email":"toto@yopmail.com","phoneNumber":"newPhone"}' localhost:8080/contacts/1`

### Supprimer un contact

`curl -X DELETE localhost:8080/contacts/1`

## Lancement des tests

Préalable: builder les images docker des composants `service-b` et `kafka-listener`: `mvn package`

Les tests peuvent être lancés:

- de manière "auto-portante"
    - via `mvn verify`
    - via `mvn test -Dtest=CucumberRunnerIT -DfailIfNoTests=false`
    - par l'IDE
        - glue: `com.example.demo.cucumber.steps com.example.demo.cucumber.component`
        - feature: `${project.basedir}/serviceA/src/test/resources/features/component-test.feature`
- avec kafka et mysql lancés séparément - un fichier docker-compose est fourni à cet effet: `docker-compose-development-test.yml`
    - pour lancer mysql et kafka : `docker-compose -f serviceA/src/test/resources/docker-compose-development-test.yml down && docker-compose -f serviceA/src/test/resources/docker-compose-development-test.yml up`
    - via `mvn test -Dtest=CucumberRunnerITDevelopment -DfailIfNoTests=false` (car certains modules maven n'ont pas de tests)
    - par l'IDE
        - glue: `com.example.demo.cucumber.steps com.example.demo.cucumber.development`
        - feature: `${project.basedir}/serviceA/src/test/resources/features/component-test.feature`
- avec toutes les dépendances externes mockées (`mysql`,`kafka`,`service-b`) - ne nécessite aucun composant externe lancé
    - via `mvn test -Dtest=CucumberRunnerITUnittest -DfailIfNoTests=false`
    - par l'IDE
        - glue: `com.example.demo.cucumber.steps com.example.demo.cucumber.unittest`
        - feature: `${project.basedir}/serviceA/src/test/resources/features/component-test.feature`

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

# kafkacat

```
kafkacat -b localhost:9094 -t test -C   -f '\nKey (%K bytes): %k
  Value (%S bytes): %s
  Timestamp: %T
  Partition: %p
  Offset: %o
  Headers: %h\n'
```

# :gb: Project Description (Coming soon)