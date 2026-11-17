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

# Check whether the Spring Boot application is responding
HEALTHCHECK --interval=30s --timeout=5s --start-period=60s --retries=3 \
    CMD wget -q --spider http://127.0.0.1:8080/ || exit 1

ENTRYPOINT ["java", "-jar", "app.jar"]