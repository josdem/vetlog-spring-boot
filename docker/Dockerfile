# -- NPM installation stage
FROM node:22.18 AS npm-install

WORKDIR /app

COPY src/main/resources/static/assets/servizi-dog-theme/package*.json ./
RUN npm install

# --- Build Stage ---
FROM gradle:8.14-jdk21 AS build
WORKDIR /app

# Copy only gradle files and project metadata first for better caching
COPY build.gradle.kts settings.gradle.kts gradle.properties ./
COPY gradle/ gradle/

# Download dependencies (will be cached unless build files change)
RUN gradle --no-daemon dependencies

# Copy the rest of the source code
COPY . .

# Copy the npm installation from the npm-install stage
COPY --from=npm-install /app/node_modules /app/src/main/resources/static/assets/servizi-dog-theme/node_modules

# Run spotless to format the code
RUN gradle --no-daemon spotlessApply
# Build the Spring Boot app (skip tests for speed)
RUN gradle --no-daemon clean build -x test

# The Gradle `jar` task emits a `-plain` jar alongside the executable boot jar,
# so pin the boot jar to a fixed name for the runtime stage to copy.
RUN rm -f build/libs/*-plain.jar && mv build/libs/*.jar build/libs/app.jar

# --- Runtime Stage ---
FROM eclipse-temurin:21-jre AS runtime
WORKDIR /app

# COPY the built jar to the runtime stage
COPY --from=build /app/build/libs/app.jar app.jar

# Create a non-root user for security
RUN useradd -ms /bin/bash appuser
USER appuser

EXPOSE 8080

HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1

ENTRYPOINT ["java", "-jar", "app.jar"] 