# Build stage
FROM maven:3.9.9-eclipse-temurin-23-alpine AS build
WORKDIR /app

# Copy pom.xml and source code
COPY pom.xml .
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests

# Run stage
FROM eclipse-temurin:23-jre-alpine
WORKDIR /app

# Copy the jar from build stage
COPY --from=build /app/target/bfhl-api-1.0.0.jar app.jar

# Expose port (Render uses PORT env var, which Spring Boot will bind to)
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
