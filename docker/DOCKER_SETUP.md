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

2. **Set up environment variables**
   Create a `.env` file in the `docker` directory:
   ```bash
   cd docker
   cp env.example .env
   # Edit .env and add your Google API key
   ```

3. **Start the application**
   ```bash
   docker-compose up -d
   ```

4. **Access the application**
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

### Spring Boot Application
- **Port**: 8080
- **Health Check**: Available at `/actuator/health`
- **Database**: Automatically connects to MySQL container
- **Flyway**: Automatically runs migrations on startup

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

## Development Workflow

### Making Code Changes
1. Make your changes to the source code
2. Rebuild the application container:
   ```bash
   docker-compose build vetlog-app
   docker-compose up -d vetlog-app
   ```

### Database Migrations
- Flyway migrations are automatically applied on startup
- Migration files are located in `src/main/resources/db/migration/`
- The database is initialized with the master schema

### Environment Variables
The application uses the following environment variables:

| Variable | Description | Default |
|----------|-------------|---------|
| `USER` | Database username | vetlog_user |
| `PASSWORD` | Database password | vetlog_password |

## Troubleshooting

### Application won't start
1. Check if MySQL is healthy:
   ```bash
   docker-compose ps
   ```
2. View application logs:
   ```bash
   docker-compose logs vetlog-app
   ```
3. Ensure all environment variables are set correctly

### Database connection issues
1. Verify MySQL is running:
   ```bash
   docker-compose exec mysql mysqladmin ping -h localhost -u root -prootpassword
   ```
2. Check database logs:
   ```bash
   docker-compose logs mysql
   ```

### Port conflicts
If ports 8080 or 3306 are already in use, modify the `docker-compose.yml` file:
```yaml
ports:
  - "8081:8080"  # Change 8080 to 8081
```
## Support

If you encounter issues:
1. Check the logs: `docker-compose logs`
2. Verify Docker Desktop is running
3. Ensure ports are not in use
4. Check available disk space
5. Verify environment variables are set correctly 