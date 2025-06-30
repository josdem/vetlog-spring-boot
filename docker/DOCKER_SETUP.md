# Docker Setup for Vetlog Spring Boot

This guide will help you set up the Vetlog Spring Boot application using Docker for local development.

## Prerequisites

- Docker Desktop installed and running
- Docker Compose (usually included with Docker Desktop)
- At least 4GB of available RAM

## Quick Start

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd vetlog-spring-boot
   ```

2. **Start the application**
   ```bash
   cd docker
   docker-compose up -d
   ```

3. **Access the application**
   - Application: http://localhost:8080
   - Health check: http://localhost:8080/actuator/health
   - Database: localhost:3306 (user: vetlog_user, password: vetlog_password)

## Services

### MySQL Database
- **Port**: 3306
- **Database**: vetlog
- **Username**: vetlog_user
- **Password**: vetlog_password
- **Root Password**: rootpassword
- **Health Check**: Automatically configured

### Spring Boot Application
- **Port**: 8080
- **Health Check**: Available at `/actuator/health`
- **Database**: Automatically connects to MySQL container
- **Flyway**: Automatically runs migrations on startup
- **Build**: Uses multi-stage Dockerfile with Gradle

## Development Workflows

### Full Containerized Development
```bash
# Start both MySQL and Spring Boot app
cd docker
docker-compose up -d

# View logs
docker-compose logs -f

# Stop services
docker-compose down
```

### Hybrid Development
```bash
# Start only MySQL container
cd docker
docker-compose up mysql
```
### Run Spring Boot app from your IDE
#### The app will connect to localhost:3306

**Important for Hybrid Development:**
- **You must manually update `src/main/resources/application.yml`** to use hardcoded credentials:
  ```yaml
  spring:
    datasource:
      username: vetlog_user
      password: vetlog_password
  ```
- **You must also update `build.gradle.kts`** to use hardcoded Flyway credentials:
  ```kotlin
  flyway {
    url = "jdbc:mysql://localhost:3306/vetlog"
    user = "vetlog_user"
    password = "vetlog_password"
  }
   ```

- Replace the environment variable references (`${USER}` and `${PASSWORD}`) with the hardcoded values
- These credentials must match the Docker MySQL container setup:
  - Username: `vetlog_user`
  - Password: `vetlog_password`
- No environment variables or .env file needed for local development

### Making Code Changes
1. **Full containerized approach**:
   ```bash
   # Rebuild and restart the application
   docker-compose build vetlog-app
   docker-compose up -d vetlog-app
   ```

2. **Hybrid approach**:(Recommended)
   - Make changes in your IDE
   - Restart the application from IDE
   - No Docker rebuild needed

## Testing

### Recommended Way to Run Tests (Hybrid Approach)

1. **Start MySQL (in Docker):**
   ```sh
   cd docker
   docker-compose up -d mysql
   ```

2. **From your project root, run tests using your local Gradle or from your IDE:**
   ```sh
   ./gradlew test
   ```
   - Or run specific test classes:
   ```sh
   ./gradlew test --tests PetControllerTest
   ./gradlew test --tests UserServiceTest
   ```

**Notes:**
- The application and tests connect to MySQL at `localhost:3306`.
- The runtime container is only for running the built app, not for testing.
- All tests must run in an environment with Gradle and JDK (your local machine).

### Example Database Configuration for Hybrid Testing

In `src/main/resources/application.yml`:
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/vetlog
    username: vetlog_user
    password: vetlog_password
```

In `build.gradle.kts`:
```kotlin
flyway {
  url = "jdbc:mysql://localhost:3306/vetlog"
  user = "vetlog_user"
  password = "vetlog_password"
}
```

## Useful Commands

### Start services
```bash
cd docker
docker-compose up -d
```

### View logs
```bash
# All services
docker-compose logs -f

# Specific service
docker-compose logs -f vetlog-app
docker-compose logs -f mysql
```

### Stop services
```bash
docker-compose down
```

### Rebuild and restart
```bash
docker-compose down
docker-compose build --no-cache
docker-compose up -d
```

### Access database
```bash
# Connect to MySQL container
docker-compose exec mysql mysql -u vetlog_user -p vetlog

# Or as root
docker-compose exec mysql mysql -u root -p
```

### Clean up everything
```bash
docker-compose down -v --remove-orphans
docker system prune -f
```

## Database Migrations

- **Flyway migrations** are automatically applied on startup
- **Migration files** are located in `src/main/resources/db/migration/`
- **Database schema** is automatically created and updated
- **No manual migration steps** required

## Configuration

### Default Configuration
The application uses these default settings (configured in `docker-compose.yml`):

- **Database**: MySQL 8.0 with vetlog database
- **Application**: Spring Boot on port 8080
- **Health Checks**: Both MySQL and Spring Boot have health checks
- **Networking**: Services communicate via Docker network
- **Volumes**: MySQL data is persisted in Docker volume

## Troubleshooting

### Application won't start
1. **Check if MySQL is healthy**:
   ```bash
   docker-compose ps
   ```
2. **View application logs**:
   ```bash
   docker-compose logs vetlog-app
   ```
3. **Check health endpoint**:
   ```bash
   curl http://localhost:8080/actuator/health
   ```

### Database connection issues
1. **Verify MySQL is running**:
   ```bash
   docker-compose exec mysql mysqladmin ping -h localhost -u root -prootpassword
   ```
2. **Check database logs**:
   ```bash
   docker-compose logs mysql
   ```
3. **Test connection from host**:
   ```bash
   mysql -h localhost -P 3306 -u vetlog_user -p vetlog
   ```

### Port conflicts
If ports 8080 or 3306 are already in use, modify the `docker-compose.yml` file:
```yaml
ports:
  - "8081:8080"  # Change 8080 to 8081
```

### Build issues
1. **Clear Docker cache**:
   ```bash
   docker-compose build --no-cache
   ```
2. **Check Dockerfile**:
   ```bash
   docker-compose build vetlog-app --progress=plain
   ```

### Flyway migration issues
1. **Check migration files** in `src/main/resources/db/migration/`
2. **View application startup logs** for migration errors
3. **Reset database** (if needed):
   ```bash
   docker-compose down -v
   docker-compose up -d
   ```

## File Structure

```
docker/
├── docker-compose.yml    # Service definitions
├── Dockerfile           # Multi-stage build for Spring Boot app
├── .dockerignore        # Files to exclude from Docker build
└── DOCKER_SETUP.md      # This documentation
```

## Support

If you encounter issues:
1. **Check the logs**: `docker-compose logs`
2. **Verify Docker Desktop** is running
3. **Ensure ports** are not in use
4. **Check available disk space**
5. **Verify Docker has enough memory** (at least 4GB recommended)
