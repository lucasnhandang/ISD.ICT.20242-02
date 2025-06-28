import React, { useState, useCallback, useEffect } from 'react';
import { AppBar, Toolbar, Typography, Button, Badge, Box, Avatar, Menu, MenuItem, IconButton } from '@mui/material';
import { useNavigate } from 'react-router-dom';
import SearchIcon from '@mui/icons-material/Search';
import ShoppingCartIcon from '@mui/icons-material/ShoppingCart';
import AccountCircleIcon from '@mui/icons-material/AccountCircle';
import LogoutIcon from '@mui/icons-material/Logout';
import api from '../services/api';
import {
  Search,
  SearchIconWrapper,
  StyledInputBase,
  LoadingIndicator,
  logoStyles,
  signInButtonStyles
} from '../styles/Header.styles';

const Header = ({ onSearch, onSignInClick, currentUser, onLogout, showLoginButton = true }) => {
  const navigate = useNavigate();
  const [searchValue, setSearchValue] = useState('');
  const [isSearching, setIsSearching] = useState(false);
  const [debouncedValue, setDebouncedValue] = useState('');
  const [cartItemCount, setCartItemCount] = useState(0);
  const [anchorEl, setAnchorEl] = useState(null);

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
    setSearchValue('');
    onSearch?.('');
  };

  const handleCartClick = () => {
    navigate('/cart');
  };

  const handleUserMenuOpen = (event) => {
    setAnchorEl(event.currentTarget);
  };

  const handleUserMenuClose = () => {
    setAnchorEl(null);
  };

  const handleLogout = () => {
    handleUserMenuClose();
    if (onLogout) {
      onLogout();
    }
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

        <Box sx={{ display: 'flex', alignItems: 'center' }}>
          <IconButton 
            color="primary"
            onClick={handleCartClick}
            sx={{ mr: 2 }}
          >
            <Badge badgeContent={cartItemCount} color="error">
              <ShoppingCartIcon />
            </Badge>
          </IconButton>

          {currentUser ? (
            <>
              <IconButton
                onClick={handleUserMenuOpen}
                sx={{ 
                  display: 'flex', 
                  alignItems: 'center',
                  gap: 1,
                  color: 'primary.main',
                  '&:hover': {
                    backgroundColor: 'primary.light + 20',
                  }
                }}
              >
                <Avatar 
                  sx={{ 
                    width: 32, 
                    height: 32, 
                    bgcolor: 'primary.main',
                    fontSize: '0.875rem',
                    fontWeight: 600
                  }}
                >
                  {currentUser.name ? currentUser.name.charAt(0).toUpperCase() : 'U'}
                </Avatar>
                <Typography 
                  variant="body2" 
                  sx={{ 
                    display: { xs: 'none', sm: 'block' },
                    fontWeight: 500,
                    color: 'text.primary'
                  }}
                >
                  {currentUser.name} ({currentUser.roles?.join(', ')})
                </Typography>
              </IconButton>
              <Menu
                anchorEl={anchorEl}
                open={Boolean(anchorEl)}
                onClose={handleUserMenuClose}
                PaperProps={{
                  sx: {
                    mt: 1,
                    minWidth: 200,
                    borderRadius: 2,
                    boxShadow: '0 4px 20px rgba(0,0,0,0.1)',
                  }
                }}
              >
                <MenuItem 
                  onClick={handleLogout}
                  sx={{ 
                    display: 'flex', 
                    alignItems: 'center', 
                    gap: 1,
                    color: 'error.main',
                    '&:hover': {
                      backgroundColor: 'error.light + 20',
                    }
                  }}
                >
                  <LogoutIcon fontSize="small" />
                  Logout
                </MenuItem>
              </Menu>
            </>
          ) : (
            showLoginButton && (
              <Button 
                variant="contained" 
                color="primary"
                sx={signInButtonStyles}
                onClick={onSignInClick}
              >
                Admin/PM Login
              </Button>
            )
          )}
        </Box>
      </Toolbar>
    </AppBar>
  );
};

export default Header; 