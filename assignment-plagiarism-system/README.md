# Smart Assignment Submission System with Plagiarism Detection

A comprehensive full-stack application for managing assignments with built-in plagiarism detection using Java Spring Boot and React.

## 🎯 Features

### 👩‍🎓 Student Features
- Register & Login with JWT authentication
- View all available assignments
- Submit assignments (PDF, DOC, TXT)
- Track submission status
- View plagiarism reports
- See grades and feedback

### 👨‍🏫 Teacher Features
- Create and manage assignments
- View all student submissions
- Automatic plagiarism detection
- Review similarity scores
- Grade submissions with feedback
- Download plagiarism reports
- Compare with other submissions

### 👨‍💻 Admin Features
- Manage all users
- Enable/disable user accounts
- View system analytics
- Access audit logs

### 🔍 Plagiarism Detection
- **Cosine Similarity Algorithm**: Compares term frequencies
- **N-gram Analysis**: Token-based text comparison
- **Similarity Scoring**: 0-100% plagiarism score
- **Text Highlighting**: Identifies similar sections
- **Batch Comparison**: Compares against all submissions

## 🛠 Tech Stack

### Backend
- **Java 17** - Latest LTS version
- **Spring Boot 3.1.5** - REST API Framework
- **Spring Security** - Authentication & Authorization
- **JWT** - Stateless authentication
- **Spring Data JPA** - Database ORM
- **MySQL/PostgreSQL** - Database
- **Apache PDFBox** - PDF text extraction

### Frontend
- **React 18** - UI Framework
- **React Router** - Navigation
- **Axios** - HTTP Client
- **CSS3** - Styling

## 📋 Prerequisites

### Required
- Java 17 JDK
- Node.js 16+ & npm
- MySQL 5.7+ or PostgreSQL 12+
- Maven 3.6+

### Optional
- Docker & Docker Compose
- Git

## ⚙️ Installation & Setup

### 1. Database Setup

#### MySQL
```bash
# Login to MySQL
mysql -u root -p

# Create database
CREATE DATABASE plagiarism_db;
CREATE USER 'plagiarism_user'@'localhost' IDENTIFIED BY 'password123';
GRANT ALL PRIVILEGES ON plagiarism_db.* TO 'plagiarism_user'@'localhost';
FLUSH PRIVILEGES;
```

#### PostgreSQL
```bash
# Create database
createdb plagiarism_db
createuser plagiarism_user
psql -U postgres -d plagiarism_db -c "ALTER USER plagiarism_user WITH PASSWORD 'password123';"
```

### 2. Backend Setup

```bash
# Navigate to backend directory
cd assignment-plagiarism-system/backend

# Update application.properties with your database credentials
# For MySQL (default):
spring.datasource.url=jdbc:mysql://localhost:3306/plagiarism_db?useSSL=false&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=root

# For PostgreSQL:
spring.datasource.url=jdbc:postgresql://localhost:5432/plagiarism_db
spring.datasource.username=plagiarism_user
spring.datasource.password=password123
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.hibernate.ddl-auto=update

# Build the project
mvn clean install

# Run the application
mvn spring-boot:run
```

The backend will start on `http://localhost:8080`

### 3. Frontend Setup

```bash
# Navigate to frontend directory
cd assignment-plagiarism-system/frontend

# Install dependencies
npm install

# Start the development server
npm start
```

The frontend will start on `http://localhost:3000`

## 🔐 Authentication

### Default Credentials (Create your own)

The system uses JWT tokens for authentication. 

1. Register a new account (Student/Teacher)
2. Login with email and password
3. Receive JWT token stored in localStorage

### API Endpoints

#### Authentication
- `POST /api/auth/register` - Register new user
- `POST /api/auth/login` - Login user
- `GET /api/auth/me` - Get current user

#### Assignments
- `GET /api/assignments` - Get all assignments
- `GET /api/assignments/{id}` - Get assignment details
- `POST /api/assignments` - Create assignment (Teacher)
- `PUT /api/assignments/{id}` - Update assignment (Teacher)
- `DELETE /api/assignments/{id}` - Delete assignment (Teacher)

#### Submissions
- `POST /api/submissions/upload` - Submit assignment
- `GET /api/submissions/{id}` - Get submission details
- `GET /api/submissions/assignment/{assignmentId}` - Get submissions for assignment
- `GET /api/submissions/student/{studentId}` - Get student submissions
- `POST /api/submissions/{id}/grade` - Grade submission (Teacher)

#### Plagiarism Detection
- `POST /api/submissions/{id}/check-plagiarism` - Run plagiarism check
- `GET /api/submissions/{id}/plagiarism` - Get plagiarism result
- `GET /api/submissions/assignment/{assignmentId}/plagiarism-results` - Get all results for assignment

