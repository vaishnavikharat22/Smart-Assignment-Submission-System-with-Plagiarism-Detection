# Backend Setup Guide

## Prerequisites

- Java 17 JDK
- Maven 3.6+
- MySQL 5.7+ or PostgreSQL 12+

## Database Setup

### MySQL
```bash
mysql -u root -p
CREATE DATABASE plagiarism_db;
CREATE USER 'plagiarism'@'localhost' IDENTIFIED BY 'plagiarism123';
GRANT ALL PRIVILEGES ON plagiarism_db.* TO 'plagiarism'@'localhost';
FLUSH PRIVILEGES;
```

### PostgreSQL
```bash
createdb plagiarism_db
createuser plagiarism
psql -U postgres -d plagiarism_db -c "ALTER USER plagiarism WITH PASSWORD 'plagiarism123';"
GRANT ALL PRIVILEGES ON DATABASE plagiarism_db TO plagiarism;
```

## Build and Run

```bash
cd backend

# Build
mvn clean install

# Run
mvn spring-boot:run

# Or run JAR file
mvn clean package
java -jar target/assignment-system-1.0.0.jar
```

## Configuration

Edit `src/main/resources/application.properties`:

```properties
# MySQL (default)
spring.datasource.url=jdbc:mysql://localhost:3306/plagiarism_db?useSSL=false&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=root
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.database-platform=org.hibernate.dialect.MySQL8Dialect

# Or PostgreSQL
spring.datasource.url=jdbc:postgresql://localhost:5432/plagiarism_db
spring.datasource.username=plagiarism
spring.datasource.password=plagiarism123
spring.datasource.driver-class-name=org.postgresql.Driver
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect

# JWT
jwt.secret=mySecretKeyForJWTTokenGenerationAndValidation123456789012345678901234567890
jwt.expiration=86400000

# File Upload
spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=10MB

# Logging
logging.level.com.plagiarism=INFO
```

## Default Port

Backend runs on `http://localhost:8080`

## Tables Created

The application will automatically create the following tables:

- `users` - User accounts
- `assignments` - Created assignments
- `submissions` - Student submissions
- `plagiarism_results` - Plagiarism detection results

## Testing Endpoints

```bash
# Health check
curl http://localhost:8080/api/assignments

# Register
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"email":"student@test.com","password":"password123","fullName":"Test Student","role":"STUDENT"}'

# Login
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"student@test.com","password":"password123"}'
```

## Troubleshooting

### Port Already in Use
```bash
# Windows
netstat -ano | findstr :8080
taskkill /PID <PID> /F

# Linux/Mac
lsof -i :8080
kill -9 <PID>
```

### Database Connection Issues
1. Verify database is running
2. Check credentials in application.properties
3. Ensure database exists: `SHOW DATABASES;`
4. Test connection with MySQL Workbench or pgAdmin

### Maven Build Issues
```bash
mvn clean install -U  # Update snapshots
mvn -X clean install  # Debug mode
```

## Database Schema

### Users Table
- id (PK)
- email (Unique)
- password
- full_name
- role (STUDENT, TEACHER, ADMIN)
- enabled
- created_at
- updated_at

### Assignments Table
- id (PK)
- title
- description
- teacher_id (FK)
- due_date
- max_score
- created_at
- updated_at

### Submissions Table
- id (PK)
- assignment_id (FK)
- student_id (FK)
- file_path
- file_name
- extracted_text
- status
- score
- feedback
- created_at
- updated_at

### Plagiarism Results Table
- id (PK)
- submission_id (FK)
- similarity_score
- total_comparisons
- detailed_report
- highlighted_text
- checked_at
