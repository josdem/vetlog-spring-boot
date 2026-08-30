# Contributing to Vetlog Spring Boot

Thank you for your interest in contributing to Vetlog! Your help is highly appreciated. This guide will walk you through
the setup process, development workflows, and best practices for contributing to the project.

---

## Project Setup

### Prerequisites

- **Java 21+**
- **Docker**

---

## Running the Application

### Option 1: Full Containerized Setup (Recommended)

```bash
git clone https://github.com/josdem/vetlog-spring-boot.git
cd vetlog-spring-boot/docker
docker-compose up -d
```

- **App:** http://localhost:8080
- **Health Check:** http://localhost:8080/actuator/health
- **MySQL DB:** localhost:3306
    - Username: `vetlog_user`
    - Password: `vetlog_password`

### Option 2: Hybrid Development (Run App in IDE)

1. Start only MySQL in Docker:

    ```bash
    cd docker
    docker-compose up -d mysql
    ```

2. Update the following files:

    - `src/main/resources/application.yml`

      ```yaml
      spring:
        datasource:
          url: jdbc:mysql://localhost:3306/vetlog
          username: vetlog_user
          password: vetlog_password
      ```

    - `build.gradle.kts`

      ```kotlin
      flyway {
        url = "jdbc:mysql://localhost:3306/vetlog"
        user = "vetlog_user"
        password = "vetlog_password"
      }
      ```

3. Run the application from your IDE.

---

## How to Contribute

1. **Get the repository**
   - If you are a **new contributor**: **Fork** the repository
   - If you already have **write access**: **Clone** the repository
2. **Create a feature branch**:
    ```bash
    git checkout -b feature/<short-description>
    ```
3. **Make your changes**
4. **Commit using clear messages** (use [Conventional Commits](https://www.conventionalcommits.org/en/v1.0.0/) if
   possible)
    ```bash
    git commit -m "feat: add ability to update pet profile"
    ```
5. **Push and submit a Pull Request (PR)** against the `main` branch
6. In your PR:
    - Link any relevant issue (e.g. `Closes #652`)
    - Provide a short summary of your changes

---

## Testing

To run tests locally:

1. Ensure MySQL is running (from Docker)
2. Run:

    ```bash
    ./gradlew test
    ```

   Or run specific tests:

    ```bash
    ./gradlew test --tests UserServiceTest
    ```

- **Tests connect to:** `localhost:3306`
- **JDK & Gradle** must be installed locally

---

## Code Style & Linting

- Follow Java standard formatting
- Prefer meaningful names
- Use consistent indentation

_If you use IntelliJ IDEA, use "Reformat Code" before committing._

---

## Support

If you face any issues:

- Check logs: `docker-compose logs`
- Make sure Docker is running
- Confirm DB container is healthy: `docker-compose ps`
- Reach out via GitHub Issues

---

Thank you for contributing to Vetlog!️
