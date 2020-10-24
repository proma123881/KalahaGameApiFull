# Kalaha
Kalaha is a well known turn based game. Detailed rules of Kalaha [here](https://en.wikipedia.org/wiki/Kalah)

# Technology used

* JAVA8
* Spring Boot 2.2.6
* Maven build plugin
* Intellij

# API Request and Expected Response

1. Creating a new game:
    * Request:
      * ```curl --header "Content-Type: application/json" --request POST http://<host>:<port>/games```
    
    * Response: 
      * HTTP code: 201
      * Body: ```{ "id": "1234", "uri": "http://<host>:<port>/games/1234" }```
      
      id: unique identifier of a game
      
      url: link to the game created

2. Make a move:
    * Request:
        * ```curl --header "Content-Type: application/json" --request PUT http://<host>:<port>/games/{gameId}/pits/{pitId}```
    * Success Response:
        * HTTP code: 200
        * Body: ```{"id":"1234","url":"http://<host>:<port>/games/1234","status":{"1":"4","2":"4","3":"4","4":"4","5":"4","6":"4","7":"0","8":"4","9":"4","10":"4","11":"4","12":"4","13":"4","14":"0"}}```
        * gameId: unique identifier of a game
        * pitId: id of the pit selected to make a move. Pits are numbered from 1 to 14 where 7 and 14 are the kalah (or house)of each player
    
    * Failure Response:
        * HTTP code: 4XX (XX depending on the error)
        * Body: ```{'errorCode': '<error code>',"timestamp": "<time stamp>","message": "<error message>", "requestUri": "uri=<url requested>"}```

# Exploring the API
   * You can explore the api from swagger [here](http://localhost:8080/swagger-ui.html) 
      
# How to run ?
   * Steps:
     * Download the code in an IDE
     * ``` mvn clean install ``` 
     * Run KalahaGameApplication.java
     * Use postman or curl (or any other client) to access to apis 
     * Default host is localhost and default port is 8080
     * Enjoy playing Kalaha :)
      

# For Feature requests or to report Bugs
      Proma Chowdhury
      promachowdhury5@gmail.com
      Java API Developer
    
