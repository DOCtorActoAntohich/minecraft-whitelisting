FROM maven:3.8.3-openjdk-17-slim AS build
COPY . /build
WORKDIR /build
RUN mvn clean package install -DskipTests

FROM openjdk:16.0.1-slim
COPY --from=build /build/target/*.jar /
RUN mv /*.jar /app.jar
