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
        <strong>Tác giả:</strong> {product.authors}
      </Typography>
    )}
    {product.publisher && (
      <Typography variant="body1" gutterBottom>
        <strong>Nhà xuất bản:</strong> {product.publisher}
      </Typography>
    )}
    {product.coverType && (
      <Typography variant="body1" gutterBottom>
        <strong>Loại bìa:</strong> {product.coverType}
      </Typography>
    )}
    {product.publicationDate && (
      <Typography variant="body1" gutterBottom>
        <strong>Ngày xuất bản:</strong> {formatDate(product.publicationDate)}
      </Typography>
    )}
    {product.pages && (
      <Typography variant="body1" gutterBottom>
        <strong>Số trang:</strong> {product.pages}
      </Typography>
    )}
    {product.language && (
      <Typography variant="body1" gutterBottom>
        <strong>Ngôn ngữ:</strong> {product.language}
      </Typography>
    )}
    {product.genre && (
      <Typography variant="body1" gutterBottom>
        <strong>Thể loại:</strong> {product.genre}
      </Typography>
    )}
  </>
);

const renderCDDetails = (product) => (
  <>
    {product.artists && (
      <Typography variant="body1" gutterBottom>
        <strong>Nghệ sĩ:</strong> {product.artists}
      </Typography>
    )}
    {product.recordLabel && (
      <Typography variant="body1" gutterBottom>
        <strong>Hãng thu âm:</strong> {product.recordLabel}
      </Typography>
    )}
    {product.genre && (
      <Typography variant="body1" gutterBottom>
        <strong>Thể loại:</strong> {product.genre}
      </Typography>
    )}
    {product.releaseDate && (
      <Typography variant="body1" gutterBottom>
        <strong>Ngày phát hành:</strong> {formatDate(product.releaseDate)}
      </Typography>
    )}
    {product.trackList && (
      <Typography variant="body1" gutterBottom>
        <strong>Danh sách bài hát:</strong> {product.trackList}
      </Typography>
    )}
  </>
);

const renderDVDDetails = (product) => (
  <>
    {product.director && (
      <Typography variant="body1" gutterBottom>
        <strong>Đạo diễn:</strong> {product.director}
      </Typography>
    )}
    {product.studio && (
      <Typography variant="body1" gutterBottom>
        <strong>Studio:</strong> {product.studio}
      </Typography>
    )}
    {product.discType && (
      <Typography variant="body1" gutterBottom>
        <strong>Loại đĩa:</strong> {product.discType}
      </Typography>
    )}
    {product.runtime && (
      <Typography variant="body1" gutterBottom>
        <strong>Thời lượng:</strong> {product.runtime} phút
      </Typography>
    )}
    {product.language && (
      <Typography variant="body1" gutterBottom>
        <strong>Ngôn ngữ:</strong> {product.language}
      </Typography>
    )}
    {product.subtitles && (
      <Typography variant="body1" gutterBottom>
        <strong>Phụ đề:</strong> {product.subtitles}
      </Typography>
    )}
    {product.genre && (
      <Typography variant="body1" gutterBottom>
        <strong>Thể loại:</strong> {product.genre}
      </Typography>
    )}
    {product.releaseDate && (
      <Typography variant="body1" gutterBottom>
        <strong>Ngày phát hành:</strong> {formatDate(product.releaseDate)}
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
        message: 'Không thể tải thông tin chi tiết sản phẩm',
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
      setQuantity(1); // Reset quantity về 1 mỗi khi mở dialog
      setOpenQuantityDialog(true);
    } else {
      setSnackbar({
        open: true,
        message: 'Sản phẩm đã hết hàng',
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
    
    // Cho phép input trống hoặc chỉ số
    if (value === '' || /^\d+$/.test(value)) {
      const numValue = value === '' ? '' : parseInt(value);
      
      // Nếu trống hoặc trong khoảng hợp lệ thì update
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
        throw new Error('Số lượng không hợp lệ');
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
        message: `Đã thêm ${validQuantity} ${product.title} vào giỏ hàng`,
        severity: 'success'
      });

      handleCloseQuantityDialog();
      handleCloseDetails();
    } catch (error) {
      console.error('Error adding to cart:', error);
      setSnackbar({
        open: true,
        message: error.response?.data?.message || 'Lỗi khi thêm vào giỏ hàng',
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
            Xem chi tiết
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
              label={`${product.quantity} còn lại`}
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
        <DialogTitle>Chi tiết sản phẩm</DialogTitle>
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
                  <strong>Danh mục:</strong> {(productDetails || product).category}
                </Typography>
                <Typography variant="body1" gutterBottom>
                  <strong>Còn lại:</strong> {(productDetails || product).quantity} sản phẩm
                </Typography>
                
                {/* Product-specific details */}
                {productDetails && renderProductSpecificDetails(productDetails)}
                
                {/* Common product details */}
                {(productDetails || product).dimension && (
                  <Typography variant="body1" gutterBottom>
                    <strong>Kích thước:</strong> {(productDetails || product).dimension}
                  </Typography>
                )}
                {(productDetails || product).weight && (
                  <Typography variant="body1" gutterBottom>
                    <strong>Trọng lượng:</strong> {(productDetails || product).weight} kg
                  </Typography>
                )}
                
                {(productDetails || product).description && (
                  <>
                    <Divider sx={{ my: 2 }} />
                    <Typography variant="h6" gutterBottom>
                      Mô tả
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
                    {product.quantity === 0 ? 'Hết hàng' : 'Thêm vào giỏ'}
                  </Button>
                </Box>
              </Grid>
            </Grid>
          )}
        </DialogContent>
        <DialogActions>
          <Button onClick={handleCloseDetails}>Đóng</Button>
        </DialogActions>
      </Dialog>

      {/* Quantity Selection Dialog */}
      <Dialog
        open={openQuantityDialog}
        onClose={handleCloseQuantityDialog}
        sx={quantityDialogStyles}
      >
        <DialogTitle>Thêm vào giỏ hàng</DialogTitle>
        <DialogContent>
          <Typography variant="body1" gutterBottom>
            {product.title}
          </Typography>
          <Typography variant="body2" color="text.secondary" gutterBottom>
            Giá: {formatPrice(product.currentPrice)}
          </Typography>
          <Typography variant="body2" color="text.secondary" gutterBottom>
            Còn lại: {product.quantity} sản phẩm
          </Typography>
          <TextField
            label="Số lượng"
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
            helperText={`Nhập số từ 1 đến ${product.quantity}`}
            onFocus={(event) => event.target.select()}
          />
        </DialogContent>
        <DialogActions sx={dialogActionsStyles}>
          <Button onClick={handleCloseQuantityDialog}>Hủy</Button>
          <Button 
            onClick={handleConfirmAdd} 
            variant="contained" 
            color="primary"
            disabled={!quantity || quantity < 1 || quantity > product.quantity}
          >
            Thêm vào giỏ
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