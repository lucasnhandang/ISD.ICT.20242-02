import React, { useState } from 'react';
import {
  Box,
  Typography,
  Paper,
  TextField,
  Button,
  Grid,
  FormControl,
  InputLabel,
  Select,
  MenuItem,
  Chip,
  Alert,
  CircularProgress,
  Card,
  CardContent
} from '@mui/material';
import { PersonAdd, Save, Clear } from '@mui/icons-material';

const CreateUserPage = () => {
  const [formData, setFormData] = useState({
    name: '',
    email: '',
    password: '',
    phoneNumber: '',
    roles: []
  });
  const [loading, setLoading] = useState(false);
  const [success, setSuccess] = useState('');
  const [error, setError] = useState('');

  const availableRoles = [
    { value: 'ADMIN', label: 'Admin' },
    { value: 'PRODUCT_MANAGER', label: 'Product Manager' }
  ];

  const handleInputChange = (field) => (event) => {
    setFormData(prev => ({
      ...prev,
      [field]: event.target.value
    }));
    // Clear messages when user starts typing
    if (success) setSuccess('');
    if (error) setError('');
  };

  const handleRoleChange = (event) => {
    setFormData(prev => ({
      ...prev,
      roles: event.target.value
    }));
  };

  const validateForm = () => {
    if (!formData.name.trim()) {
      setError('Name is required');
      return false;
    }
    if (!formData.email.trim()) {
      setError('Email is required');
      return false;
    }
    if (!formData.password.trim()) {
      setError('Password is required');
      return false;
    }
    if (formData.password.length < 5) {
      setError('Password must be at least 5 characters');
      return false;
    }
    if (formData.roles.length === 0) {
      setError('At least one role must be selected');
      return false;
    }
    return true;
  };

  const handleSubmit = async (event) => {
    event.preventDefault();
    
    if (!validateForm()) return;

    setLoading(true);
    setError('');
    setSuccess('');

    try {
      // TODO: Implement API call to create user
      // const response = await api.post('/admin/users', formData);
      
      // Simulate API call
      await new Promise(resolve => setTimeout(resolve, 1000));
      
      setSuccess('User created successfully!');
      setFormData({
        name: '',
        email: '',
        password: '',
        phoneNumber: '',
        roles: []
      });
    } catch (error) {
      console.error('Create user error:', error);
      setError(error.response?.data?.message || 'Failed to create user');
    } finally {
      setLoading(false);
    }
  };

  const handleReset = () => {
    setFormData({
      name: '',
      email: '',
      password: '',
      phoneNumber: '',
      roles: []
    });
    setError('');
    setSuccess('');
  };

  return (
    <Box>
      <Typography variant="h4" gutterBottom sx={{ mb: 3 }}>
        Create New User
      </Typography>

      <Grid container spacing={3}>
        <Grid item xs={12} md={8}>
          <Paper elevation={2} sx={{ p: 3 }}>
            <form onSubmit={handleSubmit}>
              <Grid container spacing={3}>
                <Grid item xs={12} sm={6}>
                  <TextField
                    fullWidth
                    label="Full Name"
                    value={formData.name}
                    onChange={handleInputChange('name')}
                    required
                    disabled={loading}
                  />
                </Grid>
                
                <Grid item xs={12} sm={6}>
                  <TextField
                    fullWidth
                    label="Email"
                    type="email"
                    value={formData.email}
                    onChange={handleInputChange('email')}
                    required
                    disabled={loading}
                  />
                </Grid>
                
                <Grid item xs={12} sm={6}>
                  <TextField
                    fullWidth
                    label="Password"
                    type="password"
                    value={formData.password}
                    onChange={handleInputChange('password')}
                    required
                    disabled={loading}
                    helperText="Minimum 5 characters"
                  />
                </Grid>
                
                <Grid item xs={12} sm={6}>
                  <TextField
                    fullWidth
                    label="Phone Number"
                    value={formData.phoneNumber}
                    onChange={handleInputChange('phoneNumber')}
                    disabled={loading}
                  />
                </Grid>
                
                <Grid item xs={12}>
                  <FormControl fullWidth required>
                    <InputLabel>Roles</InputLabel>
                    <Select
                      multiple
                      value={formData.roles}
                      onChange={handleRoleChange}
                      label="Roles"
                      disabled={loading}
                      renderValue={(selected) => (
                        <Box sx={{ display: 'flex', flexWrap: 'wrap', gap: 0.5 }}>
                          {selected.map((value) => (
                            <Chip 
                              key={value} 
                              label={availableRoles.find(role => role.value === value)?.label || value}
                              color={value === 'ADMIN' ? 'primary' : 'secondary'}
                              size="small"
                            />
                          ))}
                        </Box>
                      )}
                    >
                      {availableRoles.map((role) => (
                        <MenuItem key={role.value} value={role.value}>
                          {role.label}
                        </MenuItem>
                      ))}
                    </Select>
                  </FormControl>
                </Grid>
                
                <Grid item xs={12}>
                  <Box sx={{ display: 'flex', gap: 2, justifyContent: 'flex-end' }}>
                    <Button
                      variant="outlined"
                      startIcon={<Clear />}
                      onClick={handleReset}
                      disabled={loading}
                    >
                      Reset
                    </Button>
                    <Button
                      type="submit"
                      variant="contained"
                      startIcon={loading ? <CircularProgress size={20} /> : <Save />}
                      disabled={loading}
                    >
                      {loading ? 'Creating...' : 'Create User'}
                    </Button>
                  </Box>
                </Grid>
              </Grid>
            </form>
          </Paper>
        </Grid>
        
        <Grid item xs={12} md={4}>
          <Card>
            <CardContent>
              <Typography variant="h6" gutterBottom>
                <PersonAdd sx={{ mr: 1, verticalAlign: 'middle' }} />
                User Creation Guide
              </Typography>
              <Typography variant="body2" color="text.secondary" paragraph>
                Create new users for the AIMS system. Each user must have at least one role assigned.
              </Typography>
              
              <Typography variant="subtitle2" gutterBottom>
                Available Roles:
              </Typography>
              <Box sx={{ display: 'flex', flexDirection: 'column', gap: 1 }}>
                <Chip 
                  label="Admin" 
                  color="primary" 
                  size="small" 
                  variant="outlined"
                />
                <Typography variant="caption" color="text.secondary">
                  Full system access, can create users and manage all features
                </Typography>
                
                <Chip 
                  label="Product Manager" 
                  color="secondary" 
                  size="small" 
                  variant="outlined"
                />
                <Typography variant="caption" color="text.secondary">
                  Can manage products and orders
                </Typography>
              </Box>
            </CardContent>
          </Card>
        </Grid>
      </Grid>

      {/* Messages */}
      {error && (
        <Alert severity="error" sx={{ mt: 2 }}>
          {error}
        </Alert>
      )}
      
      {success && (
        <Alert severity="success" sx={{ mt: 2 }}>
          {success}
        </Alert>
      )}
    </Box>
  );
};

export default CreateUserPage; 