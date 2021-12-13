FROM openjdk:16.0.1-slim AS build
COPY . /build
WORKDIR /build
RUN chmod +x ./mvnw && ./mvnw clean package install -DskipTests

FROM openjdk:16.0.1-slim
COPY --from=build /build/target/*.jar /
RUN mv /*.jar /app.jar
