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
  Divider,
  CircularProgress
} from '@mui/material';
import AddShoppingCartIcon from '@mui/icons-material/AddShoppingCart';
import InfoIcon from '@mui/icons-material/Info';
import { addToCart, getProductDetails } from '../services/api';
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

const formatDate = (dateString) => {
  if (!dateString) return 'N/A';
  return new Date(dateString).toLocaleDateString('vi-VN');
};

const renderBookDetails = (product) => (
  <>
    {product.authors && (
      <Typography variant="body1" gutterBottom>
        <strong>Authors:</strong> {product.authors}
      </Typography>
    )}
    {product.publisher && (
      <Typography variant="body1" gutterBottom>
        <strong>Publisher:</strong> {product.publisher}
      </Typography>
    )}
    {product.coverType && (
      <Typography variant="body1" gutterBottom>
        <strong>Cover Type:</strong> {product.coverType}
      </Typography>
    )}
    {product.publicationDate && (
      <Typography variant="body1" gutterBottom>
        <strong>Publication Date:</strong> {formatDate(product.publicationDate)}
      </Typography>
    )}
    {product.pages && (
      <Typography variant="body1" gutterBottom>
        <strong>Pages:</strong> {product.pages}
      </Typography>
    )}
    {product.language && (
      <Typography variant="body1" gutterBottom>
        <strong>Language:</strong> {product.language}
      </Typography>
    )}
    {product.genre && (
      <Typography variant="body1" gutterBottom>
        <strong>Genre:</strong> {product.genre}
      </Typography>
    )}
  </>
);

const renderCDDetails = (product) => (
  <>
    {product.artists && (
      <Typography variant="body1" gutterBottom>
        <strong>Artists:</strong> {product.artists}
      </Typography>
    )}
    {product.recordLabel && (
      <Typography variant="body1" gutterBottom>
        <strong>Record Label:</strong> {product.recordLabel}
      </Typography>
    )}
    {product.genre && (
      <Typography variant="body1" gutterBottom>
        <strong>Genre:</strong> {product.genre}
      </Typography>
    )}
    {product.releaseDate && (
      <Typography variant="body1" gutterBottom>
        <strong>Release Date:</strong> {formatDate(product.releaseDate)}
      </Typography>
    )}
    {product.trackList && (
      <Typography variant="body1" gutterBottom>
        <strong>Track List:</strong> {product.trackList}
      </Typography>
    )}
  </>
);

const renderDVDDetails = (product) => (
  <>
    {product.director && (
      <Typography variant="body1" gutterBottom>
        <strong>Director:</strong> {product.director}
      </Typography>
    )}
    {product.studio && (
      <Typography variant="body1" gutterBottom>
        <strong>Studio:</strong> {product.studio}
      </Typography>
    )}
    {product.discType && (
      <Typography variant="body1" gutterBottom>
        <strong>Disc Type:</strong> {product.discType}
      </Typography>
    )}
    {product.runtime && (
      <Typography variant="body1" gutterBottom>
        <strong>Runtime:</strong> {product.runtime} minutes
      </Typography>
    )}
    {product.language && (
      <Typography variant="body1" gutterBottom>
        <strong>Language:</strong> {product.language}
      </Typography>
    )}
    {product.subtitles && (
      <Typography variant="body1" gutterBottom>
        <strong>Subtitles:</strong> {product.subtitles}
      </Typography>
    )}
    {product.genre && (
      <Typography variant="body1" gutterBottom>
        <strong>Genre:</strong> {product.genre}
      </Typography>
    )}
    {product.releaseDate && (
      <Typography variant="body1" gutterBottom>
        <strong>Release Date:</strong> {formatDate(product.releaseDate)}
      </Typography>
    )}
  </>
);

const renderProductSpecificDetails = (product) => {
  const category = product.category?.toLowerCase();
  
  switch (category) {
    case 'book':
      return renderBookDetails(product);
    case 'cd':
      return renderCDDetails(product);
    case 'dvd':
      return renderDVDDetails(product);
    default:
      return null;
  }
};

