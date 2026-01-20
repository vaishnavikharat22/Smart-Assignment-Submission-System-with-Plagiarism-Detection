# Smart Assignment Submission System with Plagiarism Detection

![Java](https://img.shields.io/badge/Java-17-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.1.5-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)
![React](https://img.shields.io/badge/React-18-61DAFB?style=for-the-badge&logo=react&logoColor=black)
![License](https://img.shields.io/badge/License-MIT-yellow.svg?style=for-the-badge)
![Build Status](https://img.shields.io/badge/Build-Passing-brightgreen?style=for-the-badge)

A comprehensive, full-stack application designed to streamline assignment management for educational institutions. It features role-based access for students and teachers, with a robust, built-in plagiarism detection engine that analyzes submissions for originality using Cosine Similarity and N-gram analysis.

---

## 📖 Quick Links

| Document | Description |
|----------|-------------|
| [**🚀 Quick Start**](QUICKSTART.md) | Get the app running in under 5 minutes. |
| [**📘 Project Summary**](PROJECT_SUMMARY.md) | Detailed overview of features, architecture, and what has been built. |
| [**⚙️ Backend Setup**](BACKEND_SETUP.md) | In-depth guide for configuring the Spring Boot backend. |
| [**🎨 Frontend Setup**](FRONTEND_SETUP.md) | Instructions for setting up the React frontend. |
| [**🔌 API Documentation**](API_DOCUMENTATION.md) | Complete reference for all REST API endpoints. |
| [**🧪 Testing Guide**](TESTING_GUIDE.md) | How to run tests and verify system integrity. |

---

## 🏗️ System Architecture

```mermaid
graph TD
    Client[React Frontend] -->|REST API / JWT| LB[Load Balancer / Nginx]
    LB --> Backend[Spring Boot Backend]
    
    subgraph "Backend Services"
        Backend --> Auth[Auth Service]
        Backend --> Assign[Assignment Service]
        Backend --> Sub[Submission Service]
        Backend --> Plag[Plagiarism Engine]
    end
    
    subgraph "Data & Storage"
        Backend --> DB[(MySQL / PostgreSQL)]
        Sub --> FileStore[File Storage]
    end
    
    Plag -->|Extract Text| PDFBox[Apache PDFBox]
    Plag -->|Compare| DB
```

---

## ✨ Key Features

### 👩‍🎓 For Students
*   **Secure Dashboard**: View assignments and track submission status.
*   **Multi-format Uploads**: Support for PDF, DOCX, and TXT files.
*   **Instant Feedback**: View grades and similarity reports once released.

### 👨‍🏫 For Teachers
*   **Assignment Management**: Create, edit, and manage deadlines easily.
*   **Automated Plagiarism Checks**: Instantly detect copied content across submissions.
*   **Detailed Reporting**: View similarity scores with highlighted text comparisons.
*   **Grading System**: Grade assignments and provide specific feedback.

### 🛡️ Security & Core
*   **JWT Authentication**: Stateless, secure access control.
*   **Role-Based Access**: Strict separation between Student, Teacher, and Admin data.
*   **Data Integrity**: Transaction-based operations to ensure consistent data states.

---

## 🛠️ Technology Stack

*   **Backend**: Java 17, Spring Boot 3.1.5, Spring Security, Spring Data JPA
*   **Frontend**: React 18, React Router, Axios, CSS3
*   **Database**: MySQL 8.0 (Compatible with PostgreSQL)
*   **Utilities**: Apache PDFBox (Text Extraction), JJWT (Security), Lombok
*   **DevOps**: Docker, Docker Compose

---

## 🚀 Getting Started

The fastest way to run the system is using Docker.

```bash
# Clone the repository
git clone https://github.com/vaishnavikharat22/Smart-Assignment-Submission-System-with-Plagiarism-Detection.git

# Navigate to the project root
cd assignment-plagiarism-system

# Start with Docker Compose
docker-compose up -d
```

Access the application:
*   **Frontend**: `http://localhost:3000`
*   **Backend**: `http://localhost:8080`

For manual installation instructions (without Docker), please refer to the [**Backend Setup**](BACKEND_SETUP.md) and [**Frontend Setup**](FRONTEND_SETUP.md) guides.

---

## 📄 License

This project is licensed under the [MIT License](LICENSE).
