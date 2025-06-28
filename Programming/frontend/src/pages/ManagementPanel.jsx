import React, { useState, useEffect } from 'react';
import { Routes, Route, Navigate } from 'react-router-dom';
import { Box, Container, Typography, Alert } from '@mui/material';
import ManagementNavbar from '../components/ManagementNavbar';
import CreateUserPage from './admin/CreateUserPage';
import ProductManagementPage from './admin/ProductManagementPage';
import OrderManagementPage from './admin/OrderManagementPage';
import { authService } from '../services/authService';

const ManagementPanel = () => {
  const [currentUser, setCurrentUser] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    const checkAuth = async () => {
      try {
        const user = authService.getCurrentUser();
        if (!user) {
          window.location.href = '/';
          return;
        }
        const hasValidRole = user.roles && (
          user.roles.includes('ADMIN') || 
          user.roles.includes('PRODUCT_MANAGER')
        );
        if (!hasValidRole) {
          window.location.href = '/';
          return;
        }
        setCurrentUser(user);
      } catch (error) {
        setError(error.message);
        window.location.href = '/';
      } finally {
        setLoading(false);
      }
    };
    checkAuth();
  }, []);

  const handleLogout = async () => {
    try {
      await authService.logout();
      window.location.href = '/';
    } catch (error) {
      setError(error.message);
    }
  };

  if (loading) {
    return (
      <Box sx={{ display: 'flex', justifyContent: 'center', alignItems: 'center', height: '100vh' }}>
        <Typography>Loading...</Typography>
      </Box>
    );
  }
  if (!currentUser) return null;

  return (
    <Box sx={{ minHeight: '100vh', backgroundColor: '#f8fafc' }}>
      <ManagementNavbar currentUser={currentUser} onLogout={handleLogout} />
      {error && <Alert severity="error" sx={{ m: 2 }}>{error}</Alert>}
      <Container maxWidth="xl" sx={{ mt: 2, mb: 4 }}>
        <Routes>
          <Route 
            path="/" 
            element={
              <Box sx={{ p: 3, backgroundColor: 'white', borderRadius: 2, boxShadow: 1 }}>
                <Typography variant="h4" gutterBottom>
                  Welcome to Management Panel
                </Typography>
                <Typography variant="body1" color="text.secondary" paragraph>
                  Hello <strong>{currentUser.name}</strong>! You are logged in as: {currentUser.roles?.join(', ')}
                </Typography>
                <Alert severity="info" sx={{ mt: 2 }}>
                  Use the navigation bar above to access different management features for Admin and Product Manager.
                </Alert>
              </Box>
            } 
          />
          {currentUser.roles?.includes('ADMIN') && (
            <Route path="/create-user" element={<CreateUserPage />} />
          )}
          {currentUser.roles?.includes('PRODUCT_MANAGER') && (
            <>
              <Route path="/products" element={<ProductManagementPage />} />
              <Route path="/orders" element={<OrderManagementPage />} />
            </>
          )}
          <Route path="*" element={<Navigate to="/" replace />} />
        </Routes>
      </Container>
    </Box>
  );
};

export default ManagementPanel; 