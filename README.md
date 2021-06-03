Elmenus Coding Challenge Description
============================

-  [Description](#ElmenusCodingChallenge-Description)
-  [Building the application](#ElmenusCodingChallenge-BuildingApplication)
-  [Running the application](#ElmenusCodingChallenge-RunningApplication)
-  [REST specification](#ElmenusCodingChallenge-RestSpecification)	
-  [Features of the application](#ElmenusCodingChallenge-Features)	
		
Description
-----------

This application implements the order basket checkout process coding challenge.
The application supports basic CRUD operations for users, items and baskets.
Further, doing a checkout for a basket and check its contents before the actual checkout operation.

Building the application
--------------------
To build the application via maven, you can use the following command:

    mvn clean install
    
This will build the application and run unit and integration tests.

Running the application
--------------------
To run the application via maven, you can use the following command:

    mvn spring-boot:run
    
The default port of this service is **8090**.
To run it on a different port:

    mvn spring-boot:run -Dspring-boot.run.arguments=--server.port=8091
    
This will make the service running on port **8091**.

Once the application stars it will be available at [http://localhost:8090/](http://localhost:8090/). 

REST specification
-------------------------

### Users

|   **REST API**                                                    | **Description**                               |
|-------------------------------------------------------------------|-----------------------------------------------|
|     **GET http://localhost:8090/users**                           |       Get all users                           |
|     **GET http://localhost:8090/users/{id}**                      |       Get user by ID                          |
|     **POST http://localhost:8090/users**                          |       Add a new user                          |
|     **PUT http://localhost:8090/users/{id}**                      |       Update user by ID                       |
|     **DELETE http://localhost:8090/users/{id}**                   |       Delete user by ID                       |

### Items

|   **REST API**                                                   | **Description**                                 |
|------------------------------------------------------------------|-------------------------------------------------|
|     **GET http://localhost:8090/items**                          |       Get all items                             |
|     **GET http://localhost:8090/items/{id}**                     |       Get item by ID                            |
|     **POST http://localhost:8090/items**                         |       Add a new item                            |
|     **PUT http://localhost:8090/items/{id}**                     |       Update item by ID                         |
|     **DELETE http://localhost:8090/items/{id}**                  |       Delete item by ID                         |

### Baskets

|   **REST API**                                                   | **Description**                                 |
|------------------------------------------------------------------|-------------------------------------------------|
|     **GET http://localhost:8090/baskets**                        |       Get all baskets                           |
|     **GET http://localhost:8090/baskets/{id}**                   |       Get basket by ID                          |
|     **POST http://localhost:8090/baskets**                       |       Add a new basket                          |
|     **PUT http://localhost:8090/baskets/{id}**                   |       Update basket by ID                       |
|     **DELETE http://localhost:8090/baskets/{id}**                |       Delete basket by ID                       |
|     **POST http://localhost:8090/baskets/checkout/{id}**         |       Checks out a basket by ID                 |

### Basket contents

|   **REST API**                                                   | **Description**                                 |
|------------------------------------------------------------------|-------------------------------------------------|
|     **GET http://localhost:8090/basket-contents**                |       Get all basket contents                   |
|     **GET http://localhost:8090/basket-contents/{id}**           |       Get basket content by ID                  |
|     **POST http://localhost:8090/basket-contents**               |       Add a new basket content                  |
|     **PUT http://localhost:8090/basket-contents/{id}**           |       Update basket content by ID               |
|     **DELETE http://localhost:8090/basket-contents/{id}**        |       Delete basket content by ID               |

Features of the application
-------------------------
1. The application is fully asynchronous via Spring WebFlux.
2. It leverages H2 in-memory database for holding users, items and baskets.
3. It provides swagger definition for the REST api which can be found at [http://localhost:8090/swagger-ui.html](http://localhost:8090/swagger-ui.html). 
4. It uses Data Transfer Object (DTO) design pattern for sending data.
5. Unit tests and integration tests are developed with high test coverage.  