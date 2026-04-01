# Java 21 JDK use garnu
FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app
COPY target/job-sathi-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 9001
ENTRYPOINT ["java","-jar","app.jar"]
