# System Architecture & Testing Guide

## 🏗️ System Architecture

```
┌─────────────────────────────────────────────────────────────────┐
│                        CLIENT LAYER (React)                      │
├──────────────────────┬──────────────────────┬──────────────────┤
│   Student           │    Teacher           │      Admin        │
│   Dashboard         │    Dashboard         │      Panel        │
│                     │                      │                   │
│ • View Assign       │ • Create Assign      │ • Manage Users    │
│ • Upload Submission │ • View Submissions   │ • View Analytics  │
│ • View Scores       │ • Check Plagiarism   │ • Enable/Disable  │
│ • See Feedback      │ • Grade Submissions  │   Accounts        │
└──────────────────────┴──────────────────────┴──────────────────┘
                              ↓
                        HTTP (Axios)
                              ↓
┌─────────────────────────────────────────────────────────────────┐
│                   API GATEWAY (Spring Boot)                      │
├─────────────────────────────────────────────────────────────────┤
│  • Authentication (JWT)                                         │
│  • CORS Management                                              │
│  • Request/Response Processing                                  │
└─────────────────────────────────────────────────────────────────┘
                              ↓
┌─────────────────────────────────────────────────────────────────┐
│                    REST CONTROLLERS LAYER                        │
├──────────────────────┬──────────────────────┬──────────────────┤
│   AuthController    │  AssignmentController│ SubmissionController
│                     │                      │                   │
│ • Register          │ • Create Assign      │ • Upload File     │
│ • Login             │ • Get Assign List    │ • View Submission │
│ • Get Current User  │ • Update Assign      │ • Check Plagiarism│
│                     │ • Delete Assign      │ • Grade           │
└──────────────────────┴──────────────────────┴──────────────────┘
                              ↓
┌─────────────────────────────────────────────────────────────────┐
│                    BUSINESS LOGIC LAYER                          │
├──────────────────────┬──────────────────────┬──────────────────┤
│   UserService       │  AssignmentService   │SubmissionService  │
│                     │                      │                   │
│ • Create User       │ • CRUD Operations    │ • File Handling   │
│ • Auth Checks       │ • Role Validation    │ • Extract Text    │
│ • User Queries      │                      │ • Text Cleaning   │
└──────────────────────┴──────────────────────┴──────────────────┘
                              ↓
                    ┌─────────────────────┐
                    │ PlagiarismService   │
                    ├─────────────────────┤
                    │ • Check Similarity  │
                    │ • Generate Report   │
                    │ • Store Results     │
                    └─────────────────────┘
                              ↓
┌─────────────────────────────────────────────────────────────────┐
│                  PLAGIARISM ENGINE LAYER                         │
├─────────────────────────────────────────────────────────────────┤
│  PlagiarismDetectionEngine                                      │
│                                                                 │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐          │
│  │Normalization │→ │Tokenization  │→ │Similarity    │          │
│  │• Lowercase   │  │• N-gram Gen  │  │• Cosine      │          │
│  │• Remove Char │  │• Term Freq   │  │• Jaccard     │          │
│  │• Clean Space │  │• Vectors     │  │• Combined    │          │
│  └──────────────┘  └──────────────┘  └──────────────┘          │
└─────────────────────────────────────────────────────────────────┘
                              ↓
┌─────────────────────────────────────────────────────────────────┐
│                    DATA ACCESS LAYER (JPA)                       │
├──────────────────────┬──────────────────────┬──────────────────┤
│   UserRepository    │ AssignmentRepository │SubmissionRepository
│                     │                      │                   │
│ • findByEmail       │ • findByTeacher      │ • findByStudent   │
│ • findByRole        │ • findAll            │ • findByAssignment│
│ • Custom Queries    │                      │ • Custom Queries  │
└──────────────────────┴──────────────────────┴──────────────────┘
                              ↓
┌─────────────────────────────────────────────────────────────────┐
│                   PERSISTENCE LAYER (Database)                  │
├──────────────────────┬──────────────────────┬──────────────────┤
│      MySQL          │    PostgreSQL        │   File System    │
│                     │                      │                   │
│ • users             │ • users              │ • Submission     │
│ • assignments       │ • assignments        │   Files          │
│ • submissions       │ • submissions        │ • Extracted      │
│ • plagiarism_result │ • plagiarism_result  │   Text           │
└──────────────────────┴──────────────────────┴──────────────────┘
```

---

## 🔄 Data Flow Diagrams

### Authentication Flow
```
User Input (Email/Password)
         ↓
    AuthController
         ↓
    UserService (Password Validation)
         ↓
    JwtTokenProvider (Generate Token)
         ↓
    Response (Token + User Info)
         ↓
    Frontend (Store in localStorage)
```

### Submission Flow
```
Select File
     ↓
SubmissionController (Upload)
     ↓
FileTextExtractor (Extract Text)
     ↓
SubmissionService (Save to DB)
     ↓
PlagiarismService (Trigger Check)
     ↓
PlagiarismDetectionEngine
     ├─ Normalize Text
     ├─ Generate N-grams
     ├─ Compare with Others
     ├─ Calculate Score
     └─ Store Results
     ↓
Database Update
     ↓
Frontend Notification
```

