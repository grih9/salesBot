FROM maven:3.6.0-jdk-11-slim AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -B -f /home/app/pom.xml clean package -DskipTests

FROM openjdk:11.0.4-jre-slim
RUN cd /home/app/target
RUN ls
COPY --from=build /home/app/target/salesBot-1.0-SNAPSHOT.jar /usr/local/lib/salesBot.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/usr/local/lib/salesBot.jar"]