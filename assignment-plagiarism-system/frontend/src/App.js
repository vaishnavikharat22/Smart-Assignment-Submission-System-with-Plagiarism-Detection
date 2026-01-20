import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import { AuthProvider, useAuth } from './context/AuthContext';
import Login from './pages/Login';
import Register from './pages/Register';
import StudentDashboard from './pages/StudentDashboard';
import TeacherDashboard from './pages/TeacherDashboard';
import Navigation from './components/Navigation';
import './styles/App.css';

const ProtectedRoute = ({ element, requiredRole }) => {
  const { user, loading } = useAuth();

  if (loading) return <div>Loading...</div>;

  if (!user) {
    return <Navigate to="/login" />;
  }

  if (requiredRole && user.role !== requiredRole) {
    return <Navigate to="/dashboard" />;
  }

  return element;
};

const DashboardRouter = () => {
  const { user, loading } = useAuth();

  if (loading) return <div>Loading...</div>;

  if (user?.role === 'STUDENT') {
    return <StudentDashboard />;
  } else if (user?.role === 'TEACHER') {
    return <TeacherDashboard />;
  } else {
    return <div>Dashboard not available for this role</div>;
  }
};

function App() {
  return (
    <Router>
      <AuthProvider>
        <Navigation />
        <Routes>
          <Route path="/login" element={<Login />} />
          <Route path="/register" element={<Register />} />
          <Route
            path="/dashboard"
            element={<ProtectedRoute element={<DashboardRouter />} />}
          />
          <Route path="/" element={<Navigate to="/dashboard" />} />
        </Routes>
      </AuthProvider>
    </Router>
  );
}

export default App;
