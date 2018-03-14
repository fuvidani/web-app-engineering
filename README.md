# Web Application Engineering and Content Management
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

## License
This project is licensed under the [MIT License](https://opensource.org/licenses/MIT). Feel free to
use, extend or fit to your needs.

