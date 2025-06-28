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
import { useNavigate } from 'react-router-dom';
import Header from '../components/Header';
import DeliveryForm from '../components/DeliveryForm';
import { 
  getCart, 
  requestToPlaceOrder, 
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

  // Fetch cart data
  useEffect(() => {
    const fetchCart = async () => {
      try {
        setLoading(true);
        const response = await getCart();
        
        if (!response.data || !response.data.productList) {
          setError('Giỏ hàng trống hoặc không hợp lệ.');
          return;
        }
        
        setCart(response.data);
      } catch (err) {
        console.error('Error fetching cart:', err);
        setError(err.response?.data?.message || 'Không thể tải giỏ hàng.');
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

  // Handle normal order submission
  const handleNormalOrderSubmit = async () => {
    try {
      setProcessingOrder(true);
      setError('');
      
      const response = await handleNormalOrder();
      
      if (!response.data) {
        throw new Error('Không nhận được phản hồi từ server');
      }
      
      const { cart: apiCart, invoice, deliveryForm } = response.data;
      
      console.log('Normal order response:', response.data);
      
      // Navigate to OrderReviewPage with complete data
      navigate('/order-review', {
        state: {
          cart: apiCart || cart,
          invoice: invoice,
          deliveryForm: deliveryForm
        }
      });
      
    } catch (error) {
      console.error('Failed to process normal order:', error);
      setError(error.response?.data?.message || 'Có lỗi xảy ra khi xử lý đơn hàng');
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
        throw new Error('Không tìm thấy thông tin giỏ hàng');
      }

      // Submit delivery form and process order
      await submitDeliveryForm(deliveryForm);
      await requestToPlaceOrder(cart);
      
      const invoiceResponse = await handleNormalOrder();
      
      if (!invoiceResponse.data) {
        throw new Error('Không nhận được thông tin hóa đơn');
      }

      // Navigate with complete data
      navigate('/order-review', { 
        state: { 
          cart: invoiceResponse.data.cart || cart,
          invoice: invoiceResponse.data.invoice, 
          deliveryForm: deliveryForm
        } 
      });
      
    } catch (err) {
      console.error('Error processing delivery form:', err);
      setError(err.response?.data?.message || 'Có lỗi xảy ra, vui lòng thử lại.');
    } finally {
      setProcessingOrder(false);
    }
  };

  // Loading state
  if (loading) {
    return (
      <Box sx={{ display: 'flex', justifyContent: 'center', alignItems: 'center', minHeight: '100vh' }}>
        <CircularProgress size={60} />
        <Typography sx={{ ml: 2 }}>Đang tải giỏ hàng...</Typography>
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
            Quay lại giỏ hàng
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
            Giỏ hàng của bạn đang trống
          </Alert>
          <Button variant="contained" onClick={() => navigate('/products')}>
            Tiếp tục mua sắm
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
              Thông tin đơn hàng
            </Typography>
            <Divider sx={{ mb: 2 }} />
            
            {/* Products list */}
            <Box sx={{ mb: 3 }}>
              {fetchingProducts && (
                <Box sx={{ display: 'flex', alignItems: 'center', mb: 2 }}>
                  <CircularProgress size={20} sx={{ mr: 1 }} />
                  <Typography variant="body2" color="text.secondary">
                    Đang tải thông tin sản phẩm...
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
                          <Typography variant="body2" color="text.secondary">
                            ID: {item.productID}
                          </Typography>
                          <Box sx={{ display: 'flex', justifyContent: 'space-between', mt: 1 }}>
                            <Typography variant="body2">
                              Số lượng: <strong>{item.quantity}</strong>
                            </Typography>
                            <Typography variant="body2">
                              Đơn giá: {formatPrice(item.price, cart.currency)}
                            </Typography>
                          </Box>
                          <Typography variant="body2" color="primary" fontWeight={500}>
                            Thành tiền: {formatPrice(item.price * item.quantity, cart.currency)}
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
                <Typography>Giá sản phẩm (chưa VAT):</Typography>
                <Typography>
                  {formatPrice(cart.totalPrice - (cart.discount || 0), cart.currency)}
                </Typography>
              </Box>
              
              {cart.discount > 0 && (
                <Box sx={{ display: 'flex', justifyContent: 'space-between', mb: 1 }}>
                  <Typography color="success.main">Giảm giá:</Typography>
                  <Typography color="success.main">
                    -{formatPrice(cart.discount, cart.currency)}
                  </Typography>
                </Box>
              )}
              
              <Box sx={{ display: 'flex', justifyContent: 'space-between', mb: 1 }}>
                <Typography>Giá sản phẩm (đã VAT):</Typography>
                <Typography>
                  {formatPrice(cart.totalPrice, cart.currency)}
                </Typography>
              </Box>
              
              <Typography color="text.secondary" variant="body2" sx={{ mt: 2, fontStyle: 'italic' }}>
                * Phí giao hàng, phí giao nhanh (nếu có) sẽ được tính sau khi nhập địa chỉ giao hàng.
              </Typography>
            </Box>

            <Divider sx={{ my: 2 }} />
            <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
              <Typography variant="h6" color="primary" fontWeight={600}>
                Tổng cộng:
              </Typography>
              <Typography variant="h6" color="primary" fontWeight={600}>
                {formatPrice(cart.totalPrice - (cart.discount || 0), cart.currency)}
              </Typography>
            </Box>

            {/* Shipping policy */}
            <Divider sx={{ my: 2 }} />
            <Box>
              <Typography variant="subtitle2" color="text.secondary" gutterBottom>
                📋 Quy định về phí giao hàng và giao nhanh:
              </Typography>
              <Box component="ul" sx={{ 
                pl: 2, 
                color: 'text.secondary', 
                fontSize: '0.875rem',
                '& li': { mb: 0.5 }
              }}>
                <li>Phí giao hàng không chịu thuế.</li>
                <li>Đơn hàng trên 100,000 VND được miễn phí ship tối đa 25,000 VND (không áp dụng cho giao nhanh).</li>
                <li>Phí giao hàng tính theo cân nặng và địa chỉ nhận hàng.</li>
                <li>Giao nhanh chỉ áp dụng cho nội thành Hà Nội, phí cộng thêm 10,000 VND/món.</li>
                <li>Chi tiết phí sẽ hiển thị sau khi nhập địa chỉ giao hàng.</li>
              </Box>
            </Box>
          </Box>
        </Paper>

        {/* Right side: Delivery form */}
        <Paper elevation={2} sx={{ flex: 1, minWidth: { xs: '100%', md: '350px' } }}>
          <Box sx={{ p: 3 }}>
            <Typography variant="h5" fontWeight={600} gutterBottom>
              Thông tin giao hàng
            </Typography>
            <Divider sx={{ mb: 2 }} />
            
            {processingOrder && (
              <Alert severity="info" sx={{ mb: 2 }}>
                <Box sx={{ display: 'flex', alignItems: 'center' }}>
                  <CircularProgress size={20} sx={{ mr: 1 }} />
                  Đang xử lý đơn hàng...
                </Box>
              </Alert>
            )}
            
            <DeliveryForm 
              onClose={() => navigate('/cart')} 
              onSuccess={handleDeliveryFormSuccess}
              disabled={processingOrder}
            />
          </Box>
        </Paper>
      </Box>
    </Box>
  );
};

export default CheckoutPage;