### Plagiarism Detection Flow
```
Submission Text + All Other Submissions
         ↓
    Normalize (Lowercase, Remove Special Chars)
         ↓
    Tokenize (Split Words, Generate 3-grams)
         ↓
    For Each Comparison:
    ├─ Calculate Cosine Similarity (Term Frequency)
    ├─ Calculate Jaccard Similarity (N-gram Overlap)
    └─ Store Individual Scores
         ↓
    Combine Scores:
    Final Score = (Jaccard × 0.4) + (Cosine × 0.6) × 100
         ↓
    Generate Report (Details + Highlighting)
         ↓
    Store in PlagiarismResult
         ↓
    Return to Teacher
```

---

## ✅ Manual Testing Guide

### Test Setup
1. Database: MySQL running, `plagiarism_db` created
2. Backend: Running on `http://localhost:8080`
3. Frontend: Running on `http://localhost:3000`

### Test Case 1: User Registration & Login

**Step 1: Register as Student**
```
1. Navigate to http://localhost:3000/register
2. Fill form:
   - Full Name: Alice Student
   - Email: alice@test.com
   - Password: password123
   - Role: STUDENT
3. Click Register
4. Expected: Redirected to dashboard, user stored in DB
```

**Step 2: Logout**
```
1. Click "Logout" in navbar
2. Expected: Token removed, redirected to login
```

**Step 3: Login**
```
1. Navigate to http://localhost:3000/login
2. Enter: alice@test.com / password123
3. Click Login
4. Expected: Token received, dashboard displayed
```

**Verification:**
```bash
# Check user in database
mysql> SELECT * FROM users WHERE email='alice@test.com';
```

---

### Test Case 2: Teacher Creates Assignment

**Step 1: Register as Teacher**
```
1. Register with:
   - Full Name: Bob Teacher
   - Email: teacher@test.com
   - Password: password123
   - Role: TEACHER
```

**Step 2: Create Assignment**
```
1. Go to http://localhost:3000/dashboard
2. Fill "Create Assignment":
   - Title: Java Programming Assignment 1
   - Description: Write a program to find prime numbers
   - Due Date: 2025-12-31T23:59:59
   - Max Score: 100
3. Click "Create Assignment"
4. Expected: Assignment appears in list
```

**Step 3: Verify Assignment**
```
1. Navigate to http://localhost:3000/dashboard
2. Click "View Submissions" on assignment
3. Expected: Empty submissions list (no students submitted yet)
```

**Verification:**
```bash
# Check assignment in database
mysql> SELECT * FROM assignments WHERE title='Java Programming Assignment 1';

# Check API
curl http://localhost:8080/api/assignments
```

---

### Test Case 3: Student Submits Assignment

**Step 1: Create Test File**
```
Create file: assignment.txt with content:
---
public class PrimeChecker {
    public static void main(String[] args) {
        for (int i = 2; i <= 100; i++) {
            boolean isPrime = true;
            for (int j = 2; j < i; j++) {
                if (i % j == 0) {
                    isPrime = false;
                    break;
                }
            }
            if (isPrime) {
                System.out.println(i);
            }
        }
    }
}
---
```

**Step 2: Submit as Student**
```
1. Login as alice@test.com
2. Go to dashboard
3. Click "Submit Assignment" on assignment
4. Select assignment.txt
5. Click "Submit"
6. Expected: File uploaded, submission created
```

**Verification:**
```bash
# Check submission in database
mysql> SELECT * FROM submissions WHERE student_id=1;

# Check extracted text
mysql> SELECT extracted_text FROM submissions WHERE id=1\G
```

---

### Test Case 4: Multiple Submissions & Plagiarism Detection

**Step 1: Create More Students & Submissions**
```
Register 3 more students:
- charlie@test.com (Student)
- diana@test.com (Student)
- evan@test.com (Student)

Create test files with varying similarity:

File 1 (Charlie - 95% similar):
---
public class PrimeChecker {
    public static void main(String[] args) {
        for (int i = 2; i <= 100; i++) {
            boolean isPrime = true;
            for (int j = 2; j < i; j++) {
                if (i % j == 0) {
                    isPrime = false;
                    break;
                }
            }
            if (isPrime) {
                System.out.println(i);
            }
        }
    }
}
---

File 2 (Diana - 30% similar):
---
public class EvenOddChecker {
    public static void main(String[] args) {
        int count = 0;
        for (int i = 1; i <= 100; i++) {
            if (i % 2 == 0) {
                System.out.println(i + " is even");
                count++;
            }
        }
        System.out.println("Total even numbers: " + count);
    }
}
---

File 3 (Evan - 10% similar):
---
public class Fibonacci {
    public static void main(String[] args) {
        int a = 0, b = 1;
        for (int i = 0; i < 20; i++) {
            System.out.println(a);
            int temp = a + b;
            a = b;
            b = temp;
        }
    }
}
---
```

