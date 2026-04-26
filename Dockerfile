# Example using official Eclipse Temurin
FROM eclipse-temurin:21-jdk-alpine
ENV JAVA_HOME=/opt/java/openjdk
# JAVA_VERSION and PATH are usually pre-configured by the official image
CMD ["java", "-jar", "app.jar"]   
