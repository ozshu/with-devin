# Spring Boot Docker Compose Project

This project demonstrates a multi-container Docker setup with MySQL database, MailHog email testing, and a Spring Boot application.

## Architecture

The project consists of 3 Docker containers:

1. **MySQL Database** (`mysql_db`)
   - Contains a `messages` table with `id` and `text` columns
   - Pre-populated with one record: `id=1, text="Hello"`
   - Accessible on port 3306

2. **MailHog** (`mailhog`)
   - Email testing tool for development
   - Web interface on port 8025
   - SMTP server on port 1025

3. **Spring Boot Application** (`spring_boot_app`)
   - REST API and web interface
   - Connects to MySQL database
   - Accessible on port 8080

## Endpoints

The Spring Boot application provides two endpoints that both accept a `message_id` parameter:

### 1. Web Page Endpoint
- **URL**: `http://localhost:8080/message?message_id={id}`
- **Method**: GET
- **Description**: Displays a styled web page showing the message from the database
- **Example**: `http://localhost:8080/message?message_id=1`

### 2. REST API Endpoint
- **URL**: `http://localhost:8080/api/message?message_id={id}`
- **Method**: GET
- **Description**: Returns JSON response with message data
- **Example**: `http://localhost:8080/api/message?message_id=1`
- **Response**: `{"id":1,"text":"Hello"}`

## Quick Start

### Prerequisites
- Docker and Docker Compose installed
- No need for local Java or Maven installation (handled by Docker containers)

### Running the Application

1. **Clone the project**
   ```bash
   git clone https://github.com/ozshu/with-devin.git
   cd with-devin
   git checkout devin/1750129856-spring-docker-compose
   ```

2. **Set up environment variables**
   ```bash
   cp .env.example .env
   # Edit .env file with your preferred database credentials
   ```

3. **Start all containers (Maven build happens automatically in Docker)**
   ```bash
   docker-compose up -d
   ```

4. **Verify all containers are running**
   ```bash
   docker-compose ps
   ```

### Testing the Application

1. **Test the REST API endpoint**
   ```bash
   curl "http://localhost:8080/api/message?message_id=1"
   ```
   Expected response: `{"id":1,"text":"Hello"}`

2. **Test the web page endpoint**
   Open in browser: `http://localhost:8080/message?message_id=1`

3. **Test error handling**
   ```bash
   curl "http://localhost:8080/api/message?message_id=999"
   ```
   Open in browser: `http://localhost:8080/message?message_id=999`

4. **Access MailHog web interface**
   Open in browser: `http://localhost:8025`

## Project Structure

```
spring-docker-project/
├── docker-compose.yml          # Docker Compose configuration
├── init.sql                    # MySQL initialization script
├── README.md                   # This file
└── spring-app/                 # Spring Boot application
    ├── Dockerfile              # Spring Boot container definition
    ├── pom.xml                 # Maven dependencies
    ├── src/
    │   └── main/
    │       ├── java/com/example/demo/
    │       │   ├── DemoApplication.java      # Main Spring Boot class
    │       │   ├── Message.java              # JPA Entity
    │       │   ├── MessageController.java    # REST Controller
    │       │   └── MessageRepository.java    # JPA Repository
    │       └── resources/
    │           ├── application.properties    # Spring configuration
    │           └── templates/
    │               └── message.html          # Thymeleaf template
    └── target/                 # Maven build output
```

## Configuration

### Environment Variables
Create a `.env` file from the provided `.env.example` template:

```bash
cp .env.example .env
```

Required environment variables:
- `MYSQL_ROOT_PASSWORD`: Root password for MySQL
- `MYSQL_DATABASE`: Database name (defaults to "testdb")
- `MYSQL_USER`: Database username
- `MYSQL_PASSWORD`: Database password

### Database Configuration
- **Host**: mysql (container name)
- **Port**: 3306
- **Database**: Configured via `MYSQL_DATABASE` environment variable
- **Username**: Configured via `MYSQL_USER` environment variable
- **Password**: Configured via `MYSQL_PASSWORD` environment variable

### Environment Variables
The Spring Boot application uses these environment variables (set in docker-compose.yml):
- `SPRING_DATASOURCE_URL`: JDBC connection string
- `SPRING_DATASOURCE_USERNAME`: Database username
- `SPRING_DATASOURCE_PASSWORD`: Database password
- `SPRING_MAIL_HOST`: MailHog hostname
- `SPRING_MAIL_PORT`: MailHog SMTP port

## Development Workflow

### Making Code Changes

When you modify Spring Boot source code (e.g., adding new endpoints, changing business logic), you need to manually rebuild the container to see your changes.

#### Basic Rebuild Process
```bash
# After making code changes to Spring Boot application
docker-compose build --no-cache spring-app
docker-compose up -d
```

#### Efficient Development Commands
```bash
# More efficient: rebuild and restart only the changed service
docker-compose up -d --build spring-app

# This command will:
# 1. Rebuild the spring-app container with your code changes
# 2. Restart only the spring-app container
# 3. Keep MySQL and MailHog containers running
```

#### Complete Reset (if issues occur)
```bash
# Stop all containers and rebuild everything from scratch
docker-compose down
docker-compose build --no-cache
docker-compose up -d
```

### Development Example

Here's a typical development cycle when adding a new endpoint:

1. **Make code changes**: Edit `MessageController.java` to add a new endpoint
2. **Rebuild container**: `docker-compose build --no-cache spring-app`
3. **Restart application**: `docker-compose up -d`
4. **Test changes**: `curl http://localhost:8080/your-new-endpoint`
5. **View logs**: `docker-compose logs spring-app` (if needed)

### Development Tips

- **Docker layer caching**: If you only change source code (not `pom.xml`), Maven dependencies won't be re-downloaded
- **Partial rebuilds**: Use `--build spring-app` to avoid rebuilding MySQL and MailHog containers
- **Log monitoring**: Use `docker-compose logs -f spring-app` to follow application logs in real-time
- **Container status**: Use `docker-compose ps` to check which containers are running

### Important Notes

- **No automatic rebuilds**: Code changes do NOT automatically trigger container rebuilds
- **Manual process**: You must run build commands after each code change
- **Multi-stage efficiency**: The Docker multi-stage build optimizes the rebuild process by caching layers

## Stopping the Application

```bash
docker-compose down
```

## Troubleshooting

1. **Containers not starting**: Check if ports 3306, 8025, 8080 are available
2. **Database connection issues**: Ensure MySQL container is fully started before Spring Boot app
3. **Build issues**: Make sure Java 17 and Maven are installed and the Spring Boot app is built

## Development Notes

- The application uses Spring Boot 3.2.0 with Java 17
- Database uses JPA/Hibernate for ORM
- Web interface uses Thymeleaf templating
- Error handling is implemented for non-existent message IDs
- All containers communicate through a custom Docker network
- **Multi-stage Docker build**: Maven compilation happens inside Docker container
- **No local dependencies**: No need to install Java or Maven locally
- **Optimized builds**: Docker layer caching for faster subsequent builds