#### Users (Admin)
- `GET /api/users` - Get all users
- `GET /api/users/{id}` - Get user details
- `GET /api/users/role/{role}` - Get users by role
- `PUT /api/users/{id}/disable` - Disable user
- `PUT /api/users/{id}/enable` - Enable user
- `DELETE /api/users/{id}` - Delete user

## 📊 Plagiarism Detection Algorithm

### How It Works

1. **Text Extraction**: Extract text from uploaded PDF/DOC/TXT files
2. **Normalization**: Clean text (lowercase, remove special chars)
3. **Tokenization**: Split into words and generate n-grams
4. **Comparison**: Compare against all other submissions
5. **Scoring**: Calculate similarity using:
   - Jaccard Similarity (n-gram overlap)
   - Cosine Similarity (term frequency)
   - Combined Score = (Jaccard × 0.4) + (Cosine × 0.6) × 100

### Similarity Score Interpretation

- **0-20%**: Low similarity - Likely original work
- **20-50%**: Moderate similarity - Review required
- **50-80%**: High similarity - Probable plagiarism
- **80%+**: Critical - Definite plagiarism

## 📁 Project Structure

```
assignment-plagiarism-system/
├── backend/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/plagiarism/
│   │   │   │   ├── controller/     # REST controllers
│   │   │   │   ├── entity/         # JPA entities
│   │   │   │   ├── repository/     # Data repositories
│   │   │   │   ├── service/        # Business logic
│   │   │   │   ├── security/       # JWT & Security config
│   │   │   │   ├── dto/            # Data transfer objects
│   │   │   │   └── util/           # Utility classes
│   │   │   └── resources/          # Configuration files
│   │   └── test/                   # Unit tests
│   └── pom.xml                     # Maven config
├── frontend/
│   ├── src/
│   │   ├── components/             # React components
│   │   ├── pages/                  # Page components
│   │   ├── services/               # API services
│   │   ├── context/                # React context
│   │   ├── styles/                 # CSS files
│   │   ├── App.js                  # Main app component
│   │   └── index.js                # Entry point
│   ├── public/                     # Static files
│   └── package.json                # npm config
└── README.md                       # This file
```

## 🚀 Usage Examples

### Creating an Assignment (Teacher)
1. Login as teacher
2. Go to "Create Assignment" section
3. Fill in title, description, due date, max score
4. Click "Create Assignment"

### Submitting an Assignment (Student)
1. Login as student
2. View available assignments
3. Click "Submit Assignment"
4. Upload PDF, DOC, or TXT file
5. Click "Submit"

### Checking Plagiarism (Teacher)
1. Navigate to assignment submissions
2. Click "Check" button for plagiarism detection
3. View similarity score and detailed report
4. Review highlighted copied sections

### Grading (Teacher)
1. Select submission
2. Enter score and feedback
3. Click "Grade"
4. Score is sent to student

## 🔧 Configuration

### JWT Settings
```properties
jwt.secret=your-secret-key-here
jwt.expiration=86400000  # 24 hours in milliseconds
```

### File Upload
```properties
spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=10MB
```

### Supported File Types
- `.pdf` - PDF documents
- `.doc` / `.docx` - Microsoft Word documents
- `.txt` - Plain text files

## 📈 Advanced Features (Future)

- [ ] Email notifications after submission
- [ ] Dashboard with plagiarism analytics
- [ ] Configurable plagiarism threshold alerts
- [ ] Support for image-based PDFs (OCR)
- [ ] Batch plagiarism checks
- [ ] Custom plagiarism sensitivity levels
- [ ] API rate limiting
- [ ] Two-factor authentication
- [ ] Assignment groups/categories
- [ ] Student comment threads

## 🐛 Troubleshooting

### Backend won't start
```bash
# Check Java version
java -version  # Should be 17+

# Check Maven
mvn -v

# Clear Maven cache
mvn clean install
```

### Database connection error
```bash
# Verify MySQL/PostgreSQL is running
# Check credentials in application.properties
# Verify database exists
```

### Frontend won't connect to backend
```bash
# Verify backend is running on http://localhost:8080
# Check CORS settings in SecurityConfig.java
# Check browser console for errors (F12)
```

## 📝 License

This project is licensed under the MIT License.

## 👥 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit changes (`git commit -m 'Add AmazingFeature'`)
4. Push to branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 🤝 Support

For issues and questions:
1. Check existing issues in repository
2. Create a detailed bug report
3. Include screenshots if applicable
4. Provide system information (OS, Java version, etc.)

## 📞 Contact

For more information or inquiries, please reach out to the development team.

---

**Version**: 1.0.0  
**Last Updated**: January 2026  
**Status**: Active Development
