FROM openjdk:17
WORKDIR /application
ADD passenger-service/target/passenger-service-0.0.1-SNAPSHOT.jar passenger-service-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "passenger-service-0.0.1-SNAPSHOT.jar"]
