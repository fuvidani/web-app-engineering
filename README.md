# Web Application Engineering and Content Management 
[![Build Status](https://travis-ci.com/fuvidani/web-app-engineering.svg?token=nWakM5wh7rnyXAfUiELD&branch=master)](https://travis-ci.com/fuvidani/web-app-engineering)
[![ktlint](https://img.shields.io/badge/code%20style-%E2%9D%A4-FF4081.svg)](https://ktlint.github.io/)

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

## Running locally through IDE
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

To manually test the application, hit the following endpoints using `curl` or Postman:

- `GET http://localhost:8080/counter` (gets counter)
- `POST http://localhost:8080/counter` (increments counter)
- `POST http://localhost:8080/reset` (resets the counter)

**Important**: Don't forget to enable Basic Auth with the credentials from `application.yml` otherwise the
REST-calls will return `401 Unauthorized`.
 
## Running locally through Docker
Running the project using Docker saves you the trouble starting and setting up a local 
mongo db. Assuming you have working Docker on your machine, simply navigate in your console
to the project root folder (`web-app-engineering`) and start the docker-compose yaml file:
 ```shell
 docker-compose up
 ```
Note: Presumably your docker will have to download bunch of images, build the images and deploy. 
It may last 1-2 minutes until everything's up and running. Stare at the console and you'll see what's happening.

To manually test the application, hit the following endpoints using `curl` or Postman (**Mind the changed port**):

- `GET http://localhost:8182/counter` (gets counter)
- `POST http://localhost:8182/counter` (increments counter)
- `POST http://localhost:8182/reset` (resets the counter)

**Important**: Don't forget to enable Basic Auth with the credentials from `application.yml` otherwise the
REST-calls will return `401 Unauthorized`.

## Troubleshooting
Contact one of the contributors or open an issue.

## License
This project is licensed under the [MIT License](https://opensource.org/licenses/MIT). Feel free to
use, extend or fit it to your needs.

