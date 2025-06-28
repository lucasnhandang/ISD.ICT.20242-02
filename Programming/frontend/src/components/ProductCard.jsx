import React, { useState } from 'react';
import { 
  Card, 
  CardContent, 
  CardMedia, 
  Typography, 
  Box,
  Button,
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  TextField,
  Alert,
  Snackbar,
  Chip,
  Grid,
  Divider
} from '@mui/material';
import AddShoppingCartIcon from '@mui/icons-material/AddShoppingCart';
import InfoIcon from '@mui/icons-material/Info';
import { addToCart } from '../services/api';
import { 
  cardStyles, 
  cardMediaStyles, 
  cardContentStyles,
  titleStyles, 
  categoryStyles,
  priceStyles,
  overlayStyles,
  addToCartButtonStyles,
  quantityDialogStyles,
  quantityInputStyles,
  dialogActionsStyles
} from '../styles/ProductCard.styles';

const formatPrice = (price) => {
  return new Intl.NumberFormat('vi-VN', {
    style: 'currency',
    currency: 'VND'
  }).format(price);
};

const ProductCard = ({ product, onCartUpdate }) => {
  const [openDetailsDialog, setOpenDetailsDialog] = useState(false);
  const [openQuantityDialog, setOpenQuantityDialog] = useState(false);
  const [quantity, setQuantity] = useState(1);
  const [snackbar, setSnackbar] = useState({
    open: false,
    message: '',
    severity: 'success'
  });

  const handleViewDetails = () => {
    setOpenDetailsDialog(true);
  };

  const handleCloseDetails = () => {
    setOpenDetailsDialog(false);
  };

  const handleAddToCart = () => {
    if (product.quantity > 0) {
      setOpenQuantityDialog(true);
    } else {
      setSnackbar({
        open: true,
        message: 'Product is out of stock',
        severity: 'error'
      });
    }
  };

  const handleCloseQuantityDialog = () => {
    setOpenQuantityDialog(false);
    setQuantity(1);
  };

  const handleCloseSnackbar = () => {
    setSnackbar({ ...snackbar, open: false });
  };

  const handleQuantityChange = (event) => {
    const value = parseInt(event.target.value);
    if (value > 0 && value <= product.quantity) {
      setQuantity(value);
    }
  };

  const handleConfirmAdd = async () => {
    try {
      if (quantity > product.quantity) {
        throw new Error('Not enough quantity available');
      }

      const response = await addToCart({
        productID: product.id,
        quantity: quantity
      });

      if (onCartUpdate) {
        onCartUpdate(response.data);
      }

      setSnackbar({
        open: true,
        message: `Added ${quantity} ${product.title} to cart`,
        severity: 'success'
      });

      handleCloseQuantityDialog();
      handleCloseDetails();
    } catch (error) {
      console.error('Error adding to cart:', error);
      setSnackbar({
        open: true,
        message: error.response?.data?.message || 'Error adding to cart',
        severity: 'error'
      });
    }
  };

  return (
    <>
      <Card sx={cardStyles}>
        <Box sx={{ position: 'relative' }}>
          <CardMedia
            component="img"
            image={product.imageUrl || '/placeholder-book.jpg'}
            alt={product.title}
            sx={cardMediaStyles}
          />
          <Box className="overlay" sx={overlayStyles} />
          <Button
            className="add-to-cart-btn"
            variant="contained"
            startIcon={<InfoIcon />}
            onClick={handleViewDetails}
            sx={addToCartButtonStyles}
          >
            View Details
          </Button>
        </Box>
        <CardContent sx={cardContentStyles}>
          <Typography 
            variant="h6" 
            component="h2"
            sx={titleStyles}
          >
            {product.title}
          </Typography>
          
          <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 1 }}>
            <Typography 
              component="span"
              sx={categoryStyles}
            >
              {product.category}
            </Typography>
            <Chip 
              label={`${product.quantity} in stock`}
              color={product.quantity > 0 ? "success" : "error"}
              size="small"
            />
          </Box>
        </CardContent>
      </Card>

      {/* Product Details Dialog */}
      <Dialog
        open={openDetailsDialog}
        onClose={handleCloseDetails}
        maxWidth="md"
        fullWidth
      >
        <DialogTitle>Product Details</DialogTitle>
        <DialogContent>
          <Grid container spacing={3}>
            <Grid item xs={12} md={6}>
              <img 
                src={product.imageUrl || '/placeholder-book.jpg'} 
                alt={product.title}
                style={{ width: '100%', height: 'auto', borderRadius: '8px' }}
              />
            </Grid>
            <Grid item xs={12} md={6}>
              <Typography variant="h5" gutterBottom>
                {product.title}
              </Typography>
              <Typography variant="h6" color="primary" gutterBottom>
                {formatPrice(product.currentPrice)}
              </Typography>
              <Divider sx={{ my: 2 }} />
              <Typography variant="body1" gutterBottom>
                Category: {product.category}
              </Typography>
              <Typography variant="body1" gutterBottom>
                Available: {product.quantity} items
              </Typography>
              {product.description && (
                <Typography variant="body1" gutterBottom>
                  {product.description}
                </Typography>
              )}
              <Box sx={{ mt: 3 }}>
                <Button
                  variant="contained"
                  color="primary"
                  startIcon={<AddShoppingCartIcon />}
                  onClick={handleAddToCart}
                  disabled={product.quantity === 0}
                  fullWidth
                >
                  {product.quantity === 0 ? 'Out of Stock' : 'Add to Cart'}
                </Button>
              </Box>
            </Grid>
          </Grid>
        </DialogContent>
        <DialogActions>
          <Button onClick={handleCloseDetails}>Close</Button>
        </DialogActions>
      </Dialog>

      {/* Quantity Selection Dialog */}
      <Dialog
        open={openQuantityDialog}
        onClose={handleCloseQuantityDialog}
        sx={quantityDialogStyles}
      >
        <DialogTitle>Add to Cart</DialogTitle>
        <DialogContent>
          <Typography variant="body1" gutterBottom>
            {product.title}
          </Typography>
          <Typography variant="body2" color="text.secondary" gutterBottom>
            Price: {formatPrice(product.currentPrice)}
          </Typography>
          <Typography variant="body2" color="text.secondary" gutterBottom>
            Available: {product.quantity} items
          </Typography>
          <TextField
            label="Quantity"
            type="number"
            value={quantity}
            onChange={handleQuantityChange}
            inputProps={{ 
              min: 1,
              max: product.quantity
            }}
            sx={quantityInputStyles}
            helperText={`Maximum: ${product.quantity}`}
          />
        </DialogContent>
        <DialogActions sx={dialogActionsStyles}>
          <Button onClick={handleCloseQuantityDialog}>Cancel</Button>
          <Button 
            onClick={handleConfirmAdd} 
            variant="contained" 
            color="primary"
            disabled={quantity > product.quantity}
          >
            Add to Cart
          </Button>
        </DialogActions>
      </Dialog>

      <Snackbar 
        open={snackbar.open} 
        autoHideDuration={3000} 
        onClose={handleCloseSnackbar}
        anchorOrigin={{ vertical: 'bottom', horizontal: 'center' }}
      >
        <Alert 
          onClose={handleCloseSnackbar} 
          severity={snackbar.severity}
          variant="filled"
        >
          {snackbar.message}
        </Alert>
      </Snackbar>
    </>
  );
};

export default ProductCard; 