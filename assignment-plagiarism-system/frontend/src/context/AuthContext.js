import React, { createContext, useState, useContext, useEffect } from 'react';
import { authService } from '../services/api';

const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);
  const [token, setToken] = useState(localStorage.getItem('token'));

  useEffect(() => {
    if (token) {
      authService.getCurrentUser()
        .then(response => {
          setUser(response.data);
          setLoading(false);
        })
        .catch(error => {
          console.error('Failed to fetch user:', error);
          localStorage.removeItem('token');
          setToken(null);
          setLoading(false);
        });
    } else {
      setLoading(false);
    }
  }, [token]);

  const login = (email, password) => {
    return authService.login(email, password)
      .then(response => {
        localStorage.setItem('token', response.data.token);
        setToken(response.data.token);
        setUser({
          id: response.data.id,
          email: response.data.email,
          fullName: response.data.fullName,
          role: response.data.role
        });
        return response.data;
      });
  };

  const register = (email, password, fullName, role) => {
    return authService.register(email, password, fullName, role)
      .then(response => {
        localStorage.setItem('token', response.data.token);
        setToken(response.data.token);
        setUser({
          id: response.data.id,
          email: response.data.email,
          fullName: response.data.fullName,
          role: response.data.role
        });
        return response.data;
      });
  };

  const logout = () => {
    localStorage.removeItem('token');
    setToken(null);
    setUser(null);
  };

  return (
    <AuthContext.Provider value={{ user, loading, token, login, register, logout }}>
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return context;
};
