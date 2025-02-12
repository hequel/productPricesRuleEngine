# Product Prices Rule Engine #

This is an application to get product prices based on business rules using the rule engine design pattern.

### Prerequisites ###

This service uses Java8, maven, Spring. You must have JDK8 or higher installed.

context path:
````
 /ruleEngine
````

port:

```
set to run on port 8091
```

api to request product price: /ruleEngine/api/v1/prices
````
sample request body: 

{
  "credit_score":"700",
  "state":"california",
  "productName":"homeMorgage"
}

sample request response:

{
"interest_rate": 5.5,
"disqualified": false,
"match": true
}
````

### Running test ###

To run test, run:

```
mvn clean test
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
