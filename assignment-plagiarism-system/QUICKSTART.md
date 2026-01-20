# Quick Start Guide

## 🚀 Quick Start (5 minutes)

### Option 1: Docker (Easiest)

```bash
# Make sure Docker is installed
docker --version

# Navigate to project root
cd assignment-plagiarism-system

# Start all services
docker-compose up -d

# Access the application
# Frontend: http://localhost:3000
# Backend: http://localhost:8080
# MySQL: localhost:3306
```

### Option 2: Manual Setup

#### Step 1: Start Backend
```bash
cd backend

# Edit application.properties with your database credentials
mvn spring-boot:run
```

Backend will run on: `http://localhost:8080`

#### Step 2: Start Frontend (in new terminal)
```bash
cd frontend
npm install
npm start
```

Frontend will run on: `http://localhost:3000`

## 📝 First Steps

### 1. Create Test Accounts

**Register as Student:**
1. Go to http://localhost:3000/register
2. Fill in:
   - Full Name: John Student
   - Email: student@test.com
   - Password: password123
   - Role: Student
3. Click Register

**Register as Teacher:**
1. Go to http://localhost:3000/register
2. Fill in:
   - Full Name: Jane Teacher
   - Email: teacher@test.com
   - Password: password123
   - Role: Teacher
3. Click Register

### 2. Create Assignment (as Teacher)

1. Login as teacher
2. Go to Dashboard
3. Fill "Create Assignment":
   - Title: Java Programming Assignment
   - Description: Write a program to calculate factorial
   - Due Date: Select future date
   - Max Score: 100
4. Click "Create Assignment"

### 3. Submit Assignment (as Student)

1. Logout and login as student
2. Go to Dashboard
3. Click "Submit Assignment" on any assignment
4. Upload a text file with some code
5. Click "Submit"

### 4. Check Plagiarism (as Teacher)

1. Logout and login as teacher
2. Go to assignment submissions
3. Click "Check" button next to submission
4. Wait for plagiarism analysis
5. View similarity score

## 📊 Sample Test Data

Create multiple submissions to test plagiarism detection:

**Submission 1** (Original):
```
def calculate_factorial(n):
    if n <= 1:
        return 1
    return n * calculate_factorial(n - 1)

result = calculate_factorial(5)
print(result)
```

**Submission 2** (Similar):
```
def fact(number):
    if number <= 1:
        return 1
    return number * fact(number - 1)

answer = fact(5)
print(answer)
```

**Submission 3** (Different):
```
def sum_numbers(numbers):
    total = 0
    for num in numbers:
        total += num
    return total

print(sum_numbers([1, 2, 3, 4, 5]))
```

## 🔑 Default Credentials

After docker-compose is up:
- MySQL User: `plagiarism`
- MySQL Password: `plagiarism123`
- MySQL Database: `plagiarism_db`

## 🛑 Stopping Services

### Docker
```bash
# Stop all services
docker-compose down

# Stop and remove volumes
docker-compose down -v
```

### Manual
- Backend: Press Ctrl+C in terminal
- Frontend: Press Ctrl+C in terminal

## 📱 API Testing

Test API endpoints with curl:

```bash
# Login
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"teacher@test.com","password":"password123"}'

# Get all assignments
curl http://localhost:8080/api/assignments

# Create assignment (replace TOKEN)
curl -X POST http://localhost:8080/api/assignments \
  -H "Authorization: Bearer TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "title":"Test Assignment",
    "description":"Test",
    "teacherId":1,
    "dueDate":"2025-12-31T23:59:59",
    "maxScore":100
  }'
```

## 🐛 Common Issues & Fixes

### Port Already in Use
```bash
# Windows: Find process using port 8080
netstat -ano | findstr :8080
taskkill /PID <PID> /F

# Linux/Mac: Find and kill process
lsof -i :8080
kill -9 <PID>
```

### Database Connection Failed
- Check MySQL is running
- Verify credentials in application.properties
- Create database: `CREATE DATABASE plagiarism_db;`

### Frontend Can't Connect to Backend
- Check backend is running: http://localhost:8080
- Check browser console (F12) for errors
- Verify CORS settings in SecurityConfig.java

### Docker Issues
```bash
# Remove all containers and volumes
docker-compose down -v

# Rebuild images
docker-compose build --no-cache

# Start fresh
docker-compose up -d
```

## 📚 Next Steps

1. **Explore Features**
   - Create multiple assignments
   - Submit different versions
   - Test plagiarism detection

2. **Customize System**
   - Change JWT secret in properties
   - Adjust plagiarism sensitivity
   - Modify email templates

3. **Deploy to Cloud**
   - AWS (EC2, RDS)
   - Azure (App Service, Database)
   - Google Cloud (Compute Engine)

## 📖 Full Documentation

- See [README.md](README.md) for complete features
- See [BACKEND_SETUP.md](BACKEND_SETUP.md) for backend configuration
- See [FRONTEND_SETUP.md](FRONTEND_SETUP.md) for frontend configuration

## 🆘 Need Help?

1. Check the documentation files
2. Review API responses in browser DevTools
3. Check backend logs: `docker logs plagiarism_backend`
4. Check database: `mysql -u plagiarism -p plagiarism_db`

---

**Happy coding! 🎉**