const ProductCard = ({ product, onCartUpdate }) => {
  const [openDetailsDialog, setOpenDetailsDialog] = useState(false);
  const [openQuantityDialog, setOpenQuantityDialog] = useState(false);
  const [quantity, setQuantity] = useState(1);
  const [productDetails, setProductDetails] = useState(null);
  const [loadingDetails, setLoadingDetails] = useState(false);
  const [snackbar, setSnackbar] = useState({
    open: false,
    message: '',
    severity: 'success'
  });

  const handleViewDetails = async () => {
    setOpenDetailsDialog(true);
    setLoadingDetails(true);
    
    try {
      const response = await getProductDetails(product.id);
      setProductDetails(response.data);
    } catch (error) {
      console.error('Error fetching product details:', error);
      setSnackbar({
        open: true,
        message: 'Unable to load product details',
        severity: 'error'
      });
    } finally {
      setLoadingDetails(false);
    }
  };

  const handleCloseDetails = () => {
    setOpenDetailsDialog(false);
    setProductDetails(null);
  };

  const handleAddToCart = () => {
    if (product.quantity > 0) {
      setQuantity(1); // Reset quantity to 1 each time dialog opens
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
    const value = event.target.value;
    
    // Allow empty input or numbers only
    if (value === '' || /^\d+$/.test(value)) {
      const numValue = value === '' ? '' : parseInt(value);
      
      // If empty or within valid range then update
      if (value === '' || (numValue >= 1 && numValue <= product.quantity)) {
        setQuantity(value === '' ? 1 : numValue);
      }
    }
  };

  const handleQuantityBlur = (event) => {
    const value = event.target.value;
    if (value === '' || parseInt(value) < 1) {
      setQuantity(1);
    } else if (parseInt(value) > product.quantity) {
      setQuantity(product.quantity);
    }
  };

  const handleConfirmAdd = async () => {
    try {
      // Validate quantity
      const validQuantity = Math.max(1, Math.min(quantity, product.quantity));
      setQuantity(validQuantity);
      
      if (validQuantity > product.quantity || validQuantity < 1) {
        throw new Error('Invalid quantity');
      }

      const response = await addToCart({
        productID: product.id,
        quantity: validQuantity
      });

      if (onCartUpdate) {
        onCartUpdate(response.data);
      }

      setSnackbar({
        open: true,
        message: `Added ${validQuantity} ${product.title} to cart`,
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
          
          {/* Hiển thị giá sản phẩm */}
          <Typography 
            variant="h6" 
            color="primary" 
            sx={{ fontWeight: 600, mb: 1 }}
          >
            {formatPrice(product.currentPrice || product.price || 0)}
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
          {loadingDetails ? (
            <Box sx={{ display: 'flex', justifyContent: 'center', alignItems: 'center', minHeight: 200 }}>
              <CircularProgress />
            </Box>
          ) : (
            <Grid container spacing={3}>
              <Grid item xs={12} md={6}>
                <img 
                  src={(productDetails || product).imageUrl || '/placeholder-book.jpg'} 
                  alt={(productDetails || product).title}
                  style={{ width: '100%', height: 'auto', borderRadius: '8px' }}
                />
              </Grid>
              <Grid item xs={12} md={6}>
                <Typography variant="h5" gutterBottom>
                  {(productDetails || product).title}
                </Typography>
                <Typography variant="h6" color="primary" gutterBottom>
                  {formatPrice((productDetails || product).currentPrice)}
                </Typography>
                <Divider sx={{ my: 2 }} />
                <Typography variant="body1" gutterBottom>
                  <strong>Category:</strong> {(productDetails || product).category}
                </Typography>
                <Typography variant="body1" gutterBottom>
                  <strong>In Stock:</strong> {(productDetails || product).quantity} products
                </Typography>
                <Typography variant="body1" gutterBottom>
                  <strong>Rush Delivery:</strong> 
                  <Chip 
                    label={(productDetails || product).rushOrderSupported ? "Supported" : "Not Supported"}
                    color={(productDetails || product).rushOrderSupported ? "success" : "default"}
                    size="small"
                    sx={{ ml: 1 }}
                  />
                </Typography>
                
                {/* Product-specific details */}
                {productDetails && renderProductSpecificDetails(productDetails)}
                
                {/* Common product details */}
                {(productDetails || product).dimension && (
                  <Typography variant="body1" gutterBottom>
                    <strong>Dimensions:</strong> {(productDetails || product).dimension}
                  </Typography>
                )}
                {(productDetails || product).weight && (
                  <Typography variant="body1" gutterBottom>
                    <strong>Weight:</strong> {(productDetails || product).weight} kg
                  </Typography>
                )}
                
                {(productDetails || product).description && (
                  <>
                    <Divider sx={{ my: 2 }} />
                    <Typography variant="h6" gutterBottom>
                      Description
                    </Typography>
                    <Typography variant="body1" gutterBottom>
                      {(productDetails || product).description}
                    </Typography>
                  </>
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
          )}
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
            In Stock: {product.quantity} products
          </Typography>
          <TextField
            label="Quantity"
            type="number"
            value={quantity}
            onChange={handleQuantityChange}
            onBlur={handleQuantityBlur}
            autoFocus
            fullWidth
            inputProps={{ 
              min: 1,
              max: product.quantity,
              style: { textAlign: 'center' }
            }}
            sx={{
              ...quantityInputStyles,
              '& input[type=number]': {
                'MozAppearance': 'textfield'
              },
              '& input[type=number]::-webkit-outer-spin-button': {
                'WebkitAppearance': 'none',
                margin: 0
              },
              '& input[type=number]::-webkit-inner-spin-button': {
                'WebkitAppearance': 'none',
                margin: 0
              }
            }}
            helperText={`Enter a number from 1 to ${product.quantity}`}
            onFocus={(event) => event.target.select()}
          />
        </DialogContent>
        <DialogActions sx={dialogActionsStyles}>
          <Button onClick={handleCloseQuantityDialog}>Cancel</Button>
          <Button 
            onClick={handleConfirmAdd} 
            variant="contained" 
            color="primary"
            disabled={!quantity || quantity < 1 || quantity > product.quantity}
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