# Product Prices Rule Engine #

This is an application to get product prices based on business rules.

### Prerequisites ###

This service uses Java8, maven, Spring. You must have JDK8 or higher installed.

Itt uses the visioLending rules services to load the rules. However the application
also uses initial set of rules provided by business team.

context path:
````
 /ruleEngine
````

port:

```
set to run on port 8091
```

### Building ###

The build uses maven. To build, run:

```
mvn clean install
```

### Running ###

This is a standard Spring Boot project. To run:

```
mvn spring-boot:run

```