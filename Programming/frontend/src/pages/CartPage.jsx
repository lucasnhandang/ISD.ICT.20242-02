import React, { useState, useEffect } from 'react';
import {
  Container,
  Typography,
  Box,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Paper,
  IconButton,
  Button,
  TextField,
  Divider,
  Alert,
  Snackbar,
  CircularProgress,
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions
} from '@mui/material';
import DeleteIcon from '@mui/icons-material/Delete';
import EditIcon from '@mui/icons-material/Edit';
import Header from '../components/Header';
import { getCart, removeFromCart, updateCartItem, requestToPlaceOrder } from '../services/api';
import { useNavigate } from 'react-router-dom';

const formatPrice = (price) => {
  return new Intl.NumberFormat('vi-VN', {
    style: 'currency',
    currency: 'VND'
  }).format(price);
};

const CartPage = () => {
  const [cart, setCart] = useState({
    productList: [],
    totalPrice: 0,
    totalItem: 0,
    currency: 'VND',
    discount: 0
  });
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [snackbar, setSnackbar] = useState({
    open: false,
    message: '',
    severity: 'success'
  });
  const [quantityDialog, setQuantityDialog] = useState({
    open: false,
    item: null,
    quantity: 1
  });
  const [invoiceList, setInvoiceList] = useState([]);
  const navigate = useNavigate();

  const fetchCart = async () => {
    try {
      setLoading(true);
      const response = await getCart();
      setCart(response.data);
      await requestToPlaceOrder(response.data);
    } catch (err) {
      setError('Failed to load cart. Please try again later.');
      console.error('Error loading cart:', err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchCart();
  }, []);

  const handleCloseSnackbar = () => {
    setSnackbar({ ...snackbar, open: false });
  };

  const handleOpenQuantityDialog = (item) => {
    setQuantityDialog({
      open: true,
      item: item,
      quantity: item.quantity
    });
  };

  const handleCloseQuantityDialog = () => {
    setQuantityDialog({
      open: false,
      item: null,
      quantity: 1
    });
  };

  const handleQuantityChange = (event) => {
    const value = parseInt(event.target.value);
    if (value > 0) {
      setQuantityDialog(prev => ({
        ...prev,
        quantity: value
      }));
    }
  };

  const handleUpdateQuantity = async () => {
    try {
      const response = await updateCartItem({
        productID: quantityDialog.item.productID,
        quantity: quantityDialog.quantity
      });

      setCart(response.data);
      setSnackbar({
        open: true,
        message: 'Cart updated successfully',
        severity: 'success'
      });
      handleCloseQuantityDialog();
    } catch (err) {
      setSnackbar({
        open: true,
        message: err.message || 'Failed to update quantity. Please try again.',
        severity: 'error'
      });
      console.error('Error updating quantity:', err);
    }
  };

  const handleRemoveItem = async (productId) => {
    try {
      const response = await removeFromCart(productId);
      setCart(response.data);
      setSnackbar({
        open: true,
        message: 'Item removed from cart',
        severity: 'success'
      });
    } catch (err) {
      setSnackbar({
        open: true,
        message: 'Failed to remove item. Please try again.',
        severity: 'error'
      });
      console.error('Error removing item:', err);
    }
  };

  const handleProceedToCheckout = () => {
    navigate('/checkout');
  };

  if (loading) {
    return (
      <Box sx={{ display: 'flex', justifyContent: 'center', alignItems: 'center', minHeight: '100vh' }}>
        <CircularProgress />
      </Box>
    );
  }

  return (
    <Box sx={{ minHeight: '100vh', backgroundColor: 'background.default' }}>
      <Header invoiceList={invoiceList} />
      <Container maxWidth="lg" sx={{ mt: 4, mb: 4 }}>
        <Typography variant="h4" gutterBottom sx={{ fontWeight: 600 }}>
          Shopping Cart
        </Typography>

        {error && (
          <Alert severity="error" sx={{ mt: 2 }}>
            {error}
          </Alert>
        )}

        {!error && cart.productList.length === 0 ? (
          <Alert severity="info" sx={{ mt: 2 }}>
            Your cart is empty. Start shopping to add items to your cart.
          </Alert>
        ) : (
          <>
            <TableContainer component={Paper} sx={{ mt: 2 }}>
              <Table>
                <TableHead>
                  <TableRow>
                    <TableCell>Product</TableCell>
                    <TableCell>Price</TableCell>
                    <TableCell>Quantity</TableCell>
                    <TableCell>Total</TableCell>
                    <TableCell>Actions</TableCell>
                  </TableRow>
                </TableHead>
                <TableBody>
                  {cart.productList.map((item) => (
                    <TableRow key={item.productID}>
                      <TableCell>
                        <Typography variant="subtitle1">
                          {item.productName}
                        </Typography>
                      </TableCell>
                      <TableCell>
                        {formatPrice(item.price)}
                      </TableCell>
                      <TableCell>
                        <Box sx={{ display: 'flex', alignItems: 'center', gap: 2 }}>
                          <Typography>
                            {item.quantity}
                          </Typography>
                          <Button
                            size="small"
                            variant="outlined"
                            onClick={() => handleOpenQuantityDialog(item)}
                            startIcon={<EditIcon />}
                          >
                            Change Quantity
                          </Button>
                        </Box>
                      </TableCell>
                      <TableCell>
                        {formatPrice(item.price * item.quantity)}
                      </TableCell>
                      <TableCell>
                        <IconButton
                          color="error"
                          onClick={() => handleRemoveItem(item.productID)}
                        >
                          <DeleteIcon />
                        </IconButton>
                      </TableCell>
                    </TableRow>
                  ))}
                </TableBody>
              </Table>
            </TableContainer>

            <Box sx={{ mt: 4, p: 2, bgcolor: 'background.paper', borderRadius: 1 }}>
              <Typography variant="h6" gutterBottom>
                Order Summary
              </Typography>
              <Divider sx={{ my: 2 }} />
              <Box sx={{ display: 'flex', justifyContent: 'space-between', mb: 2 }}>
                <Typography>Subtotal ({cart.totalItem} items)</Typography>
                <Typography>{formatPrice(cart.totalPrice)}</Typography>
              </Box>
              {cart.discount > 0 && (
                <Box sx={{ display: 'flex', justifyContent: 'space-between', mb: 2 }}>
                  <Typography>Discount</Typography>
                  <Typography color="error">
                    -{formatPrice(cart.discount)}
                  </Typography>
                </Box>
              )}
              <Divider sx={{ my: 2 }} />
              <Box sx={{ display: 'flex', justifyContent: 'space-between', mb: 2 }}>
                <Typography variant="h6">Total</Typography>
                <Typography variant="h6" color="primary">
                  {formatPrice(cart.totalPrice - cart.discount)}
                </Typography>
              </Box>
              <Button
                variant="contained"
                fullWidth
                size="large"
                onClick={handleProceedToCheckout}
                sx={{ mt: 2 }}
              >
                Proceed to Checkout
              </Button>
            </Box>
          </>
        )}

        {/* Quantity Dialog */}
        <Dialog
          open={quantityDialog.open}
          onClose={handleCloseQuantityDialog}
          maxWidth="xs"
          fullWidth
        >
          <DialogTitle>Update Quantity</DialogTitle>
          <DialogContent>
            <Box sx={{ mt: 2 }}>
              <Typography variant="subtitle1" gutterBottom>
                {quantityDialog.item?.productName}
              </Typography>
              <Typography variant="body2" color="text.secondary" gutterBottom>
                Price: {quantityDialog.item && formatPrice(quantityDialog.item.price)}
              </Typography>
              <TextField
                label="Quantity"
                type="number"
                value={quantityDialog.quantity}
                onChange={handleQuantityChange}
                fullWidth
                sx={{ mt: 2 }}
                inputProps={{
                  min: 1
                }}
              />
            </Box>
          </DialogContent>
          <DialogActions sx={{ p: 2, pt: 0 }}>
            <Button onClick={handleCloseQuantityDialog}>Cancel</Button>
            <Button
              onClick={handleUpdateQuantity}
              variant="contained"
              color="primary"
              disabled={!quantityDialog.quantity || quantityDialog.quantity < 1}
            >
              Update
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
      </Container>
    </Box>
  );
};

export default CartPage; 