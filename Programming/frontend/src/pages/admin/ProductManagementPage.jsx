import React, { useState, useEffect } from 'react';
import {
  Box, Typography, Paper, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Button, IconButton, Chip, TextField, InputAdornment, Dialog, DialogTitle, DialogContent, DialogActions, FormControl, InputLabel, Select, MenuItem, Alert, CircularProgress, Fab, Tooltip, Grid, Avatar, Checkbox, FormControlLabel
} from '@mui/material';
import { Add, Edit, Delete, Search, Book, Album, Movie, MusicNote, Image } from '@mui/icons-material';
import { productService } from '../../services/productService';

const CATEGORY_CONFIG = {
  BOOK: {
    label: 'Book', icon: <Book />, fields: [
      { name: 'authors', label: 'Authors', required: true },
      { name: 'coverType', label: 'Cover Type', required: false },
      { name: 'publisher', label: 'Publisher', required: false },
      { name: 'publicationDate', label: 'Publication Date', type: 'date', required: false },
      { name: 'pages', label: 'Pages', type: 'number', required: false },
      { name: 'language', label: 'Language', required: false },
      { name: 'genre', label: 'Genre', required: false },
    ]
  },
  CD: {
    label: 'CD', icon: <Album />, fields: [
      { name: 'artists', label: 'Artists', required: true },
      { name: 'recordLabel', label: 'Record Label', required: false },
      { name: 'trackList', label: 'Track List', required: false },
      { name: 'genre', label: 'Genre', required: false },
      { name: 'releaseDate', label: 'Release Date', type: 'date', required: false },
    ]
  },
  DVD: {
    label: 'DVD', icon: <Movie />, fields: [
      { name: 'discType', label: 'Disc Type', required: true },
      { name: 'director', label: 'Director', required: false },
      { name: 'runtime', label: 'Runtime (minutes)', type: 'number', required: false },
      { name: 'studio', label: 'Studio', required: false },
      { name: 'language', label: 'Language', required: false },
      { name: 'subtitles', label: 'Subtitles', required: false },
      { name: 'genre', label: 'Genre', required: false },
      { name: 'releaseDate', label: 'Release Date', type: 'date', required: false },
    ]
  },
  LP: {
    label: 'LP', icon: <MusicNote />, fields: [
      { name: 'artists', label: 'Artists', required: true },
      { name: 'recordLabel', label: 'Record Label', required: false },
      { name: 'trackList', label: 'Track List', required: false },
      { name: 'genre', label: 'Genre', required: false },
      { name: 'releaseDate', label: 'Release Date', type: 'date', required: false },
    ]
  }
};

const BASE_FIELDS = [
  { name: 'title', label: 'Title', required: true },
  { name: 'value', label: 'Value (no VAT)', type: 'number', required: true },
  { name: 'currentPrice', label: 'Current Price', type: 'number', required: true },
  { name: 'barcode', label: 'Barcode', required: true },
  { name: 'description', label: 'Description', required: false },
  { name: 'quantity', label: 'Quantity', type: 'number', required: true },
  { name: 'entryDate', label: 'Warehouse Entry Date', type: 'date', required: true },
  { name: 'dimension', label: 'Dimensions', required: false },
  { name: 'weight', label: 'Weight (kg)', type: 'number', required: false },
  { name: 'rushOrderSupported', label: 'Rush Order Supported', type: 'boolean', required: false },
];

// Hàm format giá VND
const formatVND = (price) => {
  if (!price && price !== 0) return '';
  return new Intl.NumberFormat('vi-VN', {
    style: 'currency',
    currency: 'VND',
    minimumFractionDigits: 0,
    maximumFractionDigits: 0
  }).format(price).replace('₫', 'VND');
};

