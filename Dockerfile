# Build
FROM maven:3.8.3-amazoncorretto-17 AS build
LABEL authors="ps"
WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src
RUN mvn clean package -DskipTests

# Run
FROM amazoncorretto:17-alpine
WORKDIR /app

COPY --from=build /app/target/pims-prices-1.0.2-SNAPSHOT.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]
