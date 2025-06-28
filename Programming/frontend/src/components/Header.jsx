import React, { useState, useEffect } from 'react';
import { AppBar, Toolbar, Typography, Button, Badge, Box, IconButton } from '@mui/material';
import { useNavigate } from 'react-router-dom';
import SearchIcon from '@mui/icons-material/Search';
import ShoppingCartIcon from '@mui/icons-material/ShoppingCart';
import api from '../services/api';
import {
  Search,
  SearchIconWrapper,
  StyledInputBase,
  LoadingIndicator,
  logoStyles,
  signInButtonStyles
} from '../styles/Header.styles';

const Header = ({ onSearch }) => {
  const navigate = useNavigate();
  const [searchValue, setSearchValue] = useState('');
  const [isSearching, setIsSearching] = useState(false);
  const [debouncedValue, setDebouncedValue] = useState('');
  const [cartItemCount, setCartItemCount] = useState(0);

  // Debounce search value
  useEffect(() => {
    const timer = setTimeout(() => {
      setDebouncedValue(searchValue);
    }, 500);

    return () => clearTimeout(timer);
  }, [searchValue]);

  // Update cart count from API
  useEffect(() => {
    const fetchCartCount = async () => {
      try {
        const response = await api.get('/cart');
        setCartItemCount(response.data.totalItem);
      } catch (error) {
        console.error('Error fetching cart count:', error);
        setCartItemCount(0);
      }
    };

    fetchCartCount();

    // Poll for cart updates every 5 seconds
    const interval = setInterval(fetchCartCount, 5000);

    return () => clearInterval(interval);
  }, []);

  // Trigger search when debounced value changes
  useEffect(() => {
    const performSearch = async () => {
      if (debouncedValue !== '') {
        setIsSearching(true);
        await onSearch?.(debouncedValue);
        setIsSearching(false);
      }
    };

    performSearch();
  }, [debouncedValue, onSearch]);

  const handleSearchChange = (event) => {
    setSearchValue(event.target.value);
  };

  const handleSearchSubmit = (event) => {
    if (event.key === 'Enter' && onSearch) {
      setIsSearching(true);
      onSearch(searchValue).finally(() => {
        setIsSearching(false);
      });
    }
  };

  const handleLogoClick = () => {
    navigate('/');
  };

  const handleCartClick = () => {
    navigate('/cart');
  };

  return (
    <AppBar position="static" color="default" elevation={1}>
      <Toolbar>
        <Typography
          variant="h6"
          noWrap
          component="div"
          sx={logoStyles}
          onClick={handleLogoClick}
        >
          AIMS
        </Typography>

        {onSearch && (
          <Search>
            <SearchIconWrapper>
              <SearchIcon />
            </SearchIconWrapper>
            <StyledInputBase
              placeholder="Search products..."
              value={searchValue}
              onChange={handleSearchChange}
              onKeyPress={handleSearchSubmit}
            />
            {isSearching && <LoadingIndicator size={20} />}
          </Search>
        )}

        <Box sx={{ flexGrow: 1 }} />

        <IconButton 
          color="primary"
          onClick={handleCartClick}
          sx={{ mr: 2 }}
        >
          <Badge badgeContent={cartItemCount} color="error">
            <ShoppingCartIcon />
          </Badge>
        </IconButton>

        <Button
          variant="contained"
          sx={signInButtonStyles}
        >
          Sign In
        </Button>
      </Toolbar>
    </AppBar>
  );
};

export default Header; 