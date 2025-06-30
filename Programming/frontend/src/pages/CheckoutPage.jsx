import React, { useEffect, useState } from 'react';
import { 
  Box, 
  Typography, 
  Divider, 
  Paper, 
  CircularProgress, 
  Alert, 
  Button,
  Card,
  CardContent,
  CardMedia,
  Grid
} from '@mui/material';
import { useNavigate, useLocation } from 'react-router-dom';
import Header from '../components/Header';
import DeliveryForm from '../components/DeliveryForm';
import { 
  getCart, 
  submitDeliveryForm, 
  handleNormalOrder,
  getProductDetails 
} from '../services/api';

const formatPrice = (price, currency = 'VND') => {
  return new Intl.NumberFormat('vi-VN', {
    style: 'currency',
    currency: currency
  }).format(price);
};

const CheckoutPage = () => {
  const [cart, setCart] = useState(null);
  const [productDetails, setProductDetails] = useState({});
  const [loading, setLoading] = useState(true);
  const [processingOrder, setProcessingOrder] = useState(false);
  const [error, setError] = useState('');
  const [fetchingProducts, setFetchingProducts] = useState(false);
  const navigate = useNavigate();
  const location = useLocation();
  
  // Lấy thông tin form đã lưu từ OrderReviewPage (nếu có)
  const preservedDeliveryForm = location.state?.preservedDeliveryForm;

  // Fetch cart data
  useEffect(() => {
    const fetchCart = async () => {
      try {
        setLoading(true);
        const response = await getCart();
        
        if (!response.data || !response.data.productList) {
          setError('Cart is empty or invalid.');
          return;
        }
        
        setCart(response.data);
      } catch (err) {
        console.error('Error fetching cart:', err);
        setError(err.response?.data?.message || 'Unable to load cart.');
      } finally {
        setLoading(false);
      }
    };
    
    fetchCart();
  }, []);

  // Fetch product details when cart is loaded
  useEffect(() => {
    const fetchProductDetails = async () => {
      if (!cart?.productList?.length) return;
      
      try {
        setFetchingProducts(true);
        const productPromises = cart.productList.map(async (item) => {
          try {
            const response = await getProductDetails(item.productID);
            return { [item.productID]: response.data };
          } catch (error) {
            console.error(`Error fetching product ${item.productID}:`, error);
            return { [item.productID]: null };
          }
        });

        const results = await Promise.all(productPromises);
        const productMap = results.reduce((acc, curr) => ({ ...acc, ...curr }), {});
        setProductDetails(productMap);
      } catch (error) {
        console.error('Error fetching product details:', error);
      } finally {
        setFetchingProducts(false);
      }
    };

    fetchProductDetails();
  }, [cart]);

  // Handle normal order submission (with delivery form from state)
  const handleNormalOrderSubmit = async () => {
    try {
      setProcessingOrder(true);
      setError('');
      
      if (!preservedDeliveryForm) {
        throw new Error('No delivery information found. Please fill delivery form first.');
      }
      
      // Submit delivery form
      await submitDeliveryForm(preservedDeliveryForm);
      
      // Finally handle normal order
      const response = await handleNormalOrder(cart);
      
      if (!response.data) {
        throw new Error('No response received from server');
      }
      
      const { cart: apiCart, invoice, deliveryForm, orderid } = response.data;
      
      console.log('Normal order response:', response.data);
      
      // Navigate to OrderReviewPage with complete data
      navigate('/order-review', {
        state: {
          cart: apiCart || cart,
          invoice: invoice,
          deliveryForm: deliveryForm,
          orderId: orderid 
        }
      });
      
    } catch (error) {
      console.error('Failed to process normal order:', error);
      setError(error.response?.data?.message || 'An error occurred while processing the order');
    } finally {
      setProcessingOrder(false);
    }
  };

  // Handle delivery form submission
  const handleDeliveryFormSuccess = async (deliveryForm) => {
    try {
      setProcessingOrder(true);
      setError('');
      
      if (!cart) {
        throw new Error('Cart information not found');
      }

      // Submit delivery form
      await submitDeliveryForm(deliveryForm);
      
      // Finally handle normal order
      const invoiceResponse = await handleNormalOrder(cart);
      
      if (!invoiceResponse.data) {
        throw new Error('Invoice information not received');
      }

      // Navigate with complete data
      navigate('/order-review', { 
        state: { 
          cart: invoiceResponse.data.cart || cart,
          invoice: invoiceResponse.data.invoice, 
          deliveryForm: deliveryForm,
          orderId: invoiceResponse.data.orderid
        } 
      });
      
    } catch (err) {
      console.error('Error processing delivery form:', err);
      setError(err.response?.data?.message || 'An error occurred, please try again.');
    } finally {
      setProcessingOrder(false);
    }
  };

  // Loading state
  if (loading) {
    return (
      <Box sx={{ display: 'flex', justifyContent: 'center', alignItems: 'center', minHeight: '100vh' }}>
        <CircularProgress size={60} />
        <Typography sx={{ ml: 2 }}>Loading cart...</Typography>
      </Box>
    );
  }

  // Error state
  if (error) {
    return (
      <Box sx={{ minHeight: '100vh', backgroundColor: 'background.default' }}>
        <Header />
        <Box sx={{ maxWidth: '1200px', mx: 'auto', mt: 4, p: 2 }}>
          <Alert severity="error" sx={{ mb: 2 }}>
            {error}
          </Alert>
          <Button variant="contained" onClick={() => navigate('/cart')}>
            Back to Cart
          </Button>
        </Box>
      </Box>
    );
  }

  // Empty cart state
  if (!cart || !cart.productList || cart.productList.length === 0) {
    return (
      <Box sx={{ minHeight: '100vh', backgroundColor: 'background.default' }}>
        <Header />
        <Box sx={{ maxWidth: '1200px', mx: 'auto', mt: 4, p: 2, textAlign: 'center' }}>
          <Alert severity="info" sx={{ mb: 2 }}>
            Your cart is empty
          </Alert>
          <Button variant="contained" onClick={() => navigate('/products')}>
            Continue Shopping
          </Button>
        </Box>
      </Box>
    );
  }

  return (
    <Box sx={{ minHeight: '100vh', backgroundColor: 'background.default' }}>
      <Header />
      <Box sx={{ 
        display: 'flex', 
        flexDirection: { xs: 'column', md: 'row' }, 
        maxWidth: '1200px', 
        mx: 'auto', 
        mt: 4, 
        gap: 4,
        p: 2 
      }}>
        
        {/* Left side: Cart information */}
        <Paper elevation={2} sx={{ flex: 1, minWidth: { xs: '100%', md: '350px' } }}>
          <Box sx={{ p: 3 }}>
            <Typography variant="h5" fontWeight={600} gutterBottom>
              Order Information
            </Typography>
            <Divider sx={{ mb: 2 }} />
            
            {/* Products list */}
            <Box sx={{ mb: 3 }}>
              {fetchingProducts && (
                <Box sx={{ display: 'flex', alignItems: 'center', mb: 2 }}>
                  <CircularProgress size={20} sx={{ mr: 1 }} />
                  <Typography variant="body2" color="text.secondary">
                    Loading product information...
                  </Typography>
                </Box>
              )}
              
              {cart.productList.map((item) => {
                const product = productDetails[item.productID];
                return (
                  <Card key={item.productID} sx={{ mb: 2, elevation: 1 }}>
                    <CardContent sx={{ p: 2, '&:last-child': { pb: 2 } }}>
                      <Grid container spacing={2} alignItems="center">
                        {/* Product image */}
                        {product?.imageUrl && (
                          <Grid item xs={3}>
                            <CardMedia
                              component="img"
                              sx={{ 
                                width: '100%', 
                                height: 60, 
                                objectFit: 'cover',
                                borderRadius: 1
                              }}
                              image={product.imageUrl}
                              alt={item.productName}
                            />
                          </Grid>
                        )}
                        
                        {/* Product info */}
                        <Grid item xs={product?.imageUrl ? 9 : 12}>
                          <Typography variant="subtitle1" fontWeight={500} gutterBottom>
                            {item.productName}
                          </Typography>
                          <Box sx={{ display: 'flex', justifyContent: 'space-between', mt: 1 }}>
                            <Typography variant="body2">
                              Quantity: <strong>{item.quantity}</strong>
                            </Typography>
                            <Typography variant="body2">
                              Unit Price: {formatPrice(item.price, cart.currency)}
                            </Typography>
                          </Box>
                          <Typography variant="body2" color="primary" fontWeight={500}>
                            Subtotal: {formatPrice(item.price * item.quantity, cart.currency)}
                          </Typography>
                        </Grid>
                      </Grid>
                    </CardContent>
                  </Card>
                );
              })}
            </Box>

            {/* Price summary */}
            <Divider sx={{ my: 2 }} />
            <Box sx={{ mb: 2 }}>
              <Box sx={{ display: 'flex', justifyContent: 'space-between', mb: 1 }}>
                <Typography>Product Price (excl. 10% VAT):</Typography>
                <Typography>
                  {formatPrice(cart.totalPrice - (cart.discount || 0), cart.currency)}
                </Typography>
              </Box>
              
              {cart.discount > 0 && (
                <Box sx={{ display: 'flex', justifyContent: 'space-between', mb: 1 }}>
                  <Typography color="success.main">Discount:</Typography>
                  <Typography color="success.main">
                    -{formatPrice(cart.discount, cart.currency)}
                  </Typography>
                </Box>
              )}
              
              {/* <Box sx={{ display: 'flex', justifyContent: 'space-between', mb: 1 }}>
                <Typography>Giá sản phẩm (đã VAT):</Typography>
                <Typography>
                  {formatPrice(cart.totalPrice, cart.currency)}
                </Typography>
              </Box> */}
              
              <Typography color="text.secondary" variant="body2" sx={{ mt: 2, fontStyle: 'italic' }}>
                * Shipping fee, VAT, rush delivery fee (if applicable) will be calculated after entering delivery address.
              </Typography>
            </Box>

            <Divider sx={{ my: 2 }} />
            <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
              <Typography variant="h6" color="primary" fontWeight={600}>
                Total:
              </Typography>
              <Typography variant="h6" color="primary" fontWeight={600}>
                {formatPrice(cart.totalPrice - (cart.discount || 0), cart.currency)}
              </Typography>
            </Box>

            {/* Shipping policy */}
            <Divider sx={{ my: 2 }} />
            <Box>
              <Typography variant="subtitle2" color="text.secondary" gutterBottom>
                📋 Shipping and Rush Delivery Policy:
              </Typography>
              <Box component="ul" sx={{ 
                pl: 2, 
                color: 'text.secondary', 
                fontSize: '0.875rem',
                '& li': { mb: 0.5 }
              }}>
                <li>Shipping fees are not subject to tax.</li>
                <li>Orders above 100,000 VND get free shipping up to 25,000 VND (not applicable for rush delivery).</li>
                <li>Shipping fee calculated based on weight and delivery address.</li>
                <li>Rush delivery only available in Hanoi city center, additional 10,000 VND per item.</li>
                <li>Detailed fees will be displayed after entering delivery address.</li>
              </Box>
            </Box>
          </Box>
        </Paper>

        {/* Right side: Delivery form */}
        <Paper elevation={2} sx={{ flex: 1, minWidth: { xs: '100%', md: '350px' } }}>
          <Box sx={{ p: 3 }}>
            <Typography variant="h5" fontWeight={600} gutterBottom>
              Delivery Information
            </Typography>
            <Divider sx={{ mb: 2 }} />
            
            {processingOrder && (
                              <Alert severity="info" sx={{ mb: 2 }}>
                  <Box sx={{ display: 'flex', alignItems: 'center' }}>
                    <CircularProgress size={20} sx={{ mr: 1 }} />
                    Processing order...
                  </Box>
                </Alert>
            )}
            
            <DeliveryForm 
              onClose={() => navigate('/cart')} 
              onSuccess={handleDeliveryFormSuccess}
              disabled={processingOrder}
              initialValues={preservedDeliveryForm}
            />
          </Box>
        </Paper>
      </Box>
    </Box>
  );
};

export default CheckoutPage;