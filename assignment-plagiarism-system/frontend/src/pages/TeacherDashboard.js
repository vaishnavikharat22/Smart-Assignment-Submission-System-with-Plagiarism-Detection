import React, { useState, useEffect } from 'react';
import { useAuth } from '../context/AuthContext';
import { assignmentService, submissionService } from '../services/api';
import '../styles/Dashboard.css';

const TeacherDashboard = () => {
  const { user } = useAuth();
  const [assignments, setAssignments] = useState([]);
  const [selectedAssignment, setSelectedAssignment] = useState(null);
  const [submissions, setSubmissions] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [newAssignment, setNewAssignment] = useState({
    title: '',
    description: '',
    dueDate: '',
    maxScore: 100
  });

  useEffect(() => {
    loadAssignments();
  }, [user]);

  const loadAssignments = async () => {
    try {
      setLoading(true);
      const res = await assignmentService.getByTeacher(user?.id);
      setAssignments(res.data);
    } catch (err) {
      setError('Failed to load assignments');
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  const handleCreateAssignment = async (e) => {
    e.preventDefault();
    try {
      await assignmentService.create(
        newAssignment.title,
        newAssignment.description,
        user.id,
        newAssignment.dueDate,
        newAssignment.maxScore
      );
      setNewAssignment({ title: '', description: '', dueDate: '', maxScore: 100 });
      loadAssignments();
    } catch (err) {
      setError('Failed to create assignment');
      console.error(err);
    }
  };

  const loadSubmissions = async (assignmentId) => {
    try {
      const res = await submissionService.getByAssignment(assignmentId);
      setSubmissions(res.data);
      setSelectedAssignment(assignmentId);
    } catch (err) {
      console.error(err);
    }
  };

  const checkPlagiarism = async (submissionId) => {
    try {
      await submissionService.checkPlagiarism(submissionId);
      loadSubmissions(selectedAssignment);
    } catch (err) {
      setError('Failed to check plagiarism');
      console.error(err);
    }
  };

  if (loading) return <div className="dashboard">Loading...</div>;

  return (
    <div className="dashboard">
      <h1>Teacher Dashboard - {user?.fullName}</h1>

      {error && <div className="error-message">{error}</div>}

      <section className="create-assignment">
        <h2>Create Assignment</h2>
        <form onSubmit={handleCreateAssignment}>
          <div className="form-group">
            <label>Title:</label>
            <input
              type="text"
              value={newAssignment.title}
              onChange={(e) => setNewAssignment({ ...newAssignment, title: e.target.value })}
              required
            />
          </div>
          <div className="form-group">
            <label>Description:</label>
            <textarea
              value={newAssignment.description}
              onChange={(e) => setNewAssignment({ ...newAssignment, description: e.target.value })}
            />
          </div>
          <div className="form-group">
            <label>Due Date:</label>
            <input
              type="datetime-local"
              value={newAssignment.dueDate}
              onChange={(e) => setNewAssignment({ ...newAssignment, dueDate: e.target.value })}
              required
            />
          </div>
          <div className="form-group">
            <label>Max Score:</label>
            <input
              type="number"
              value={newAssignment.maxScore}
              onChange={(e) => setNewAssignment({ ...newAssignment, maxScore: parseInt(e.target.value) })}
            />
          </div>
          <button type="submit">Create Assignment</button>
        </form>
      </section>

      <section>
        <h2>Your Assignments</h2>
        <div className="assignments-list">
          {assignments.map(assignment => (
            <div key={assignment.id} className="assignment-item">
              <h3>{assignment.title}</h3>
              <p>{assignment.description}</p>
              <p>Due: {new Date(assignment.dueDate).toLocaleDateString()}</p>
              <button onClick={() => loadSubmissions(assignment.id)}>
                View Submissions ({assignment.id})
              </button>
            </div>
          ))}
        </div>
      </section>

      {selectedAssignment && (
        <section>
          <h2>Submissions for Assignment {selectedAssignment}</h2>
          <table className="submissions-table">
            <thead>
              <tr>
                <th>Student</th>
                <th>File</th>
                <th>Status</th>
                <th>Plagiarism Score</th>
                <th>Score</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              {submissions.map(submission => (
                <tr key={submission.id}>
                  <td>{submission.studentName}</td>
                  <td>{submission.fileName}</td>
                  <td>{submission.status}</td>
                  <td>
                    {submission.plagiarismResult?.similarityScore?.toFixed(2)}% 
                    {(!submission.plagiarismResult) && (
                      <button 
                        onClick={() => checkPlagiarism(submission.id)}
                        className="btn-small"
                      >
                        Check
                      </button>
                    )}
                  </td>
                  <td>{submission.score || '-'}</td>
                  <td>
                    <button className="btn-small">Grade</button>
                    <button className="btn-small">View</button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </section>
      )}
    </div>
  );
};

export default TeacherDashboard;
