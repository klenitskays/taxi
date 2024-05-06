FROM maven:3.9.6 AS build
WORKDIR ride-service
COPY  driver-service-0.0.1-SNAPSHOT.jar driver-service-0.0.1-SNAPSHOT.jar 
COPY  mvnw mwnw 
COPY  mvnw.cmd mvnw  
COPY  passenger-service-0.0.1-SNAPSHOT.jar passenger-service-0.0.1-SNAPSHOT.jar   
COPY  payment-service-0.0.1-SNAPSHOT.jar payment-service-0.0.1-SNAPSHOT.jar  
COPY  pom.xml pom.xml 
COPY  src src

RUN mvn clean package
RUN mvn clean install 



FROM openjdk:17
COPY --from=build ride-service/target/ride-service-0.0.1-SNAPSHOT.jar ride-service.jar
ENTRYPOINT ["java", "-jar", "ride-service.jar"]



