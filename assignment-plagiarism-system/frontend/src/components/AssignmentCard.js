import React, { useState } from 'react';
import '../styles/AssignmentCard.css';
import SubmissionModal from './SubmissionModal';

const AssignmentCard = ({ assignment, submissions, isStudent }) => {
  const [showModal, setShowModal] = useState(false);
  const studentSubmission = submissions.find(s => s.assignmentId === assignment.id);

  return (
    <>
      <div className="assignment-card">
        <h3>{assignment.title}</h3>
        <p>{assignment.description}</p>
        <div className="assignment-meta">
          <p><strong>Due Date:</strong> {new Date(assignment.dueDate).toLocaleDateString()}</p>
          <p><strong>Max Score:</strong> {assignment.maxScore}</p>
          <p><strong>Teacher:</strong> {assignment.teacherName}</p>
        </div>

        {isStudent && (
          <div className="student-submission">
            {studentSubmission ? (
              <div className="submitted">
                <p className="status">✓ Submitted: {new Date(studentSubmission.submittedAt).toLocaleDateString()}</p>
                {studentSubmission.plagiarismResult && (
                  <p className="plagiarism">
                    Plagiarism: {studentSubmission.plagiarismResult.similarityScore.toFixed(2)}%
                  </p>
                )}
                {studentSubmission.score && (
                  <p className="score">Score: {studentSubmission.score}/{assignment.maxScore}</p>
                )}
              </div>
            ) : (
              <button onClick={() => setShowModal(true)} className="btn-primary">
                Submit Assignment
              </button>
            )}
          </div>
        )}
      </div>

      {showModal && (
        <SubmissionModal
          assignment={assignment}
          onClose={() => setShowModal(false)}
          onSubmit={() => {
            setShowModal(false);
            // Refresh submissions
          }}
        />
      )}
    </>
  );
};

export default AssignmentCard;
