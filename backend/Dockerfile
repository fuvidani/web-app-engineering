FROM java:8-jre

ADD ./build/libs/backend-1.0-SNAPSHOT.jar app.jar
CMD ["java","-Dspring.data.mongodb.host=waecm-mongo-db" , "-Djava.security.egd=file:/dev/./urandom", "-jar", "/app.jar"]

EXPOSE 8443