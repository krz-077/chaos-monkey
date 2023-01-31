FROM adoptopenjdk/openjdk11:slim

#
COPY ./target/chaos-monkey-1.0-SNAPSHOT.jar app.jar


EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]
