FROM gradle:8.7.0-jdk17 AS build
WORKDIR /app
COPY build.gradle settings.gradle ./
COPY src ./src
RUN gradle build

FROM postgres:15
COPY init-scripts/test.sql /docker-entrypoint-initdb.d/test.sql
EXPOSE 5432

FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/build/libs/*.jar sw-sku-service.jar
EXPOSE 5432
ENTRYPOINT ["java", "-jar", "sw-sku-service.jar"]