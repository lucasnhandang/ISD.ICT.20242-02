import React, { useState, useEffect } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import { Box, Typography, Divider, Paper, Button, Alert, CircularProgress } from '@mui/material';
import Header from '../components/Header';
import { checkRushOrderEligibility, getProductDetails } from '../services/api';
import { getProvinceDisplayName } from '../utils/provinceUtils';

const formatPrice = (price) => {
  return new Intl.NumberFormat('vi-VN', {
    style: 'currency',
    currency: 'VND'
  }).format(price);
};

const OrderReviewPage = () => {
  const location = useLocation();
  const navigate = useNavigate();
  
  // Lấy dữ liệu từ API response (được truyền qua location.state)
  const { cart, invoice, deliveryForm, orderId } = location.state || {};
  
  // States
  const [rushError, setRushError] = useState('');
  const [loadingRush, setLoadingRush] = useState(false);
  const [productDetails, setProductDetails] = useState({});
  const [loadingProducts, setLoadingProducts] = useState(true);

  // Fetch product details khi component mount
  useEffect(() => {
    const fetchProductDetails = async () => {
      if (!cart?.productList || cart.productList.length === 0) {
        setLoadingProducts(false);
        return;
      }

      try {
        const details = {};
        
        // Fetch thông tin chi tiết cho từng sản phẩm
        for (const item of cart.productList) {
          if (item.productID) {
            try {
              const response = await getProductDetails(item.productID);
              details[item.productID] = {
                name: response.data.title || response.data.name || `Sản phẩm ${item.productID}`,
                description: response.data.description || '',
                image: response.data.imageUrl || response.data.image || null
              };
            } catch (error) {
              console.error(`Failed to fetch product ${item.productID}:`, error);
              details[item.productID] = {
                name: `Sản phẩm ${item.productID}`,
                description: '',
                image: null
              };
            }
          }
        }
        
        setProductDetails(details);
      } catch (error) {
        console.error('Error fetching product details:', error);
      } finally {
        setLoadingProducts(false);
      }
    };

    fetchProductDetails();
  }, [cart]);

  // Kiểm tra dữ liệu đầu vào
  if (!cart || !invoice || !deliveryForm) {
    return (
      <Box sx={{ minHeight: '100vh', backgroundColor: '#f5f5f5' }}>
        <Header />
        <Box sx={{ maxWidth: 1200, mx: 'auto', pt: 4, px: 2 }}>
          <Alert severity="error" sx={{ mb: 2 }}>
            Order information not found. Please try again from the checkout page.
          </Alert>
          <Button 
            variant="contained" 
            onClick={() => navigate('/checkout')}
            sx={{ mt: 2 }}
          >
            Back to Checkout
          </Button>
        </Box>
      </Box>
    );
  }

  // Xử lý rush order
  const handleRushOrder = async () => {
    setLoadingRush(true);
    setRushError('');
    
    try {
      const response = await checkRushOrderEligibility();
      
      if (response.data.eligible) {
        navigate('/rush-order', { 
          state: { cart, invoice, deliveryForm, orderId } 
        });
      } else {
        setRushError(response.data.message || 'Address is not eligible for rush delivery.');
      }
    } catch (error) {
      setRushError(
        error.response?.data?.message || 
        'An error occurred while checking rush delivery eligibility.'
      );
    } finally {
      setLoadingRush(false);
    }
  };

  // Xử lý thanh toán
  const handlePayment = () => {
    // Thêm productList vào invoice để hiển thị trong InvoicePage
    const invoiceWithProducts = {
      ...invoice,
      productList: cart.productList || []
    };

    navigate('/invoice', { 
      state: { 
        invoice: invoiceWithProducts,
        deliveryForm: deliveryForm,
        cart: cart,
        isRushOrder: false,
        orderId: orderId
      } 
    });
  };

  const productList = cart.productList || [];

  return (
    <Box sx={{ minHeight: '100vh', backgroundColor: '#f5f5f5' }}>
      <Header />
      
      <Box sx={{ maxWidth: 1200, mx: 'auto', pt: 4, px: 2 }}>
        <Typography variant="h4" gutterBottom sx={{ fontWeight: 600, mb: 4 }}>
          Order Confirmation
        </Typography>

        <Box sx={{ 
          display: 'flex', 
          flexDirection: { xs: 'column', lg: 'row' },
          gap: 3
        }}>
          {/* Cột trái: Thông tin đơn hàng */}
          <Box sx={{ flex: 2 }}>
            {/* Danh sách sản phẩm */}
            <Paper elevation={2} sx={{ p: 3, mb: 3 }}>
              <Typography variant="h6" gutterBottom sx={{ fontWeight: 600 }}>
                Ordered Products ({productList.length} items)
              </Typography>
              <Divider sx={{ mb: 2 }} />

              {loadingProducts ? (
                <Box sx={{ display: 'flex', justifyContent: 'center', py: 4 }}>
                  <CircularProgress />
                  <Typography sx={{ ml: 2 }}>Loading product information...</Typography>
                </Box>
              ) : (
                <Box>
                  {productList.map((item, index) => {
                    const product = productDetails[item.productID] || {};
                    const subtotal = item.price * item.quantity;
                    
                    return (
                      <Box
                        key={item.productID || index}
                        sx={{
                          display: 'flex',
                          alignItems: 'center',
                          py: 2,
                          borderBottom: index < productList.length - 1 ? '1px solid #e0e0e0' : 'none'
                        }}
                      >
                        {/* Hình ảnh sản phẩm */}
                        <Box
                          sx={{
                            width: 80,
                            height: 80,
                            backgroundColor: '#f0f0f0',
                            borderRadius: 1,
                            mr: 2,
                            display: 'flex',
                            alignItems: 'center',
                            justifyContent: 'center',
                            flexShrink: 0
                          }}
                        >
                          {product.image ? (
                            <img
                              src={product.image}
                              alt={product.name}
                              style={{
                                width: '100%',
                                height: '100%',
                                objectFit: 'cover',
                                borderRadius: 4
                              }}
                            />
                          ) : (
                            <Typography variant="caption" color="text.secondary">
                              No Image
                            </Typography>
                          )}
                        </Box>

                        {/* Thông tin sản phẩm */}
                        <Box sx={{ flex: 1 }}>
                          <Typography variant="h6" sx={{ fontWeight: 500, mb: 0.5 }}>
                            {product.name || `Product ${item.productID}`}
                          </Typography>
                          <Typography variant="body2" color="text.secondary">
                            Unit Price: {formatPrice(item.price)}
                          </Typography>
                        </Box>

                        {/* Số lượng và tổng tiền */}
                        <Box sx={{ textAlign: 'right', minWidth: 120 }}>
                          <Typography variant="body1" sx={{ mb: 1 }}>
                            Quantity: {item.quantity}
                          </Typography>
                          <Typography variant="h6" color="primary" sx={{ fontWeight: 600 }}>
                            {formatPrice(subtotal)}
                          </Typography>
                        </Box>
                      </Box>
                    );
                  })}
                </Box>
              )}
            </Paper>

            {/* Thông tin hóa đơn */}
            <Paper elevation={2} sx={{ p: 3 }}>
              <Typography variant="h6" gutterBottom sx={{ fontWeight: 600 }}>
                Payment Details
              </Typography>
              <Divider sx={{ mb: 2 }} />

              <Box sx={{ display: 'flex', justifyContent: 'space-between', mb: 1 }}>
                <Typography>Product Total (excl. VAT):</Typography>
                <Typography>{formatPrice(invoice.productPriceExVAT)}</Typography>
              </Box>

              <Box sx={{ display: 'flex', justifyContent: 'space-between', mb: 1 }}>
                <Typography>Product Total (incl. VAT):</Typography>
                <Typography>{formatPrice(invoice.productPriceIncVAT)}</Typography>
              </Box>

              <Box sx={{ display: 'flex', justifyContent: 'space-between', mb: 1 }}>
                <Typography>Shipping Fee:</Typography>
                <Typography sx={{ 
                  color: invoice.shippingFee > 0 ? 'orange' : 'inherit',
                  fontWeight: invoice.shippingFee > 0 ? 600 : 400
                }}>
                  {formatPrice(invoice.shippingFee)}
                </Typography>
              </Box>

              <Divider sx={{ my: 2 }} />

              <Box sx={{ display: 'flex', justifyContent: 'space-between' }}>
                <Typography variant="h6" sx={{ fontWeight: 600 }}>
                  Total Amount:
                </Typography>
                <Typography variant="h6" color="primary" sx={{ fontWeight: 700 }}>
                  {formatPrice(invoice.totalAmount)}
                </Typography>
              </Box>

              <Typography variant="body2" color="text.secondary" sx={{ mt: 1 }}>
                Currency: {cart.currency || 'VND'}
              </Typography>
            </Paper>
          </Box>

          {/* Cột phải: Thông tin giao hàng và hành động */}
          <Box sx={{ flex: 1 }}>
            {/* Thông tin giao hàng */}
            <Paper elevation={2} sx={{ p: 3, mb: 3 }}>
              <Typography variant="h6" gutterBottom sx={{ fontWeight: 600 }}>
                Delivery Information
              </Typography>
              <Divider sx={{ mb: 2 }} />

              <Box sx={{ '& > *': { mb: 1.5 } }}>
                <Box>
                  <Typography variant="subtitle2" color="text.secondary">
                    Recipient:
                  </Typography>
                  <Typography variant="body1" sx={{ fontWeight: 500 }}>
                    {deliveryForm.customerName}
                  </Typography>
                </Box>

                <Box>
                  <Typography variant="subtitle2" color="text.secondary">
                    Phone Number:
                  </Typography>
                  <Typography variant="body1">
                    {deliveryForm.phoneNumber}
                  </Typography>
                </Box>

                <Box>
                  <Typography variant="subtitle2" color="text.secondary">
                    Email:
                  </Typography>
                  <Typography variant="body1">
                    {deliveryForm.email}
                  </Typography>
                </Box>

                <Box>
                  <Typography variant="subtitle2" color="text.secondary">
                    Delivery Address:
                  </Typography>
                  <Typography variant="body1">
                    {deliveryForm.deliveryAddress}
                  </Typography>
                  <Typography variant="body2" color="text.secondary">
                    {getProvinceDisplayName(deliveryForm.deliveryProvince)}
                  </Typography>
                </Box>

                {deliveryForm.deliveryInstructions && (
                  <Box>
                    <Typography variant="subtitle2" color="text.secondary">
                      Notes:
                    </Typography>
                    <Typography variant="body1">
                      {deliveryForm.deliveryInstructions}
                    </Typography>
                  </Box>
                )}

                <Box>
                  <Typography variant="subtitle2" color="text.secondary">
                    Rush Delivery:
                  </Typography>
                  <Typography variant="body1" sx={{ 
                    color: deliveryForm.rushOrder ? 'success.main' : 'text.primary',
                    fontWeight: deliveryForm.rushOrder ? 600 : 400
                  }}>
                    {deliveryForm.rushOrder ? 'Yes' : 'No'}
                  </Typography>
                </Box>
              </Box>
            </Paper>

            {/* Hành động */}
            <Paper elevation={2} sx={{ p: 3 }}>
              <Typography variant="h6" gutterBottom sx={{ fontWeight: 600 }}>
                Actions
              </Typography>
              <Divider sx={{ mb: 2 }} />

              {rushError && (
                <Alert severity="error" sx={{ mb: 2 }}>
                  {rushError}
                </Alert>
              )}

              <Box sx={{ display: 'flex', flexDirection: 'column', gap: 2 }}>
                <Button
                  variant="outlined"
                  onClick={() => navigate('/checkout', { 
                    state: { preservedDeliveryForm: deliveryForm } 
                  })}
                  fullWidth
                >
                  Back to Checkout
                </Button>

                <Button
                  variant="outlined"
                  color="secondary"
                  onClick={handleRushOrder}
                  disabled={loadingRush}
                  fullWidth
                >
                  {loadingRush ? (
                    <>
                      <CircularProgress size={20} sx={{ mr: 1 }} />
                      Checking...
                    </>
                  ) : (
                    'Choose Rush Delivery'
                  )}
                </Button>

                <Button
                  variant="contained"
                  color="primary"
                  onClick={handlePayment}
                  size="large"
                  fullWidth
                  sx={{ 
                    fontWeight: 600,
                    py: 1.5,
                    fontSize: '1rem'
                  }}
                >
                  Proceed to Payment
                </Button>
              </Box>
            </Paper>
          </Box>
        </Box>
      </Box>
    </Box>
  );
};

export default OrderReviewPage;