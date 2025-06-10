import React, { useState } from 'react';
import { Box, Button, ButtonGroup, Select, MenuItem, FormControl, InputLabel } from '@mui/material';

const Navigation = ({ onCategoryChange, onSortChange }) => {
  const [selectedCategory, setSelectedCategory] = useState('all');
  const [sortValue, setSortValue] = useState('title-asc');

  const categories = [
    { id: 'all', label: 'All items' },
    { id: 'LP', label: 'LP' },
    { id: 'DVD', label: 'DVD' },
    { id: 'CD', label: 'CD' },
    { id: 'Book', label: 'Book' }
  ];

  const handleCategoryClick = (categoryId) => {
    setSelectedCategory(categoryId);
    onCategoryChange(categoryId);
  };

  const handleSortChange = (event) => {
    const value = event.target.value;
    setSortValue(value);
    const [newSortBy, newSortDirection] = value.split('-');
    onSortChange(newSortBy, newSortDirection);
  };

  return (
    <Box sx={{ 
      display: 'flex', 
      justifyContent: 'space-between', 
      alignItems: 'center',
      mb: 3 
    }}>
      <Box sx={{ display: 'flex', gap: 2 }}>
        {categories.map((category) => (
          <Button
            key={category.id}
            variant="outlined"
            onClick={() => handleCategoryClick(category.id)}
            sx={{
              borderRadius: '45px',
              textTransform: 'none',
              px: 3,
              ...(selectedCategory === category.id && {
                backgroundColor: 'primary.main',
                color: 'white',
                '&:hover': {
                  backgroundColor: 'primary.dark',
                }
              })
            }}
          >
            {category.label}
          </Button>
        ))}
      </Box>

      <FormControl 
        variant="outlined" 
        size="small"
        sx={{ 
          minWidth: 200,
          '& .MuiOutlinedInput-root': {
            borderRadius: '45px',
          }
        }}
      >
        <InputLabel>Sort by</InputLabel>
        <Select
          value={sortValue}
          onChange={handleSortChange}
          label="Sort by"
        >
          <MenuItem value="title-asc">Name: A to Z</MenuItem>
          <MenuItem value="title-desc">Name: Z to A</MenuItem>
          <MenuItem value="price-asc">Price: Low to High</MenuItem>
          <MenuItem value="price-desc">Price: High to Low</MenuItem>
        </Select>
      </FormControl>
    </Box>
  );
};

export default Navigation;