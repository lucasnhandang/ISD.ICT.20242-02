import React from 'react';
import { ThemeProvider, createTheme } from '@mui/material/styles';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import CssBaseline from '@mui/material/CssBaseline';

import HomePage from './pages/HomePage';
import ManagementPanel from './pages/ManagementPanel';
import CartPage from './pages/CartPage';
import CheckoutPage from './pages/CheckoutPage';
import OrderReviewPage from './pages/OrderReviewPage';
import RushOrderPage from './pages/RushOrderPage';
import InvoicePage from './pages/InvoicePage';
import PaymentResultPage from './pages/PaymentResultPage';
import VnPayReturnPage from './pages/VnPayReturnPage';
import LoginPage from './pages/LoginPage';



import ApiTestComponent from './components/ApiTestComponent';

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
    },
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
          <Route path="/login" element={<LoginPage />} />
          <Route path="/" element={<HomePage />} />
          <Route path="/cart" element={<CartPage />} />
          <Route path="/checkout" element={<CheckoutPage />} />
          <Route path="/management/*" element={<ManagementPanel />} />
          <Route path="/order-review" element={<OrderReviewPage />} />
                      <Route path="/rush-order" element={<RushOrderPage />} />
            <Route path="/invoice" element={<InvoicePage />} />
            <Route path="/payment-result" element={<PaymentResultPage />} />
            <Route path="/vnpay-return" element={<VnPayReturnPage />} />
            <Route path="/api-test" element={<ApiTestComponent />} />
        </Routes>
      </Router>
    </ThemeProvider>
  );
}

export default App;