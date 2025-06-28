import React from 'react';
import { ThemeProvider, createTheme } from '@mui/material/styles';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import CssBaseline from '@mui/material/CssBaseline';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import HomePage from './pages/HomePage';
<<<<<<< HEAD
import PaymentResult from './pages/PaymentResult';
=======
import ManagementPanel from './pages/ManagementPanel';
import CartPage from './pages/CartPage';
import LoginPage from './pages/LoginPage';
>>>>>>> b22ef1d0e0c8121cb3584607270b2075fbd496ca

const theme = createTheme({
  palette: {
    primary: {
      main: '#2563eb',
      light: '#60a5fa',
      dark: '#1d4ed8',
    },
    secondary: {
      main: '#f59e0b',
      light: '#fbbf24',
      dark: '#d97706',
    },
    background: {
      default: '#f8fafc',
      paper: '#ffffff',
    },
    text: {
      primary: '#1e293b',
      secondary: '#64748b',
    }
  },
  typography: {
    fontFamily: [
      'Be Vietnam Pro',
      '-apple-system',
      'BlinkMacSystemFont',
      '"Segoe UI"',
      'Roboto',
      '"Helvetica Neue"',
      'Arial',
      'sans-serif',
    ].join(','),
  },
  shape: {
    borderRadius: 12,
  },
  components: {
    MuiButton: {
      styleOverrides: {
        root: {
          textTransform: 'none',
          fontWeight: 600,
          borderRadius: 8,
        },
      },
    },
    MuiCard: {
      styleOverrides: {
        root: {
          borderRadius: 12,
          boxShadow: '0 1px 3px rgba(0,0,0,0.12), 0 1px 2px rgba(0,0,0,0.24)',
        },
      },
    },
  },
});

function App() {
  return (
    <ThemeProvider theme={theme}>
      <CssBaseline />
      <Router>
        <Routes>
          <Route path="/" element={<HomePage />} />
<<<<<<< HEAD
          <Route path="/payment-result" element={<PaymentResult />} />
=======
          <Route path="/cart" element={<CartPage />} />
          <Route path="/management/*" element={<ManagementPanel />} />
          <Route path="/login" element={<LoginPage />} />
>>>>>>> b22ef1d0e0c8121cb3584607270b2075fbd496ca
        </Routes>
      </Router>
    </ThemeProvider>
  );
}

export default App; 