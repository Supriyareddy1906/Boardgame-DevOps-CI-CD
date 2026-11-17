# ===== Build Stage =====
FROM maven:3.9.9-eclipse-temurin-17 AS build

WORKDIR /app

COPY pom.xml .
RUN mvn -B -q -DskipTests dependency:go-offline

COPY src ./src
RUN mvn clean package -DskipTests

# ===== Runtime Stage =====
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Create a non-root application user
RUN addgroup -S -g 10001 app && adduser -S -u 10001 -G app app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

# Run application as non-root user
USER app

ENTRYPOINT ["java", "-jar", "app.jar"]

