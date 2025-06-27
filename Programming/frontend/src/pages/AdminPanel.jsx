import React, { useState, useEffect } from 'react';
import { Routes, Route, Navigate } from 'react-router-dom';
import { Box, Container, Typography, Alert } from '@mui/material';
import AdminNavbar from '../components/AdminNavbar';
import CreateUserPage from './admin/CreateUserPage';
import ProductManagementPage from './admin/ProductManagementPage';
import OrderManagementPage from './admin/OrderManagementPage';
import { authService } from '../services/authService';

const AdminPanel = () => {
  const [currentUser, setCurrentUser] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const checkAuth = async () => {
      try {
        const user = authService.getCurrentUser();
        if (!user) {
          // Redirect to login if no user
          window.location.href = '/';
          return;
        }

        // Check if user has admin or product manager role
        const hasValidRole = user.roles && (
          user.roles.includes('ADMIN') || 
          user.roles.includes('PRODUCT_MANAGER')
        );

        if (!hasValidRole) {
          // Redirect if user doesn't have required roles
          window.location.href = '/';
          return;
        }

        setCurrentUser(user);
      } catch (error) {
        console.error('Auth check error:', error);
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
      console.error('Logout error:', error);
    }
  };

  if (loading) {
    return (
      <Box sx={{ display: 'flex', justifyContent: 'center', alignItems: 'center', height: '100vh' }}>
        <Typography>Loading...</Typography>
      </Box>
    );
  }

  if (!currentUser) {
    return null;
  }

  return (
    <Box sx={{ minHeight: '100vh', backgroundColor: '#f8fafc' }}>
      <AdminNavbar currentUser={currentUser} onLogout={handleLogout} />
      
      <Container maxWidth="xl" sx={{ mt: 2, mb: 4 }}>
        <Routes>
          <Route 
            path="/" 
            element={
              <Box sx={{ p: 3, backgroundColor: 'white', borderRadius: 2, boxShadow: 1 }}>
                <Typography variant="h4" gutterBottom>
                  Welcome to Admin Panel
                </Typography>
                <Typography variant="body1" color="text.secondary" paragraph>
                  Hello <strong>{currentUser.name}</strong>! You are logged in as: {currentUser.roles?.join(', ')}
                </Typography>
                <Alert severity="info" sx={{ mt: 2 }}>
                  Use the navigation bar above to access different admin features.
                </Alert>
              </Box>
            } 
          />
          
          {/* Admin Routes */}
          {currentUser.roles?.includes('ADMIN') && (
            <Route path="/create-user" element={<CreateUserPage />} />
          )}
          
          {/* Product Manager Routes */}
          {currentUser.roles?.includes('PRODUCT_MANAGER') && (
            <>
              <Route path="/products" element={<ProductManagementPage />} />
              <Route path="/orders" element={<OrderManagementPage />} />
            </>
          )}
          
          {/* Fallback route */}
          <Route path="*" element={<Navigate to="/" replace />} />
        </Routes>
      </Container>
    </Box>
  );
};

export default AdminPanel; 