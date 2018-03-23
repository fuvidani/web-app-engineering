# Web Application Engineering and Content Management 
[![Build Status](https://travis-ci.com/fuvidani/web-app-engineering.svg?token=nWakM5wh7rnyXAfUiELD&branch=master)](https://travis-ci.com/fuvidani/web-app-engineering)  [![codecov](https://codecov.io/gh/fuvidani/web-app-engineering/branch/master/graph/badge.svg?token=TjVLRsAmuK)](https://codecov.io/gh/fuvidani/web-app-engineering)  [![ktlint](https://img.shields.io/badge/code%20style-%E2%9D%A4-FF4081.svg)](https://ktlint.github.io/)

Web Application Engineering in 2018 with cutting-edge technologies.

## Structure
The project consists of 3 main components which are all containerized using Docker.

## Backend
Spring Boot 2 application featuring non-blocking reactive 
[WebFlux](https://docs.spring.io/spring/docs/5.0.0.BUILD-SNAPSHOT/spring-framework-reference/html/web-reactive.html#web-reactive) 
endpoints.

## Frontend
React web-application that communicates with the backend.

## Database
For this task, the database is also separated and put into a container. The idea behind this 
approach is to be able to easily scale the backend if needed. Scaling the database (data-replication,
eventual consistency) remains unconsidered for this small project.

We use MongoDB because it is fairly easy to work with plus it has a reactive behaviour support (in
contrast to JDBC for example) so that it integrates well with Spring Data and WebFlux. 

## Running locally through IDE (for development purposes)
In order to start the backend successfully, a local mongo db has to be started and configured appropriately.

The first step is to [install MongoDB](https://docs.mongodb.com/manual/administration/install-community/) on your preferred OS. 
After that, start the database:
 ```shell
 mongod
 ```
 In a separate terminal tab, start the mongo shell:
  ```shell
  mongo
  ```
In the shell, switch to our database:
  ```shell
  use waecmDatabase
  ```
This will create it if it doesn't exist yet and whenever you use ``db`` in the mongo shell this database
will be referenced. We need to put an initial counter into the database:
  ```shell
  db.counters.save({"_id": "counter", "value": 0})
  ```
The last thing we need to do is add authentication to this database. Just like in production, we want to secure
access to the database by protecting it with a username and password. In order to achieve this, we have to 
create a user with the appropriate role and its credentials:
  ```shell
  db.createUser({ user: 'user', pwd: 'devPassword', roles: [ {role:'readWrite', db:'waecmDatabase'} ]})
  ```
If this succeeded, simply exit the shell (``exit``) and stop the mongo instance. Then restart the mongo
instance, however this time with authorization enabled:
 ```shell
 mongod --auth
 ```
 This will simply turn on authorization.
 
After this, the Spring Boot app should be able to connect to your local mongo db through
`localhost:27017`. 

**Development main endpoint: http://localhost:8080**
 
## Running locally through Docker
Running the project using Docker saves you the trouble starting and setting up a local 
mongo db. 
Assuming you have working Docker on your machine, simply navigate in your console
to the project root folder (`web-app-engineering`) and pull the images from dockerhub: 
 ```shell
 docker-compose pull
 ```
In order to start the containers, use:
 ```shell
 docker-compose up -d
 ```

**Note**: If you want to execute `docker-compose build` so that the images get built locally
(instead of pulling them from registry), the jar file for the backend has to exist in `backend/build/libs/`.
For this, simply execute `gradlew build` (or `./gradlew build`) and the jar will be built.

For endpoints see next section.

## Endpoints

- **Frontend**: http://localhost:8069
- **Swagger-UI**: http://localhost:8888
- **Backend**: http://localhost:8182

### Docker Images

The following docker images are used:

- rasakul/waecm-2018-group-09-bsp-1-backend
    - based on "java:8-jre"
    - hash: sha256:0b70798631d9df7b3281beb96d283c28db93634d5854cd8e1e82b92d1d42facc
- rasakul/waecm-2018-group-09-bsp-1-mongo
    - based on "mongo:3"
    - hash: sha256:769a42386c7ed07c2d135265d179dcb2f5f5c9dc7a5c8dab6af0498239325cb8
- rasakul/waecm-2018-group-09-bsp-1-frontend
    - based on "node:9"
    - hash: sha256:f76a107eafd89892627b7b7cc490f2c8e2664a3b8259d04a1a847904df642b1c
- swaggerapi/swagger-ui
    - [Link](https://hub.docker.com/r/swaggerapi/swagger-ui/)
    - hash: 3a96c9da0b2fcb7a813821a0203f4a15cfebaad7d7549763d7840b865fcc9855    

## Authentication and Authorization
At this point, the backend exposes the four following endpoints:

- `POST http://localhost:8182/auth`
- `GET http://localhost:8182/counter` (gets counter)
- `POST http://localhost:8182/counter` (increments counter)
- `POST http://localhost:8182/reset` (resets the counter)

The last three endpoints are protected by a `Bearer Token` authorization using [JWT](https://jwt.io/).
This means that first the user has to authenticate themselves through the `/auth` endpoint. 
The endpoint expects a username and a password and returns a token if these are valid.
Example using `curl`:
 ```shell
 curl -H "Content-Type: application/json" -X POST -d '{"username":"user","password":"password"}' http://localhost:8182/auth
 ```
Upon successful authentication, the result might look like this:
 ```shell
 {"token":"eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyIiwicm9sZXMiOiJST0xFX1VTRVIiLCJleHAiOjE1MjE0ODExNTksImlhdCI6MTUyMTM5NDc1OX0.eDPMllIQoatJq657WEd6GMv-8I0UzsPY3CbRVVBJiOk"}
 ```
The obtained token has to be provided on each subsequent invocation using the [Bearer schema](https://tools.ietf.org/html/rfc6750),
like so:
 ```shell
 curl http://localhost:8182/counter -v -H "Authorization:Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyIiwicm9sZXMiOiJST0xFX1VTRVIiLCJleHAiOjE1MjE0ODExNTksImlhdCI6MTUyMTM5NDc1OX0.eDPMllIQoatJq657WEd6GMv-8I0UzsPY3CbRVVBJiOk"
 ```
Should you forget to provide a valid token, the endpoints will return `401 Unauthorized`.

**Important: In case of the non docker-based deployment, the API uses the port 8080 instead of 8182!!**

## Troubleshooting
Contact one of the contributors or open an issue.

## License
This project is licensed under the [MIT License](https://opensource.org/licenses/MIT). Feel free to
use, extend or fit it to your needs.

