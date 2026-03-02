# syntax=docker/dockerfile:1

FROM maven:3.9.9-eclipse-temurin-25 AS build
WORKDIR /workspace

COPY pom.xml ./
COPY eclipse-java-formatter.xml ./
RUN mvn -B -q -DskipTests dependency:go-offline

COPY src ./src
RUN mvn -B -DskipTests package

FROM eclipse-temurin:25-jdk
WORKDIR /app

COPY --from=build /workspace/target/*.jar /app/app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
