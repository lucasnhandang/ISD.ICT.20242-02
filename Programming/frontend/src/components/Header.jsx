import React, { useState, useCallback, useEffect } from 'react';
import { AppBar, Toolbar, Typography, Button, Badge, Box } from '@mui/material';
import SearchIcon from '@mui/icons-material/Search';
import ShoppingCartIcon from '@mui/icons-material/ShoppingCart';
import {
  Search,
  SearchIconWrapper,
  StyledInputBase,
  LoadingIndicator,
  logoStyles,
  signInButtonStyles
} from '../styles/Header.styles';

const Header = ({ onSearch }) => {
  const [searchValue, setSearchValue] = useState('');
  const [isSearching, setIsSearching] = useState(false);
  const [debouncedValue, setDebouncedValue] = useState('');

  // Debounce search value
  useEffect(() => {
    const timer = setTimeout(() => {
      setDebouncedValue(searchValue);
    }, 500); // Wait for 500ms after last change

    return () => clearTimeout(timer);
  }, [searchValue]);

  // Trigger search when debounced value changes
  useEffect(() => {
    const performSearch = async () => {
      if (debouncedValue !== '') {
        setIsSearching(true);
        await onSearch(debouncedValue);
        setIsSearching(false);
      }
    };

    performSearch();
  }, [debouncedValue, onSearch]);

  const handleSearchChange = (event) => {
    setSearchValue(event.target.value);
  };

  const handleSearchSubmit = (event) => {
    if (event.key === 'Enter') {
      setIsSearching(true);
      onSearch(searchValue).finally(() => {
        setIsSearching(false);
      });
    }
  };

  const handleClearSearch = () => {
    setSearchValue('');
    onSearch('');
  };

  return (
    <AppBar position="static" color="default" elevation={0}>
      <Toolbar>
        <Typography
          variant="h6"
          noWrap
          component="div"
          sx={logoStyles}
          onClick={handleClearSearch}
        >
          AIMS
        </Typography>

        <Search>
          <SearchIconWrapper>
            <SearchIcon />
          </SearchIconWrapper>
          <StyledInputBase
            placeholder="Search something here!"
            inputProps={{ 'aria-label': 'search' }}
            value={searchValue}
            onChange={handleSearchChange}
            onKeyPress={handleSearchSubmit}
          />
          {isSearching && <LoadingIndicator size="small" />}
        </Search>

        <Box sx={{ flexGrow: 1 }} />

        <Box sx={{ display: 'flex', alignItems: 'center' }}>
          <Badge badgeContent={0} color="error" sx={{ mr: 2 }}>
            <ShoppingCartIcon />
          </Badge>
          <Button 
            variant="contained" 
            color="primary"
            sx={signInButtonStyles}
          >
            Sign In
          </Button>
        </Box>
      </Toolbar>
    </AppBar>
  );
};

export default Header; 