import React, { useState, useEffect } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import { Box, Typography, Divider, Paper, Button, CircularProgress, Alert, TextField } from '@mui/material';
import Header from '../components/Header';
import RushOrderResults from '../components/RushOrderResults';
import { checkRushOrderEligibility, submitRushOrderInfo, saveRushOrders, getProductDetails } from '../services/api';

const formatPrice = (price) => {
  return new Intl.NumberFormat('vi-VN', {
    style: 'currency',
    currency: 'VND'
  }).format(price);
};

const RushOrderPage = () => {
  const location = useLocation();
  const navigate = useNavigate();
  
  // Lấy dữ liệu từ location state
  const { cart, invoice, deliveryForm } = location.state || {};
  
  // States
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [rushInfo, setRushInfo] = useState({ 
    expectedDateTime: '', 
    deliveryInstructions: '' 
  });
  const [orderData, setOrderData] = useState(null);
  const [showResults, setShowResults] = useState(false);
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

  const handleRushOrder = async () => {
    if (!rushInfo.expectedDateTime) {
      setError('Vui lòng chọn thời gian mong muốn nhận hàng');
      return;
    }

    setLoading(true);
    setError('');
    try {
      // 1. Check eligibility
      await checkRushOrderEligibility();
      // 2. Submit rush info
      await submitRushOrderInfo(rushInfo);
      // 3. Save rush orders and get order IDs
      const res = await saveRushOrders();
      setOrderData(res.data);
      setShowResults(true);
    } catch (err) {
      setError(err.response?.data?.message || 'Có lỗi xảy ra, vui lòng thử lại.');
    } finally {
      setLoading(false);
    }
  };

  const handlePayment = (orderId, orderType, invoice) => {
    // Navigate to existing invoice page for payment
    navigate('/invoice', { 
      state: { 
        invoice: invoice,
        orderId: orderId,
        isRushOrder: orderType === 'rush',
        source: 'rush-order'
      }
    });
  };

  // Kiểm tra dữ liệu đầu vào
  if (!cart || !invoice || !deliveryForm) {
    return (
      <Box sx={{ minHeight: '100vh', backgroundColor: '#f5f5f5' }}>
        <Header />
        <Box sx={{ maxWidth: 1200, mx: 'auto', pt: 4, px: 2 }}>
          <Alert severity="error" sx={{ mb: 2 }}>
            Không tìm thấy thông tin đơn hàng. Vui lòng thử lại từ trang order review.
          </Alert>
          <Button 
            variant="contained" 
            onClick={() => navigate('/order-review')}
            sx={{ mt: 2 }}
          >
            Quay lại Order Review
          </Button>
        </Box>
      </Box>
    );
  }

  const productList = cart.productList || [];

  // Hiển thị kết quả rush order nếu đã xử lý xong
  if (showResults && orderData) {
    return (
      <Box sx={{ minHeight: '100vh', backgroundColor: '#f5f5f5' }}>
        <Header />
        <Box sx={{ pt: 4, px: 2 }}>
          <RushOrderResults 
            orderData={orderData}
            onPayment={handlePayment}
            expectedDateTime={rushInfo.expectedDateTime}
          />
        </Box>
      </Box>
    );
  }

  return (
    <Box sx={{ minHeight: '100vh', backgroundColor: '#f5f5f5' }}>
      <Header />
      
      <Box sx={{ maxWidth: 1200, mx: 'auto', pt: 4, px: 2 }}>
        <Typography variant="h4" gutterBottom sx={{ fontWeight: 600, mb: 4 }}>
          Giao hàng nhanh (Rush Order)
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
                Sản phẩm đã đặt ({productList.length} sản phẩm)
              </Typography>
              <Divider sx={{ mb: 2 }} />

              {loadingProducts ? (
                <Box sx={{ display: 'flex', justifyContent: 'center', py: 4 }}>
                  <CircularProgress />
                  <Typography sx={{ ml: 2 }}>Đang tải thông tin sản phẩm...</Typography>
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
                            {product.name || `Sản phẩm ${item.productID}`}
                          </Typography>
                          <Typography variant="body2" color="text.secondary" sx={{ mb: 1 }}>
                            ID: {item.productID}
                          </Typography>
                          <Typography variant="body2" color="text.secondary">
                            Đơn giá: {formatPrice(item.price)}
                          </Typography>
                        </Box>

                        {/* Số lượng và tổng tiền */}
                        <Box sx={{ textAlign: 'right', minWidth: 120 }}>
                          <Typography variant="body1" sx={{ mb: 1 }}>
                            Số lượng: {item.quantity}
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
                Chi tiết thanh toán (sẽ được tính lại với phí giao hàng nhanh)
              </Typography>
              <Divider sx={{ mb: 2 }} />

              <Box sx={{ display: 'flex', justifyContent: 'space-between', mb: 1 }}>
                <Typography>Tổng tiền hàng (chưa VAT):</Typography>
                <Typography>{formatPrice(invoice.productPriceExVAT)}</Typography>
              </Box>

              <Box sx={{ display: 'flex', justifyContent: 'space-between', mb: 1 }}>
                <Typography>Tổng tiền hàng (đã VAT):</Typography>
                <Typography>{formatPrice(invoice.productPriceIncVAT)}</Typography>
              </Box>

              <Box sx={{ display: 'flex', justifyContent: 'space-between', mb: 1 }}>
                <Typography>Phí giao hàng thường:</Typography>
                <Typography sx={{ 
                  color: 'text.secondary',
                  textDecoration: 'line-through'
                }}>
                  {formatPrice(invoice.shippingFee)}
                </Typography>
              </Box>

              <Box sx={{ display: 'flex', justifyContent: 'space-between', mb: 1 }}>
                <Typography sx={{ color: 'warning.main', fontWeight: 600 }}>
                  Phí giao hàng nhanh:
                </Typography>
                <Typography sx={{ color: 'warning.main', fontWeight: 600 }}>
                  Sẽ được tính lại
                </Typography>
              </Box>

              <Divider sx={{ my: 2 }} />

              <Box sx={{ display: 'flex', justifyContent: 'space-between' }}>
                <Typography variant="h6" sx={{ fontWeight: 600 }}>
                  Tổng cộng tạm tính:
                </Typography>
                <Typography variant="h6" color="primary" sx={{ fontWeight: 700 }}>
                  {formatPrice(invoice.totalAmount)}
                </Typography>
              </Box>

              <Typography variant="body2" color="warning.main" sx={{ mt: 1, fontStyle: 'italic' }}>
                * Tổng tiền cuối cùng sẽ được cập nhật sau khi tính phí giao hàng nhanh
              </Typography>
            </Paper>
          </Box>

          {/* Cột phải: Form rush order và thông tin giao hàng */}
          <Box sx={{ flex: 1 }}>
            {/* Thông tin giao hàng hiện tại */}
            <Paper elevation={2} sx={{ p: 3, mb: 3 }}>
              <Typography variant="h6" gutterBottom sx={{ fontWeight: 600 }}>
                Thông tin giao hàng
              </Typography>
              <Divider sx={{ mb: 2 }} />

              <Box sx={{ '& > *': { mb: 1.5 } }}>
                <Box>
                  <Typography variant="subtitle2" color="text.secondary">
                    Người nhận:
                  </Typography>
                  <Typography variant="body1" sx={{ fontWeight: 500 }}>
                    {deliveryForm.customerName}
                  </Typography>
                </Box>

                <Box>
                  <Typography variant="subtitle2" color="text.secondary">
                    Số điện thoại:
                  </Typography>
                  <Typography variant="body1">
                    {deliveryForm.phoneNumber}
                  </Typography>
                </Box>

                <Box>
                  <Typography variant="subtitle2" color="text.secondary">
                    Địa chỉ giao hàng:
                  </Typography>
                  <Typography variant="body1">
                    {deliveryForm.deliveryAddress}
                  </Typography>
                  <Typography variant="body2" color="text.secondary">
                    {deliveryForm.deliveryProvince}
                  </Typography>
                </Box>
              </Box>
            </Paper>

            {/* Form rush order */}
            <Paper elevation={2} sx={{ p: 3 }}>
              <Typography variant="h6" gutterBottom sx={{ fontWeight: 600 }}>
                Thông tin giao hàng nhanh
              </Typography>
              <Divider sx={{ mb: 3 }} />

              {error && (
                <Alert severity="error" sx={{ mb: 3 }}>
                  {error}
                </Alert>
              )}

              <Box sx={{ display: 'flex', flexDirection: 'column', gap: 3 }}>
                <Box>
                  <Typography variant="subtitle1" sx={{ mb: 1, fontWeight: 500 }}>
                    Thời gian mong muốn nhận hàng <span style={{ color: 'red' }}>*</span>
                  </Typography>
                  <TextField
                    type="datetime-local"
                    value={rushInfo.expectedDateTime}
                    onChange={(e) => setRushInfo({ ...rushInfo, expectedDateTime: e.target.value })}
                    fullWidth
                    variant="outlined"
                    InputLabelProps={{ shrink: true }}
                    inputProps={{
                      min: new Date().toISOString().slice(0, 16) // Chỉ cho phép chọn thời gian tương lai
                    }}
                  />
                  <Typography variant="caption" color="text.secondary">
                    Thời gian nhận hàng phải sau ít nhất 2 giờ từ bây giờ
                  </Typography>
                </Box>

                <Box>
                  <Typography variant="subtitle1" sx={{ mb: 1, fontWeight: 500 }}>
                    Hướng dẫn giao hàng (tùy chọn)
                  </Typography>
                  <TextField
                    multiline
                    rows={4}
                    value={rushInfo.deliveryInstructions}
                    onChange={(e) => setRushInfo({ ...rushInfo, deliveryInstructions: e.target.value })}
                    fullWidth
                    variant="outlined"
                    placeholder="Ví dụ: Gọi trước khi giao, để hàng tại bảo vệ, v.v..."
                  />
                </Box>

                <Alert severity="info" sx={{ mt: 2 }}>
                  <Typography variant="body2">
                    <strong>Lưu ý:</strong> Giao hàng nhanh có thể tách thành nhiều đơn hàng riêng biệt 
                    và phí giao hàng sẽ được tính lại theo mức phí giao hàng nhanh.
                  </Typography>
                </Alert>

                <Box sx={{ display: 'flex', flexDirection: 'column', gap: 2, mt: 3 }}>
                  <Button
                    variant="outlined"
                    onClick={() => navigate(-1)}
                    disabled={loading}
                    fullWidth
                  >
                    Quay lại
                  </Button>

                  <Button
                    variant="contained"
                    color="primary"
                    onClick={handleRushOrder}
                    disabled={loading || !rushInfo.expectedDateTime}
                    size="large"
                    fullWidth
                    sx={{ 
                      fontWeight: 600,
                      py: 1.5,
                      fontSize: '1rem'
                    }}
                  >
                    {loading ? (
                      <>
                        <CircularProgress size={20} sx={{ mr: 1 }} />
                        Đang xử lý...
                      </>
                    ) : (
                      'Xác nhận giao hàng nhanh'
                    )}
                  </Button>
                </Box>
              </Box>
            </Paper>
          </Box>
        </Box>
      </Box>
    </Box>
  );
};

export default RushOrderPage; 