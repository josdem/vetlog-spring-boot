# --- Build Stage ---
FROM gradle:8.8-jdk21 AS build
WORKDIR /app

# Copy only gradle files and project metadata first for better caching
COPY build.gradle.kts settings.gradle.kts gradle.properties ./
COPY gradle/ gradle/

# Download dependencies (will be cached unless build files change)
RUN gradle --no-daemon dependencies

# Copy the rest of the source code
COPY . .

# Run spotless to format the code
RUN gradle --no-daemon spotlessApply
# Build the Spring Boot app (skip tests for speed)
RUN gradle --no-daemon clean build -x test

# --- Runtime Stage ---
FROM eclipse-temurin:21-jre AS runtime
WORKDIR /app

# COPY the built jar to the runtime stage
COPY --from=build /app/build/libs/*.jar app.jar

# Create a non-root user for security
RUN useradd -ms /bin/bash appuser
USER appuser

EXPOSE 8080

HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1

ENTRYPOINT ["java", "-jar", "app.jar"] 