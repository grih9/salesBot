FROM maven:3.8-jdk-11 AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -B -f /home/app/pom.xml clean package -DskipTests

FROM openjdk:11.0.4-jre-slim
COPY --from=build /home/app /usr/local/lib
EXPOSE 8080
ENV BOT_TOKEN="2104765245:AAHzvhwnfBZ_Na6t6TCaqxB_NYMNzJFWGQQ"
ENV BOT_NAME="ShoppingSalesBot"
ENTRYPOINT ["java", "-jar", "/usr/local/lib/target/salesBot-jar-with-dependencies.jar"]
