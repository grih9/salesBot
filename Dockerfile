FROM maven:3.8-jdk-11 AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -B -f /home/app/pom.xml clean package -DskipTests

FROM openjdk:11.0.4-jre-slim
COPY --from=build /home/app /usr/local/lib
EXPOSE 8080
ENV BOT_NAME=$(secrets.BOT_NAME)
ENV BOT_TOKEN=$(secrets.BOT_TOKEN)
ENTRYPOINT ["java","-jar","/usr/local/lib/target/salesBot-jar-with-dependencies.jar"]
