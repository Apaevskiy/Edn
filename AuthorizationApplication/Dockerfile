FROM openjdk:8-jdk-alpine
COPY target/AuthorizationApplication-1.0.jar authorizationApplication.jar
EXPOSE 9090
ENTRYPOINT ["java", "-jar", "authorizationApplication.jar"]