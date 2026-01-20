import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080/api';

// Auth Service
export const authService = {
  register: (email, password, fullName, role = 'STUDENT') =>
    axios.post(`${API_BASE_URL}/auth/register`, { email, password, fullName, role }),

  login: (email, password) =>
    axios.post(`${API_BASE_URL}/auth/login`, { email, password }),

  getCurrentUser: () => {
    const token = localStorage.getItem('token');
    return axios.get(`${API_BASE_URL}/auth/me`, {
      headers: { Authorization: `Bearer ${token}` }
    });
  }
};

// Assignment Service
export const assignmentService = {
  getAll: () => axios.get(`${API_BASE_URL}/assignments`),

  getById: (id) => axios.get(`${API_BASE_URL}/assignments/${id}`),

  getByTeacher: (teacherId) =>
    axios.get(`${API_BASE_URL}/assignments/teacher/${teacherId}`),

  create: (title, description, teacherId, dueDate, maxScore) => {
    const token = localStorage.getItem('token');
    return axios.post(`${API_BASE_URL}/assignments`, {
      title, description, teacherId, dueDate, maxScore
    }, {
      headers: { Authorization: `Bearer ${token}` }
    });
  },

  update: (id, title, description, dueDate, maxScore) => {
    const token = localStorage.getItem('token');
    return axios.put(`${API_BASE_URL}/assignments/${id}`, {
      title, description, dueDate, maxScore
    }, {
      headers: { Authorization: `Bearer ${token}` }
    });
  },

  delete: (id) => {
    const token = localStorage.getItem('token');
    return axios.delete(`${API_BASE_URL}/assignments/${id}`, {
      headers: { Authorization: `Bearer ${token}` }
    });
  }
};

// Submission Service
export const submissionService = {
  upload: (assignmentId, studentId, file) => {
    const formData = new FormData();
    formData.append('assignmentId', assignmentId);
    formData.append('studentId', studentId);
    formData.append('file', file);

    const token = localStorage.getItem('token');
    // Let the browser/axios set the Content-Type (including multipart boundary)
    return axios.post(`${API_BASE_URL}/submissions/upload`, formData, {
      headers: {
        Authorization: `Bearer ${token}`
      }
    });
  },

  getById: (id) => axios.get(`${API_BASE_URL}/submissions/${id}`),

  getByAssignment: (assignmentId) =>
    axios.get(`${API_BASE_URL}/submissions/assignment/${assignmentId}`),

  getByStudent: (studentId) =>
    axios.get(`${API_BASE_URL}/submissions/student/${studentId}`),

  grade: (submissionId, score, feedback) => {
    const token = localStorage.getItem('token');
    return axios.post(`${API_BASE_URL}/submissions/${submissionId}/grade`, {
      score, feedback
    }, {
      headers: { Authorization: `Bearer ${token}` }
    });
  },

  checkPlagiarism: (submissionId) => {
    const token = localStorage.getItem('token');
    return axios.post(`${API_BASE_URL}/submissions/${submissionId}/check-plagiarism`, {}, {
      headers: { Authorization: `Bearer ${token}` }
    });
  },

  getPlagiarismResult: (submissionId) =>
    axios.get(`${API_BASE_URL}/submissions/${submissionId}/plagiarism`),

  getPlagiarismResultsForAssignment: (assignmentId) => {
    const token = localStorage.getItem('token');
    return axios.get(`${API_BASE_URL}/submissions/assignment/${assignmentId}/plagiarism-results`, {
      headers: { Authorization: `Bearer ${token}` }
    });
  }
};

// User Service
export const userService = {
  getAll: () => {
    const token = localStorage.getItem('token');
    return axios.get(`${API_BASE_URL}/users`, {
      headers: { Authorization: `Bearer ${token}` }
    });
  },

  getById: (id) => axios.get(`${API_BASE_URL}/users/${id}`),

  getByRole: (role) => {
    const token = localStorage.getItem('token');
    return axios.get(`${API_BASE_URL}/users/role/${role}`, {
      headers: { Authorization: `Bearer ${token}` }
    });
  },

  disable: (userId) => {
    const token = localStorage.getItem('token');
    return axios.put(`${API_BASE_URL}/users/${userId}/disable`, {}, {
      headers: { Authorization: `Bearer ${token}` }
    });
  },

  enable: (userId) => {
    const token = localStorage.getItem('token');
    return axios.put(`${API_BASE_URL}/users/${userId}/enable`, {}, {
      headers: { Authorization: `Bearer ${token}` }
    });
  },

  delete: (userId) => {
    const token = localStorage.getItem('token');
    return axios.delete(`${API_BASE_URL}/users/${userId}`, {
      headers: { Authorization: `Bearer ${token}` }
    });
  }
};

export default {
  authService,
  assignmentService,
  submissionService,
  userService
};
