import React, { useState } from 'react';
import {
  AppBar,
  Toolbar,
  Typography,
  Button,
  Box,
  Menu,
  MenuItem,
  IconButton,
  Avatar,
  Divider,
  Chip
} from '@mui/material';
import {
  AdminPanelSettings,
  Person,
  Inventory,
  ShoppingCart,
  PersonAdd,
  Logout,
  KeyboardArrowDown
} from '@mui/icons-material';
import { useNavigate, useLocation } from 'react-router-dom';

const AdminNavbar = ({ currentUser, onLogout }) => {
  const [adminMenuAnchor, setAdminMenuAnchor] = useState(null);
  const [pmMenuAnchor, setPmMenuAnchor] = useState(null);
  const [userMenuAnchor, setUserMenuAnchor] = useState(null);
  const navigate = useNavigate();
  const location = useLocation();

  const isAdmin = currentUser?.roles?.includes('ADMIN');
  const isProductManager = currentUser?.roles?.includes('PRODUCT_MANAGER');

  const handleAdminMenuOpen = (event) => {
    setAdminMenuAnchor(event.currentTarget);
  };

  const handleAdminMenuClose = () => {
    setAdminMenuAnchor(null);
  };

  const handlePmMenuOpen = (event) => {
    setPmMenuAnchor(event.currentTarget);
  };

  const handlePmMenuClose = () => {
    setPmMenuAnchor(null);
  };

  const handleUserMenuOpen = (event) => {
    setUserMenuAnchor(event.currentTarget);
  };

  const handleUserMenuClose = () => {
    setUserMenuAnchor(null);
  };

  const handleNavigation = (path) => {
    navigate(path);
    handleAdminMenuClose();
    handlePmMenuClose();
  };

  const handleLogout = () => {
    handleUserMenuClose();
    onLogout();
  };

  const getRoleColor = (role) => {
    switch (role) {
      case 'ADMIN':
        return 'primary';
      case 'PRODUCT_MANAGER':
        return 'secondary';
      default:
        return 'default';
    }
  };

  return (
    <AppBar position="static" elevation={1} sx={{ backgroundColor: 'white', color: 'text.primary' }}>
      <Toolbar>
        {/* Logo/Brand */}
        <Typography 
          variant="h6" 
          component="div" 
          sx={{ 
            flexGrow: 0, 
            mr: 4, 
            fontWeight: 'bold',
            color: 'primary.main',
            cursor: 'pointer'
          }}
          onClick={() => navigate('/')}
        >
          AIMS Admin
        </Typography>

        {/* Navigation Menu */}
        <Box sx={{ flexGrow: 1, display: 'flex', gap: 2 }}>
          {/* Admin Dropdown */}
          {isAdmin && (
            <Box>
              <Button
                color="inherit"
                startIcon={<AdminPanelSettings />}
                endIcon={<KeyboardArrowDown />}
                onClick={handleAdminMenuOpen}
                sx={{
                  textTransform: 'none',
                  fontWeight: 500,
                  '&:hover': { backgroundColor: 'rgba(37, 99, 235, 0.08)' }
                }}
              >
                Admin
              </Button>
              <Menu
                anchorEl={adminMenuAnchor}
                open={Boolean(adminMenuAnchor)}
                onClose={handleAdminMenuClose}
                PaperProps={{
                  sx: {
                    mt: 1,
                    minWidth: 200,
                    boxShadow: '0 4px 20px rgba(0,0,0,0.1)'
                  }
                }}
              >
                <MenuItem onClick={() => handleNavigation('/create-user')}>
                  <PersonAdd sx={{ mr: 2 }} />
                  Create New User
                </MenuItem>
              </Menu>
            </Box>
          )}

          {/* Product Manager Dropdown */}
          {isProductManager && (
            <Box>
              <Button
                color="inherit"
                startIcon={<Inventory />}
                endIcon={<KeyboardArrowDown />}
                onClick={handlePmMenuOpen}
                sx={{
                  textTransform: 'none',
                  fontWeight: 500,
                  '&:hover': { backgroundColor: 'rgba(245, 158, 11, 0.08)' }
                }}
              >
                Product Manager
              </Button>
              <Menu
                anchorEl={pmMenuAnchor}
                open={Boolean(pmMenuAnchor)}
                onClose={handlePmMenuClose}
                PaperProps={{
                  sx: {
                    mt: 1,
                    minWidth: 200,
                    boxShadow: '0 4px 20px rgba(0,0,0,0.1)'
                  }
                }}
              >
                <MenuItem onClick={() => handleNavigation('/products')}>
                  <Inventory sx={{ mr: 2 }} />
                  CRUD Products
                </MenuItem>
                <MenuItem onClick={() => handleNavigation('/orders')}>
                  <ShoppingCart sx={{ mr: 2 }} />
                  Order Management
                </MenuItem>
              </Menu>
            </Box>
          )}
        </Box>

        {/* User Menu */}
        <Box sx={{ display: 'flex', alignItems: 'center', gap: 2 }}>
          {/* Role Chips */}
          <Box sx={{ display: 'flex', gap: 1 }}>
            {currentUser?.roles?.map((role) => (
              <Chip
                key={role}
                label={role}
                color={getRoleColor(role)}
                size="small"
                variant="outlined"
                icon={role === 'ADMIN' ? <AdminPanelSettings /> : <Person />}
              />
            ))}
          </Box>

          {/* User Avatar & Menu */}
          <IconButton
            onClick={handleUserMenuOpen}
            sx={{ 
              border: '2px solid',
              borderColor: 'primary.main',
              '&:hover': { backgroundColor: 'rgba(37, 99, 235, 0.08)' }
            }}
          >
            <Avatar sx={{ width: 32, height: 32, bgcolor: 'primary.main' }}>
              {currentUser?.name?.charAt(0)?.toUpperCase() || 'U'}
            </Avatar>
          </IconButton>
          
          <Menu
            anchorEl={userMenuAnchor}
            open={Boolean(userMenuAnchor)}
            onClose={handleUserMenuClose}
            PaperProps={{
              sx: {
                mt: 1,
                minWidth: 200,
                boxShadow: '0 4px 20px rgba(0,0,0,0.1)'
              }
            }}
          >
            <Box sx={{ px: 2, py: 1 }}>
              <Typography variant="subtitle2" fontWeight="bold">
                {currentUser?.name}
              </Typography>
              <Typography variant="caption" color="text.secondary">
                {currentUser?.email}
              </Typography>
            </Box>
            <Divider />
            <MenuItem onClick={handleLogout}>
              <Logout sx={{ mr: 2 }} />
              Logout
            </MenuItem>
          </Menu>
        </Box>
      </Toolbar>
    </AppBar>
  );
};

export default AdminNavbar; 