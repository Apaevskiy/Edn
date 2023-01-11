FROM openjdk:8-jdk-alpine AS builder
MAINTAINER baeldung.com

ARG JAR_FILE=*.jar
COPY ${JAR_FILE} application.jar

ENTRYPOINT ["java", "-jar", "application.jar"]