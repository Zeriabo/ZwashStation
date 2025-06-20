# Dockerfile
FROM maven:3.9.9-eclipse-temurin-17 AS builder
WORKDIR /app
COPY . .
# RUN mvn clean package -DskipTests

FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=builder /app/target/*.jar ZwashStation.jar
ENTRYPOINT ["java","-jar","/ZwashStation.jar"]

