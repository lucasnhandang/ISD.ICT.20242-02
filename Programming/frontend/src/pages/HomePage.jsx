import React, { useState, useEffect, useCallback } from 'react';
import { Container, Grid, Box, Pagination, Typography, CircularProgress } from '@mui/material';
import Header from '../components/Header';
import Navigation from '../components/Navigation';
import ProductCard from '../components/ProductCard';
import ConnectionStatus from '../components/ConnectionStatus';
import { checkBackendConnection, getProducts, searchProducts } from '../services/api';
import { authService } from '../services/authService';
import {
  containerStyles,
  loadingContainerStyles,
  messageContainerStyles,
  paginationContainerStyles,
  rootStyles
} from '../styles/HomePage.styles';
import { useNavigate } from 'react-router-dom';

const PRODUCTS_PER_PAGE = 20;

const HomePage = () => {
  const [products, setProducts] = useState([]);
  const [page, setPage] = useState(1);
  const [totalPages, setTotalPages] = useState(1);
  const [category, setCategory] = useState('all');
  const [sortBy, setSortBy] = useState('title');
  const [sortDirection, setSortDirection] = useState('asc');
  const [email, setEmail] = useState('');
  const [loading, setLoading] = useState(true);
  const [searchQuery, setSearchQuery] = useState('');
  const [error, setError] = useState(null);
  const [currentUser, setCurrentUser] = useState(null);
  const [connectionStatus, setConnectionStatus] = useState({
    checked: false,
    connected: false,
    message: '',
  });
  const navigate = useNavigate();

  // Check authentication status on component mount
  useEffect(() => {
    const checkAuthStatus = () => {
      const user = authService.getCurrentUser();
      if (user) {
        setCurrentUser(user);
      }
    };
    checkAuthStatus();
  }, []);

  useEffect(() => {
    const checkConnection = async () => {
      const status = await checkBackendConnection();
      setConnectionStatus({
        checked: true,
        connected: status.connected,
        message: status.message,
      });
      if (status.connected) {
        fetchProducts();
      }
    };
    checkConnection();
  }, []);

  const fetchProducts = useCallback(async () => {
    try {
      setLoading(true);
      setError(null);
      let data;

      if (searchQuery) {
        data = await searchProducts(searchQuery, page - 1, PRODUCTS_PER_PAGE, sortBy, sortDirection, category);
      } else {
        data = await getProducts(page - 1, PRODUCTS_PER_PAGE, category, sortBy, sortDirection);
      }

      if (data.content && Array.isArray(data.content)) {
        setProducts(data.content);
        setTotalPages(data.totalPages || 1);
      } else {
        setError('Invalid data format received from server');
        setProducts([]);
        setTotalPages(1);
      }
    } catch (error) {
      setError(error.message);
      setConnectionStatus({
        ...connectionStatus,
        connected: false,
        message: error.message,
      });
      setProducts([]);
      setTotalPages(1);
    } finally {
      setLoading(false);
    }
  }, [page, category, searchQuery, sortBy, sortDirection, connectionStatus]);

  useEffect(() => {
    if (connectionStatus.connected) {
      fetchProducts();
    }
  }, [fetchProducts, connectionStatus.connected]);

  const handlePageChange = (event, value) => {
    setPage(value);
  };

  const handleCategoryChange = (newCategory) => {
    setCategory(newCategory);
    setPage(1);
    setSearchQuery('');
  };

  const handleSortChange = (newSortBy, newSortDirection) => {
    setSortBy(newSortBy);
    setSortDirection(newSortDirection);
    setPage(1);
  };

  const handleSearch = async (query) => {
    setSearchQuery(query);
    setPage(1);
  };

  const handleSignInClick = () => {
    navigate('/login');
  };

  const handleLogout = async () => {
    try {
      await authService.logout();
      setCurrentUser(null);
    } catch (error) {
      console.error('Logout error:', error);
    }
  };

  const handleCartUpdate = () => {
    // This will be handled by the parent component or global state management
  };

  const handleSubscribe = (e) => {
    e.preventDefault();
    // Implement newsletter subscription logic here
    console.log('Subscribe email:', email);
    setEmail('');
  };

  const renderContent = () => {
    if (loading) {
      return (
        <Box sx={loadingContainerStyles}>
          <CircularProgress />
        </Box>
      );
    }

    if (error) {
      return (
        <Box sx={messageContainerStyles}>
          <Typography color="error">{error}</Typography>
        </Box>
      );
    }

    if (products.length === 0) {
      return (
        <Box sx={messageContainerStyles}>
          <Typography>
            {searchQuery
              ? `No products found for "${searchQuery}"`
              : `No products found in category "${category}"`}
          </Typography>
        </Box>
      );
    }

    return (
      <>
        <Grid container spacing={3}>
          {products.map((product) => (
            <Grid item key={product.id} xs={12} sm={6} md={3}>
              <ProductCard 
                product={product}
                onCartUpdate={handleCartUpdate}
              />
            </Grid>
          ))}
        </Grid>

        <Box sx={paginationContainerStyles}>
          <Pagination
            count={totalPages}
            page={page}
            onChange={handlePageChange}
            color="primary"
          />
        </Box>
      </>
    );
  };

  return (
    <Box sx={rootStyles}>
      <Header 
        onSearch={handleSearch}
        onSignInClick={handleSignInClick}
        currentUser={currentUser}
        onLogout={handleLogout}
        showLoginButton={true}
      />

      <ConnectionStatus
        open={connectionStatus.checked}
        message={connectionStatus.message}
        severity={connectionStatus.connected ? 'success' : 'error'}
      />

      <Container maxWidth="lg" sx={containerStyles}>
        <Navigation
          onCategoryChange={handleCategoryChange}
          onSortChange={handleSortChange}
        />
        {renderContent()}
      </Container>

      <Box
        component="footer"
        sx={{
          py: 4,
          bgcolor: '#f5f5f5',
          mt: 'auto'
        }}
      >
      </Box>
    </Box>
  );
};

export default HomePage; 