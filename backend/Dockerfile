FROM java:8-jre

ADD ./build/libs/backend-1.0-SNAPSHOT.jar app.jar

COPY entrypoint.sh /entrypoint.sh

RUN \
 chmod 755 /entrypoint.sh && \
 apt-get update && apt-get dist-upgrade -y && \
 apt-get install -y dos2unix && \
 apt-get install psmisc -y -q && \
 apt-get autoremove -y && apt-get clean && \
 dos2unix /entrypoint.sh && apt-get --purge remove -y dos2unix && \
 rm -rf /var/cache/* && rm -rf /var/lib/apt/lists/*

ENTRYPOINT ["/entrypoint.sh","java","-Dspring.data.mongodb.host=waecm-mongo-db" , "-Djava.security.egd=file:/dev/./urandom", "-jar", "/app.jar"]
EXPOSE 8443