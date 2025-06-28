import React, { useState } from 'react';
import { 
  CircularProgress, 
  InputAdornment, 
  IconButton,
  Link,
  Alert,
  Chip,
  Box
} from '@mui/material';
import { 
  Visibility, 
  VisibilityOff,
  Email,
  Lock,
  Login
} from '@mui/icons-material';
import { useNavigate } from 'react-router-dom';
import { authService } from '../services/authService';
import {
  LoginContainer,
  LoginCard,
  LogoContainer,
  LogoText,
  WelcomeText,
  StyledTextField,
  LoginButton,
  ErrorMessage,
  SuccessMessage,
  LoadingOverlay
} from '../styles/LoginPage.styles';

const LoginPage = ({ onLoginSuccess, onBackToHome }) => {
  const [formData, setFormData] = useState({
    email: '',
    password: ''
  });
  const [showPassword, setShowPassword] = useState(false);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');
  const [userInfo, setUserInfo] = useState(null);
  const navigate = useNavigate();

  const handleInputChange = (field) => (event) => {
    setFormData(prev => ({
      ...prev,
      [field]: event.target.value
    }));
    // Clear error when user starts typing
    if (error) setError('');
  };

  const handleTogglePasswordVisibility = () => {
    setShowPassword(!showPassword);
  };

  const validateForm = () => {
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
    return true;
  };

  const handleSubmit = async (event) => {
    event.preventDefault();
    
    if (!validateForm()) return;

    setLoading(true);
    setError('');
    setSuccess('');
    setUserInfo(null);

    try {
      const response = await authService.login(formData.email, formData.password);
      
      if (response.success) {
        setUserInfo(response.userInfo);
        setSuccess('Login successful! Redirecting...');
        
        // Redirect to management panel after 2 seconds
        setTimeout(() => {
          navigate('/management');
        }, 2000);
      } else {
        setError(response.message || 'Login failed');
      }
    } catch (error) {
      // Handle different types of errors
      let errorMessage = 'Login failed';
      
      if (error.message.includes('401') || error.message.includes('Unauthorized')) {
        errorMessage = 'Invalid email or password';
      } else if (error.message.includes('404') || error.message.includes('Not Found')) {
        errorMessage = 'Account not found';
      } else if (error.message.includes('403') || error.message.includes('Forbidden')) {
        errorMessage = 'Account does not have access permission';
      } else if (error.message.includes('500') || error.message.includes('Internal Server Error')) {
        errorMessage = 'System error, please try again later';
      } else if (error.message.includes('Network Error') || error.message.includes('timeout')) {
        errorMessage = 'Cannot connect to server, please check your network connection';
      } else {
        errorMessage = error.message || 'Login failed';
      }
      
      setError(errorMessage);
    } finally {
      setLoading(false);
    }
  };

  const handleKeyPress = (event) => {
    if (event.key === 'Enter') {
      handleSubmit(event);
    }
  };

  return (
    <LoginContainer>
      <LoginCard>
        {loading && (
          <LoadingOverlay>
            <CircularProgress size={40} />
          </LoadingOverlay>
        )}
        
        <LogoContainer>
          <LogoText>AIMS</LogoText>
          <Box sx={{ display: 'flex', alignItems: 'center', gap: 1, mt: 1 }}>
            <Chip 
              icon={<Login />} 
              label="Login" 
              color="primary" 
              size="small"
              variant="outlined"
            />
          </Box>
        </LogoContainer>
        
        <WelcomeText>
          Welcome back! Please sign in to access the system
        </WelcomeText>

        <form onSubmit={handleSubmit} style={{ width: '100%' }}>
          <StyledTextField
            label="Email"
            type="email"
            value={formData.email}
            onChange={handleInputChange('email')}
            onKeyPress={handleKeyPress}
            disabled={loading}
            InputProps={{
              startAdornment: (
                <InputAdornment position="start">
                  <Email color="action" />
                </InputAdornment>
              ),
            }}
            placeholder="Enter your email"
          />

          <StyledTextField
            label="Password"
            type={showPassword ? 'text' : 'password'}
            value={formData.password}
            onChange={handleInputChange('password')}
            onKeyPress={handleKeyPress}
            disabled={loading}
            InputProps={{
              startAdornment: (
                <InputAdornment position="start">
                  <Lock color="action" />
                </InputAdornment>
              ),
              endAdornment: (
                <InputAdornment position="end">
                  <IconButton
                    onClick={handleTogglePasswordVisibility}
                    edge="end"
                    disabled={loading}
                  >
                    {showPassword ? <VisibilityOff /> : <Visibility />}
                  </IconButton>
                </InputAdornment>
              ),
            }}
            placeholder="Enter your password"
          />

          {error && (
            <ErrorMessage>
              {error}
            </ErrorMessage>
          )}

          {success && (
            <SuccessMessage>
              {success}
              {userInfo && (
                <>
                  <br />
                  Welcome <b>{userInfo.name}</b>!<br />
                  Role: <b>{userInfo.roles?.join(', ')}</b>
                </>
              )}
            </SuccessMessage>
          )}

          <LoginButton
            type="submit"
            variant="contained"
            disabled={loading}
            onClick={handleSubmit}
          >
            {loading ? (
              <CircularProgress size={20} color="inherit" />
            ) : (
              'Log in'
            )}
          </LoginButton>
        </form>

        <Link
          component="button"
          variant="body2"
          onClick={onBackToHome}
          sx={{
            color: 'primary.main',
            textDecoration: 'none',
            cursor: 'pointer',
            '&:hover': {
              textDecoration: 'underline',
            },
            mt: 2
          }}
        >
          Back to Home
        </Link>
      </LoginCard>
    </LoginContainer>
  );
};

export default LoginPage; 