**Step 2: Check Plagiarism**
```
1. Login as teacher@test.com
2. Go to "View Submissions"
3. For each submission, click "Check" button
4. Wait for plagiarism analysis
5. Observe scores:
   - Alice vs Others: Should show high similarity with Charlie
   - Charlie vs Others: Should show high similarity with Alice
   - Diana vs Others: Should show moderate similarity
   - Evan vs Others: Should show low similarity
```

**Expected Results:**
```
Alice ↔ Charlie: 85-95%
Alice ↔ Diana: 15-30%
Alice ↔ Evan: 5-15%
Charlie ↔ Diana: 15-30%
Charlie ↔ Evan: 5-15%
Diana ↔ Evan: 5-15%
```

**Verification:**
```bash
# Check plagiarism results
mysql> SELECT s.id, pr.similarity_score FROM submissions s 
       LEFT JOIN plagiarism_results pr ON s.id = pr.submission_id;

# Check detailed report
mysql> SELECT detailed_report FROM plagiarism_results WHERE id=1\G
```

---

### Test Case 5: Grading Submissions

**Step 1: Grade Submission**
```
1. Login as teacher@test.com
2. Go to submissions
3. Click "Grade" button on submission
4. Fill:
   - Score: 85
   - Feedback: Good implementation, but inefficient
5. Click Submit
6. Expected: Status changes to GRADED, score saved
```

**Step 2: Student Views Grade**
```
1. Login as alice@test.com
2. Go to dashboard
3. View submissions
4. Expected: Score 85 and feedback visible
```

**Verification:**
```bash
# Check submission status
mysql> SELECT status, score, feedback FROM submissions WHERE id=1;
```

---

### Test Case 6: API Testing with cURL

**Register:**
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "email": "apitest@example.com",
    "password": "password123",
    "fullName": "API Test User",
    "role": "STUDENT"
  }'
```

**Login:**
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "apitest@example.com",
    "password": "password123"
  }'
```

**Get Assignments:**
```bash
TOKEN="your_jwt_token_here"
curl -H "Authorization: Bearer $TOKEN" \
  http://localhost:8080/api/assignments
```

**Upload Submission:**
```bash
curl -X POST http://localhost:8080/api/submissions/upload \
  -H "Authorization: Bearer $TOKEN" \
  -F "assignmentId=1" \
  -F "studentId=1" \
  -F "file=@assignment.txt" \
  http://localhost:8080/api/submissions/upload
```

---

## 🧪 Automated Testing Checklist

### Authentication Tests
- [ ] Register new user
- [ ] Login with correct credentials
- [ ] Login with wrong password (should fail)
- [ ] Login with non-existent email (should fail)
- [ ] Access protected endpoint without token (should fail)
- [ ] Access protected endpoint with invalid token (should fail)

### Assignment Tests
- [ ] Create assignment as teacher
- [ ] Create assignment as student (should fail)
- [ ] Get all assignments
- [ ] Get specific assignment
- [ ] Update assignment as owner
- [ ] Update assignment as non-owner (should fail)
- [ ] Delete assignment as owner
- [ ] Delete assignment as non-owner (should fail)

### Submission Tests
- [ ] Upload submission as student
- [ ] Submit same assignment twice (replace old)
- [ ] Upload unsupported file type (should fail)
- [ ] Upload file > 10MB (should fail)
- [ ] View own submissions
- [ ] View others' submissions (should fail unless teacher)

### Plagiarism Tests
- [ ] Check plagiarism on submission
- [ ] Compare identical submissions (should be ~100%)
- [ ] Compare different submissions (should be ~0%)
- [ ] Compare partially similar submissions (20-80%)
- [ ] Get plagiarism result for submission
- [ ] View all plagiarism results for assignment

### Grading Tests
- [ ] Grade submission as teacher
- [ ] Grade submission as student (should fail)
- [ ] View grades as student
- [ ] View all submissions as teacher

---

## 📊 Performance Testing

### Load Test: Create 100 Users
```bash
for i in {1..100}; do
  curl -X POST http://localhost:8080/api/auth/register \
    -H "Content-Type: application/json" \
    -d "{\"email\":\"user$i@test.com\",\"password\":\"pass\",\"fullName\":\"User $i\",\"role\":\"STUDENT\"}"
done
```

### Plagiarism Detection Performance
- Single comparison: < 1 second
- 10 submissions: < 3 seconds
- 50 submissions: < 10 seconds
- 100+ submissions: May need optimization

---

## 🐛 Debugging Tips

### Check Backend Logs
```bash
# View Spring Boot logs
tail -f backend/target/logs/app.log

# Check database connection
mysql -u root -p plagiarism_db -e "SHOW TABLES;"
```

### Check Frontend Logs
```bash
# Open browser DevTools: F12
# Check Console tab for JavaScript errors
# Check Network tab for API calls
```

### Database Inspection
```bash
# Connect to database
mysql -u root -p plagiarism_db

# View tables
SHOW TABLES;
SHOW COLUMNS FROM submissions;

# Sample queries
SELECT * FROM users LIMIT 5;
SELECT COUNT(*) FROM submissions;
SELECT AVG(similarity_score) FROM plagiarism_results;
```

---

**Testing Complete When All Cases Pass** ✅
