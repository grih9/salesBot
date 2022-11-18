# syntax=docker/dockerfile:1

FROM eclipse-temurin:17-jdk-jammy

WORKDIR /app

COPY pom.xml ./

COPY src ./src
ENTRYPOINT /APP