const ProductManagementPage = () => {
  const [products, setProducts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [searchQuery, setSearchQuery] = useState('');
  const [selectedProduct, setSelectedProduct] = useState(null);
  const [dialogOpen, setDialogOpen] = useState(false);
  const [dialogMode, setDialogMode] = useState('create');
  const [formData, setFormData] = useState({ category: 'BOOK', entryDate: new Date().toISOString().slice(0, 10) });
  const [imageFile, setImageFile] = useState(null);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');
  const [selectedIds, setSelectedIds] = useState([]);
  const [refresh, setRefresh] = useState(false);

  // Fetch products
  useEffect(() => {
    const fetchProducts = async () => {
      setLoading(true);
      try {
        const data = await productService.searchProducts({ query: searchQuery, page: 0, size: 50 });
        setProducts(data.content || []);
      } catch (e) {
        setError(e.message);
      } finally {
        setLoading(false);
      }
    };
    fetchProducts();
  }, [searchQuery, refresh]);

  // Handle form field change
  const handleInputChange = (field) => (event) => {
    let value = event.target.value;
    if (event.target.type === 'number') value = value === '' ? '' : Number(value);
    if (event.target.type === 'checkbox') value = event.target.checked;
    setFormData((prev) => ({ ...prev, [field]: value }));
  };

  // Handle image upload
  const handleImageChange = (e) => {
    setImageFile(e.target.files[0]);
  };

  // Open dialog for create/edit
  const openDialog = async (mode, product = null) => {
    setDialogMode(mode);
    setError('');
    setSuccess('');
    setImageFile(null);
    if (mode === 'edit' && product) {
      try {
        // Lấy detail từ API để fill đủ các trường đặc thù
        const detail = await productService.getProductDetail(product.id);
        // Chuyển đổi ngày về dạng yyyy-MM-dd nếu có
        const newFormData = { ...detail };
        Object.keys(newFormData).forEach(key => {
          if (typeof newFormData[key] === 'string' && /\d{4}-\d{2}-\d{2}/.test(newFormData[key])) {
            newFormData[key] = newFormData[key].slice(0, 10);
          }
        });
        setFormData(newFormData);
        setSelectedProduct(product);
      } catch (e) {
        setError(e.message);
        setFormData({ category: product.category });
        setSelectedProduct(product);
      }
    } else {
      setFormData({ category: 'BOOK', entryDate: new Date().toISOString().slice(0, 10) });
      setSelectedProduct(null);
    }
    setDialogOpen(true);
  };

  // Validate form
  const validateForm = () => {
    for (const field of BASE_FIELDS) {
      if (field.required && !formData[field.name]) return `${field.label} is required`;
    }
    const catFields = CATEGORY_CONFIG[formData.category]?.fields || [];
    for (const field of catFields) {
      if (field.required && !formData[field.name]) return `${field.label} is required`;
    }
    // Validate price logic (30%-150% value)
    if (formData.currentPrice && formData.value) {
      const min = 0.3 * formData.value;
      const max = 1.5 * formData.value;
      if (formData.currentPrice < min || formData.currentPrice > max) {
        return 'Current price must be between 30% and 150% of value';
      }
    }
    return '';
  };

  // Submit form
  const handleFormSubmit = async (e) => {
    e.preventDefault();
    const err = validateForm();
    if (err) { setError(err); return; }
    setError('');
    setSuccess('');
    try {
      if (dialogMode === 'create') {
        await productService.createProduct(formData, imageFile);
        setSuccess('Product created successfully!');
      } else {
        await productService.updateProduct(selectedProduct.id, formData, imageFile);
        setSuccess('Product updated successfully!');
      }
      setDialogOpen(false);
      setRefresh(r => !r);
    } catch (e) {
      setError(e.message);
    }
  };

  // Delete products
  const handleDelete = async (ids) => {
    if (!window.confirm('Are you sure you want to delete selected product(s)?')) return;
    try {
      await productService.deleteProducts(ids);
      setSuccess('Deleted successfully!');
      setRefresh(r => !r);
      setSelectedIds([]);
    } catch (e) {
      setError(e.message);
    }
  };

  // Table row selection
  const toggleSelect = (id) => {
    setSelectedIds((prev) => prev.includes(id) ? prev.filter(i => i !== id) : [...prev, id]);
  };

  // Render form fields
  const renderFields = () => {
    return (
      <>
        {BASE_FIELDS.filter(field => field.name !== 'rushOrderSupported').map(field => (
          <TextField
            key={field.name}
            label={field.label}
            type={field.type || 'text'}
            value={formData[field.name] ?? ''}
            onChange={handleInputChange(field.name)}
            required={field.required}
            fullWidth
            margin="dense"
            InputLabelProps={field.type === 'date' ? { shrink: true } : undefined}
            // Thêm helper text cho các trường giá
            helperText={
              (field.name === 'value' || field.name === 'currentPrice') && formData[field.name] 
                ? `Preview: ${formatVND(formData[field.name])}`
                : field.name === 'currentPrice' 
                  ? 'Must be between 30% and 150% of value'
                  : undefined
            }
          />
        ))}
        <FormControlLabel
          control={
            <Checkbox
              checked={!!formData.rushOrderSupported}
              onChange={e => setFormData(prev => ({ ...prev, rushOrderSupported: e.target.checked }))}
              color="primary"
            />
          }
          label="Rush Order Supported"
          sx={{ mb: 1 }}
        />
        {CATEGORY_CONFIG[formData.category]?.fields.map(field => (
          <TextField
            key={field.name}
            label={field.label}
            type={field.type || 'text'}
            value={formData[field.name] ?? ''}
            onChange={handleInputChange(field.name)}
            required={field.required}
            fullWidth
            margin="dense"
            InputLabelProps={field.type === 'date' ? { shrink: true } : undefined}
          />
        ))}
        <Button
          component="label"
          variant="outlined"
          startIcon={<Image />}
          sx={{ mt: 1 }}
        >
          {imageFile ? imageFile.name : 'Upload Image'}
          <input type="file" accept="image/*" hidden onChange={handleImageChange} />
        </Button>
      </>
    );
  };

  if (loading) {
    return <Box sx={{ display: 'flex', justifyContent: 'center', alignItems: 'center', height: '50vh' }}><CircularProgress /></Box>;
  }

  return (
    <Box>
      <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 3 }}>
        <Typography variant="h4">Product Management</Typography>
        <Tooltip title="Add New Product">
          <Fab color="primary" aria-label="add" onClick={() => openDialog('create')} sx={{ boxShadow: 2 }}>
            <Add />
          </Fab>
        </Tooltip>
      </Box>
      <Paper sx={{ p: 2, mb: 3 }}>
        <TextField
          fullWidth
          placeholder="Search products by title or description..."
          value={searchQuery}
          onChange={e => setSearchQuery(e.target.value)}
          InputProps={{ startAdornment: (<InputAdornment position="start"><Search /></InputAdornment>) }}
        />
      </Paper>
      <TableContainer component={Paper} elevation={2}>
        <Table>
          <TableHead>
            <TableRow sx={{ backgroundColor: 'grey.50' }}>
              <TableCell></TableCell>
              <TableCell>Product</TableCell>
              <TableCell>Category</TableCell>
              <TableCell align="right">Price</TableCell>
              <TableCell>Quantity</TableCell>
              <TableCell>Status</TableCell>
              <TableCell align="center">Actions</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {products.map((product) => (
              <TableRow key={product.id} hover selected={selectedIds.includes(product.id)}>
                <TableCell>
                  <input type="checkbox" checked={selectedIds.includes(product.id)} onChange={() => toggleSelect(product.id)} />
                </TableCell>
                <TableCell>
                  <Box sx={{ display: 'flex', alignItems: 'center', gap: 1 }}>
                    <Avatar src={product.imageUrl} variant="rounded" sx={{ width: 48, height: 48 }} />
                    <Box>
                      <Typography variant="subtitle2" fontWeight="bold">{product.title}</Typography>
                      <Typography variant="caption" color="text.secondary">{product.description}</Typography>
                    </Box>
                  </Box>
                </TableCell>
                <TableCell>
                  <Chip icon={CATEGORY_CONFIG[product.category]?.icon} label={product.category} size="small" variant="outlined" />
                </TableCell>
                <TableCell align="right">
                  <Typography variant="subtitle2" fontWeight="bold" color="primary">
                    {formatVND(product.currentPrice)}
                  </Typography>
                  {product.value && (
                    <Typography variant="caption" color="text.secondary" display="block">
                      Value: {formatVND(product.value)}
                    </Typography>
                  )}
                </TableCell>
                <TableCell>{product.quantity}</TableCell>
                <TableCell>
                  <Chip label={product.quantity > 0 ? 'ACTIVE' : 'OUT OF STOCK'} color={product.quantity > 0 ? 'success' : 'default'} size="small" />
                </TableCell>
                <TableCell align="center">
                  <IconButton color="primary" onClick={() => openDialog('edit', product)} size="small"><Edit /></IconButton>
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </TableContainer>
      <Box sx={{ mt: 2, display: 'flex', gap: 2 }}>
        <Button variant="outlined" color="error" startIcon={<Delete />} disabled={selectedIds.length === 0} onClick={() => handleDelete(selectedIds)}>
          Delete Selected
        </Button>
      </Box>
      <Dialog open={dialogOpen} onClose={() => setDialogOpen(false)} maxWidth="md" fullWidth>
        <DialogTitle>{dialogMode === 'create' ? 'Create New Product' : 'Edit Product'}</DialogTitle>
        <form onSubmit={handleFormSubmit}>
          <DialogContent>
            <Grid container spacing={2}>
              <Grid item xs={12} md={4}>
                <FormControl fullWidth required>
                  <InputLabel>Category</InputLabel>
                  <Select
                    value={formData.category}
                    onChange={handleInputChange('category')}
                    label="Category"
                    disabled={dialogMode === 'edit'}
                  >
                    {Object.entries(CATEGORY_CONFIG).map(([key, cat]) => (
                      <MenuItem key={key} value={key}>{cat.label}</MenuItem>
                    ))}
                  </Select>
                </FormControl>
              </Grid>
              <Grid item xs={12} md={8}>
                {renderFields()}
              </Grid>
            </Grid>
            {error && <Alert severity="error" sx={{ mt: 2 }}>{error}</Alert>}
            {success && <Alert severity="success" sx={{ mt: 2 }}>{success}</Alert>}
          </DialogContent>
          <DialogActions>
            <Button onClick={() => setDialogOpen(false)}>Cancel</Button>
            <Button type="submit" variant="contained">{dialogMode === 'create' ? 'Create' : 'Update'}</Button>
          </DialogActions>
        </form>
      </Dialog>
    </Box>
  );
};

export default ProductManagementPage;