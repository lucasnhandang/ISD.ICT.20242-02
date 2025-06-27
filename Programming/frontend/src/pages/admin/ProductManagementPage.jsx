import React, { useState, useEffect } from 'react';
import {
  Box,
  Typography,
  Paper,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Button,
  IconButton,
  Chip,
  TextField,
  InputAdornment,
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  FormControl,
  InputLabel,
  Select,
  MenuItem,
  Alert,
  CircularProgress,
  Fab,
  Tooltip
} from '@mui/material';
import {
  Add,
  Edit,
  Delete,
  Search,
  Book,
  Album,
  Movie,
  MusicNote
} from '@mui/icons-material';

const ProductManagementPage = () => {
  const [products, setProducts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [searchQuery, setSearchQuery] = useState('');
  const [selectedProduct, setSelectedProduct] = useState(null);
  const [dialogOpen, setDialogOpen] = useState(false);
  const [dialogMode, setDialogMode] = useState('create'); // 'create' or 'edit'
  const [formData, setFormData] = useState({
    title: '',
    category: '',
    price: '',
    description: '',
    imageUrl: ''
  });
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');

  // Mock data for demonstration
  const mockProducts = [
    {
      id: 1,
      title: 'The Great Gatsby',
      category: 'BOOK',
      price: 15.99,
      description: 'A classic novel by F. Scott Fitzgerald',
      imageUrl: 'https://example.com/gatsby.jpg',
      status: 'ACTIVE'
    },
    {
      id: 2,
      title: 'Abbey Road',
      category: 'CD',
      price: 12.99,
      description: 'The Beatles iconic album',
      imageUrl: 'https://example.com/abbey-road.jpg',
      status: 'ACTIVE'
    },
    {
      id: 3,
      title: 'The Matrix',
      category: 'DVD',
      price: 19.99,
      description: 'Sci-fi action movie',
      imageUrl: 'https://example.com/matrix.jpg',
      status: 'INACTIVE'
    }
  ];

  useEffect(() => {
    // Simulate API call
    setTimeout(() => {
      setProducts(mockProducts);
      setLoading(false);
    }, 1000);
  }, []);

  const categories = [
    { value: 'BOOK', label: 'Book', icon: <Book /> },
    { value: 'CD', label: 'CD', icon: <Album /> },
    { value: 'DVD', label: 'DVD', icon: <Movie /> },
    { value: 'LP', label: 'LP', icon: <MusicNote /> }
  ];

  const getCategoryIcon = (category) => {
    const cat = categories.find(c => c.value === category);
    return cat ? cat.icon : <Book />;
  };

  const getCategoryColor = (category) => {
    switch (category) {
      case 'BOOK':
        return 'primary';
      case 'CD':
        return 'secondary';
      case 'DVD':
        return 'success';
      case 'LP':
        return 'warning';
      default:
        return 'default';
    }
  };

  const handleSearch = (event) => {
    setSearchQuery(event.target.value);
  };

  const filteredProducts = products.filter(product =>
    product.title.toLowerCase().includes(searchQuery.toLowerCase()) ||
    product.description.toLowerCase().includes(searchQuery.toLowerCase())
  );

  const handleCreateProduct = () => {
    setDialogMode('create');
    setSelectedProduct(null);
    setFormData({
      title: '',
      category: '',
      price: '',
      description: '',
      imageUrl: ''
    });
    setDialogOpen(true);
  };

  const handleEditProduct = (product) => {
    setDialogMode('edit');
    setSelectedProduct(product);
    setFormData({
      title: product.title,
      category: product.category,
      price: product.price.toString(),
      description: product.description,
      imageUrl: product.imageUrl
    });
    setDialogOpen(true);
  };

  const handleDeleteProduct = async (productId) => {
    if (window.confirm('Are you sure you want to delete this product?')) {
      try {
        // TODO: Implement API call to delete product
        setProducts(products.filter(p => p.id !== productId));
        setSuccess('Product deleted successfully!');
      } catch (error) {
        setError('Failed to delete product');
      }
    }
  };

  const handleFormSubmit = async (event) => {
    event.preventDefault();
    
    if (!formData.title || !formData.category || !formData.price) {
      setError('Please fill in all required fields');
      return;
    }

    try {
      if (dialogMode === 'create') {
        // TODO: Implement API call to create product
        const newProduct = {
          id: Date.now(),
          ...formData,
          price: parseFloat(formData.price),
          status: 'ACTIVE'
        };
        setProducts([...products, newProduct]);
        setSuccess('Product created successfully!');
      } else {
        // TODO: Implement API call to update product
        setProducts(products.map(p => 
          p.id === selectedProduct.id 
            ? { ...p, ...formData, price: parseFloat(formData.price) }
            : p
        ));
        setSuccess('Product updated successfully!');
      }
      
      setDialogOpen(false);
    } catch (error) {
      setError('Failed to save product');
    }
  };

  const handleInputChange = (field) => (event) => {
    setFormData(prev => ({
      ...prev,
      [field]: event.target.value
    }));
  };

  if (loading) {
    return (
      <Box sx={{ display: 'flex', justifyContent: 'center', alignItems: 'center', height: '50vh' }}>
        <CircularProgress />
      </Box>
    );
  }

  return (
    <Box>
      <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 3 }}>
        <Typography variant="h4">
          Product Management
        </Typography>
        <Tooltip title="Add New Product">
          <Fab
            color="primary"
            aria-label="add"
            onClick={handleCreateProduct}
            sx={{ boxShadow: 2 }}
          >
            <Add />
          </Fab>
        </Tooltip>
      </Box>

      {/* Search Bar */}
      <Paper sx={{ p: 2, mb: 3 }}>
        <TextField
          fullWidth
          placeholder="Search products by title or description..."
          value={searchQuery}
          onChange={handleSearch}
          InputProps={{
            startAdornment: (
              <InputAdornment position="start">
                <Search />
              </InputAdornment>
            ),
          }}
        />
      </Paper>

      {/* Products Table */}
      <TableContainer component={Paper} elevation={2}>
        <Table>
          <TableHead>
            <TableRow sx={{ backgroundColor: 'grey.50' }}>
              <TableCell>Product</TableCell>
              <TableCell>Category</TableCell>
              <TableCell align="right">Price</TableCell>
              <TableCell>Status</TableCell>
              <TableCell align="center">Actions</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {filteredProducts.map((product) => (
              <TableRow key={product.id} hover>
                <TableCell>
                  <Box>
                    <Typography variant="subtitle2" fontWeight="bold">
                      {product.title}
                    </Typography>
                    <Typography variant="caption" color="text.secondary">
                      {product.description}
                    </Typography>
                  </Box>
                </TableCell>
                <TableCell>
                  <Chip
                    icon={getCategoryIcon(product.category)}
                    label={product.category}
                    color={getCategoryColor(product.category)}
                    size="small"
                    variant="outlined"
                  />
                </TableCell>
                <TableCell align="right">
                  <Typography variant="subtitle2" fontWeight="bold">
                    ${product.price}
                  </Typography>
                </TableCell>
                <TableCell>
                  <Chip
                    label={product.status}
                    color={product.status === 'ACTIVE' ? 'success' : 'default'}
                    size="small"
                  />
                </TableCell>
                <TableCell align="center">
                  <IconButton
                    color="primary"
                    onClick={() => handleEditProduct(product)}
                    size="small"
                  >
                    <Edit />
                  </IconButton>
                  <IconButton
                    color="error"
                    onClick={() => handleDeleteProduct(product.id)}
                    size="small"
                  >
                    <Delete />
                  </IconButton>
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </TableContainer>

      {/* Create/Edit Dialog */}
      <Dialog open={dialogOpen} onClose={() => setDialogOpen(false)} maxWidth="sm" fullWidth>
        <DialogTitle>
          {dialogMode === 'create' ? 'Create New Product' : 'Edit Product'}
        </DialogTitle>
        <form onSubmit={handleFormSubmit}>
          <DialogContent>
            <Box sx={{ display: 'flex', flexDirection: 'column', gap: 2 }}>
              <TextField
                label="Product Title"
                value={formData.title}
                onChange={handleInputChange('title')}
                required
                fullWidth
              />
              
              <FormControl fullWidth required>
                <InputLabel>Category</InputLabel>
                <Select
                  value={formData.category}
                  onChange={handleInputChange('category')}
                  label="Category"
                >
                  {categories.map((category) => (
                    <MenuItem key={category.value} value={category.value}>
                      <Box sx={{ display: 'flex', alignItems: 'center', gap: 1 }}>
                        {category.icon}
                        {category.label}
                      </Box>
                    </MenuItem>
                  ))}
                </Select>
              </FormControl>
              
              <TextField
                label="Price"
                type="number"
                value={formData.price}
                onChange={handleInputChange('price')}
                required
                fullWidth
                InputProps={{
                  startAdornment: <InputAdornment position="start">$</InputAdornment>,
                }}
              />
              
              <TextField
                label="Description"
                value={formData.description}
                onChange={handleInputChange('description')}
                multiline
                rows={3}
                fullWidth
              />
              
              <TextField
                label="Image URL"
                value={formData.imageUrl}
                onChange={handleInputChange('imageUrl')}
                fullWidth
              />
            </Box>
          </DialogContent>
          <DialogActions>
            <Button onClick={() => setDialogOpen(false)}>
              Cancel
            </Button>
            <Button type="submit" variant="contained">
              {dialogMode === 'create' ? 'Create' : 'Update'}
            </Button>
          </DialogActions>
        </form>
      </Dialog>

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

export default ProductManagementPage; 