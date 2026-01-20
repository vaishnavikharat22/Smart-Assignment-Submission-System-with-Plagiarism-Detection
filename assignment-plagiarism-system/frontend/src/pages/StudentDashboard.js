import React, { useState, useEffect } from 'react';
import { useAuth } from '../context/AuthContext';
import { assignmentService, submissionService } from '../services/api';
import AssignmentCard from '../components/AssignmentCard';
import '../styles/Dashboard.css';

const StudentDashboard = () => {
  const { user } = useAuth();
  const [assignments, setAssignments] = useState([]);
  const [submissions, setSubmissions] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    loadData();
  }, [user]);

  const loadData = async () => {
    try {
      setLoading(true);
      const [assignRes, subRes] = await Promise.all([
        assignmentService.getAll(),
        user?.id ? submissionService.getByStudent(user.id) : Promise.resolve({ data: [] })
      ]);
      setAssignments(assignRes.data);
      setSubmissions(subRes.data);
    } catch (err) {
      setError('Failed to load data');
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  if (loading) return <div className="dashboard">Loading...</div>;

  return (
    <div className="dashboard">
      <h1>Welcome, {user?.fullName}</h1>
      
      {error && <div className="error-message">{error}</div>}

      <section>
        <h2>Available Assignments</h2>
        <div className="assignments-grid">
          {assignments.map(assignment => (
            <AssignmentCard 
              key={assignment.id} 
              assignment={assignment}
              submissions={submissions}
              isStudent={true}
            />
          ))}
        </div>
      </section>

      <section>
        <h2>My Submissions</h2>
        <table className="submissions-table">
          <thead>
            <tr>
              <th>Assignment</th>
              <th>Submitted</th>
              <th>Status</th>
              <th>Score</th>
              <th>Plagiarism Score</th>
            </tr>
          </thead>
          <tbody>
            {submissions.map(submission => (
              <tr key={submission.id}>
                <td>{submission.assignmentId}</td>
                <td>{new Date(submission.submittedAt).toLocaleDateString()}</td>
                <td>{submission.status}</td>
                <td>{submission.score || '-'}</td>
                <td>{submission.plagiarismResult?.similarityScore?.toFixed(2)}% || '-'}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </section>
    </div>
  );
};

export default StudentDashboard;
