FROM maven:4.0.0-jdk-11 AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -B -f /home/app/pom.xml clean package -DskipTests

FROM openjdk:11.0.4-jre-slim
COPY --from=build /home/app /usr/local/lib
EXPOSE 8080
ENTRYPOINT ["java","-jar","/usr/local/lib/target/salesBot.jar"]
