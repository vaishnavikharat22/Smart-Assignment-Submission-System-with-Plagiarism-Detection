import React, { useState } from 'react';
import { useAuth } from '../context/AuthContext';
import { submissionService } from '../services/api';
import '../styles/Modal.css';

const SubmissionModal = ({ assignment, onClose, onSubmit }) => {
  const { user } = useAuth();
  const [file, setFile] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  const handleFileChange = (e) => {
    setFile(e.target.files[0]);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!file) {
      setError('Please select a file');
      return;
    }

    try {
      setLoading(true);
      await submissionService.upload(assignment.id, user.id, file);
      setError('');
      onSubmit();
    } catch (err) {
      setError(err.response?.data || 'Upload failed');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="modal-overlay">
      <div className="modal">
        <button className="close-btn" onClick={onClose}>×</button>
        <h2>Submit Assignment: {assignment.title}</h2>
        
        {error && <div className="error-message">{error}</div>}
        
        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <label>Select File (PDF, DOC, TXT):</label>
            <input
              type="file"
              onChange={handleFileChange}
              accept=".pdf,.doc,.docx,.txt"
              required
            />
          </div>
          <button type="submit" disabled={loading}>
            {loading ? 'Uploading...' : 'Submit'}
          </button>
          <button type="button" onClick={onClose}>Cancel</button>
        </form>
      </div>
    </div>
  );
};

export default SubmissionModal;
