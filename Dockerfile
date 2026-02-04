FROM maven:3.9-eclipse-temurin-21-alpine AS build
WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline -B

COPY src ./src

RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app

COPY --from=build /app/target/ong-pet-0.0.1-SNAPSHOT.jar app.jar

CMD ["java", "-jar", "app.jar"]