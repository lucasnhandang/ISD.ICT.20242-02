import { styled } from '@mui/material/styles';
import { Paper, TextField, Button, Typography, Box } from '@mui/material';

export const LoginContainer = styled(Box)(({ theme }) => ({
  minHeight: '100vh',
  display: 'flex',
  alignItems: 'center',
  justifyContent: 'center',
  background: 'linear-gradient(135deg, #f8fafc 0%, #e2e8f0 100%)',
  padding: theme.spacing(2),
}));

export const LoginCard = styled(Paper)(({ theme }) => ({
  padding: theme.spacing(4),
  display: 'flex',
  flexDirection: 'column',
  alignItems: 'center',
  maxWidth: 400,
  width: '100%',
  borderRadius: 16,
  boxShadow: '0 10px 40px rgba(0, 0, 0, 0.1)',
  border: '1px solid rgba(37, 99, 235, 0.1)',
  background: 'rgba(255, 255, 255, 0.95)',
  backdropFilter: 'blur(10px)',
  position: 'relative',
  overflow: 'hidden',
  
  '&::before': {
    content: '""',
    position: 'absolute',
    top: 0,
    left: 0,
    right: 0,
    height: '4px',
    background: 'linear-gradient(90deg, #2563eb 0%, #60a5fa 100%)',
  },
}));

export const LogoContainer = styled(Box)(({ theme }) => ({
  marginBottom: theme.spacing(3),
  textAlign: 'center',
}));

export const LogoText = styled(Typography)(({ theme }) => ({
  background: 'linear-gradient(45deg, #2563eb 30%, #60a5fa 90%)',
  WebkitBackgroundClip: 'text',
  WebkitTextFillColor: 'transparent',
  fontWeight: 700,
  fontSize: '2rem',
  letterSpacing: '-0.5px',
  fontFamily: '"Be Vietnam Pro", sans-serif',
}));

export const WelcomeText = styled(Typography)(({ theme }) => ({
  color: theme.palette.text.secondary,
  marginBottom: theme.spacing(3),
  textAlign: 'center',
  fontSize: '1rem',
}));

export const StyledTextField = styled(TextField)(({ theme }) => ({
  width: '100%',
  marginBottom: theme.spacing(2),
  
  '& .MuiOutlinedInput-root': {
    borderRadius: 12,
    backgroundColor: theme.palette.background.paper,
    border: `1px solid ${theme.palette.divider}`,
    transition: 'all 0.2s ease',
    
    '&:hover': {
      borderColor: theme.palette.primary.light,
      boxShadow: `0 0 0 3px ${theme.palette.primary.light}20`,
    },
    
    '&.Mui-focused': {
      borderColor: theme.palette.primary.main,
      boxShadow: `0 0 0 3px ${theme.palette.primary.main}20`,
    },
  },
  
  '& .MuiInputLabel-root': {
    color: theme.palette.text.secondary,
    fontWeight: 500,
  },
  
  '& .MuiInputBase-input': {
    padding: theme.spacing(1.5, 2),
    fontSize: '1rem',
  },
}));

export const LoginButton = styled(Button)(({ theme }) => ({
  width: '100%',
  padding: theme.spacing(1.5, 3),
  marginTop: theme.spacing(2),
  marginBottom: theme.spacing(2),
  borderRadius: 12,
  fontWeight: 600,
  fontSize: '1rem',
  textTransform: 'none',
  background: 'linear-gradient(45deg, #2563eb 30%, #60a5fa 90%)',
  boxShadow: '0 4px 15px rgba(37, 99, 235, 0.3)',
  transition: 'all 0.3s ease',
  
  '&:hover': {
    background: 'linear-gradient(45deg, #1d4ed8 30%, #3b82f6 90%)',
    boxShadow: '0 6px 20px rgba(37, 99, 235, 0.4)',
    transform: 'translateY(-2px)',
  },
  
  '&:disabled': {
    background: theme.palette.grey[300],
    boxShadow: 'none',
    transform: 'none',
  },
}));

export const ErrorMessage = styled(Typography)(({ theme }) => ({
  color: theme.palette.error.main,
  fontSize: '0.875rem',
  marginTop: theme.spacing(1),
  textAlign: 'center',
  backgroundColor: theme.palette.error.light + '20',
  padding: theme.spacing(1, 2),
  borderRadius: 8,
  border: `1px solid ${theme.palette.error.light}`,
}));

export const SuccessMessage = styled(Typography)(({ theme }) => ({
  color: theme.palette.success.main,
  fontSize: '0.875rem',
  marginTop: theme.spacing(1),
  textAlign: 'center',
  backgroundColor: theme.palette.success.light + '20',
  padding: theme.spacing(1, 2),
  borderRadius: 8,
  border: `1px solid ${theme.palette.success.light}`,
}));

export const LoadingOverlay = styled(Box)(({ theme }) => ({
  position: 'absolute',
  top: 0,
  left: 0,
  right: 0,
  bottom: 0,
  backgroundColor: 'rgba(255, 255, 255, 0.8)',
  display: 'flex',
  alignItems: 'center',
  justifyContent: 'center',
  borderRadius: 16,
  zIndex: 1,
})); 