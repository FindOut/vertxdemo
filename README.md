# VertxDemo

* From the root run: `mvn clean install` . This makes sure you push everything to your local maven repos.

* Use IntelliJ...

* Run Java verticles simply by using `Run 'xxx'` in IntelliJ

* Run JS verticles in IntelliJ by creating a Run task:
    - Run -> Edit Configurations...
    - Add a new Application configuration
    - Main Class: ```io.vertx.core.Launcher```
    - Program arguments (using the server.js as an example): ```run ./frontend-js/src/main/resources/server.js -cluster```
    
* Running Ruby verticles in Intellij requires installation and setup in project of JRuby
    
## Easiest running of examples
* Run: `mvn package`
* Run the *-fat.jar for each respective module, for example: 
`java -jar target/news-server-rb-1.0-SNAPSHOT-fat.jar -cp target/classes -cluster`  

## Docker

* Install Docker using the instructions for your OS found here <https://docs.docker.com/v1.8/installation/>
* Running the Docker container locally might compromise your network interfaces and making the normal `java -jar xxx -cluster`
usage not work. Just a heads up. Running everything inside of Docker should work fine (the issue is related to port bindings and 
Vert.x usage of Hazelcast for setting up discovery etc).


### Docker Mysql
  The project contains a `Dockerfile` to run a custom MySql container. Please see documentation here [README.md](/database/README.md)


### Docker Maven
  * `mvn clean package` in project root or any module, or `mvn docker:build` in any module (still needs `mvn package` to have been run). 
  * For each module you want to deploy in Docker run: `mvn docker:start`, or just run it in the root to start everything at once.
  * To stop, simply do `mvn docker:stop` in the same module or the root.
  * Alternatively you can use the Docker command line and start with:
    * `docker run -t -i -p 8080:8080 vertxdemo/frontend-js:1.0-SNAPSHOT`
    * `docker run -t -i vertxdemo/news-server-java:1.0-SNAPSHOT`
  * Check your Docker container for it's ip and access frontend at that address i.e. <http://xxx.xxx.xxx.xxx:8080>.  

#### db-service-java
  This module demonstrates both proxying of remote services, linking containers and using external configuration of environment variables.
  It should be run using either `mvn docker:start` or the command line equivalent: 
  `docker run -t -i --link vertxdemo-mysql:db --env-file ./dev.env.properties vertxdemo/db-service-java:1.0-SNAPSHOT`
  
  
### Docker Compose
  * A simple docker-compose setup exists in the project root. While standing in root, calling `docker-compose up` will start
  all modules, using the `Dockerfile` files as settings.
  
