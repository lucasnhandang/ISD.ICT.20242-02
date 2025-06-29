import React, { useState } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import { Box, Typography, Divider, Paper, Button, Alert, CircularProgress } from '@mui/material';
import Header from '../components/Header';
import { createVnPayUrl } from '../services/api';

const formatPrice = (price) => {
  return new Intl.NumberFormat('vi-VN', {
    style: 'currency',
    currency: 'VND'
  }).format(price);
};

const InvoicePage = () => {
  const location = useLocation();
  const navigate = useNavigate();
  const { invoice, invoiceList, deliveryForm, isRushOrder, orderId } = location.state || {};
  
  // State để quản lý trạng thái thanh toán
  const [paymentLoading, setPaymentLoading] = useState({});
  const [paymentError, setPaymentError] = useState({});

  const invoices = invoiceList || (invoice ? [invoice] : []);

  const handlePay = async (inv, index) => {
    const invoiceKey = inv.id || index;
    
    // Set loading state
    setPaymentLoading(prev => ({ ...prev, [invoiceKey]: true }));
    setPaymentError(prev => ({ ...prev, [invoiceKey]: '' }));
    
    try {
      // Tạo VnPay payment data
      const vnPayData = {
        amount: inv.totalAmount,
        orderInfo: `Thanh toan hoa don ${orderId || inv.id || 'N/A'}`,
        orderType: "other",
        locale: "vn",
        orderId: orderId || null
      };
      
      // Gọi API tạo VnPay URL
      const response = await createVnPayUrl(vnPayData);
      
      if (response.data.paymentUrl) {
        // Mở tab mới với VnPay URL thay vì redirect trang hiện tại
        const paymentWindow = window.open(response.data.paymentUrl, '_blank');
        
        // Kiểm tra xem có mở được tab mới không
        if (!paymentWindow) {
          throw new Error('Trình duyệt đã chặn popup. Vui lòng cho phép popup và thử lại.');
        }
        
        // Hiển thị thông báo cho user
        alert(`Đã mở trang thanh toán VnPay trong tab mới. Tổng tiền: ${formatPrice(inv.totalAmount)}`);
        
      } else {
        throw new Error('Không thể tạo URL thanh toán');
      }
      
    } catch (error) {
      console.error('Payment error:', error);
      setPaymentError(prev => ({ 
        ...prev, 
        [invoiceKey]: error.response?.data?.message || error.message || 'Có lỗi khi tạo liên kết thanh toán' 
      }));
    } finally {
      setPaymentLoading(prev => ({ ...prev, [invoiceKey]: false }));
    }
  };

  if (!invoices.length) {
    return (
      <Box sx={{ minHeight: '100vh', backgroundColor: '#f5f5f5' }}>
        <Header />
        <Box sx={{ maxWidth: 1200, mx: 'auto', pt: 4, px: 2 }}>
          <Alert severity="error" sx={{ mb: 2 }}>
            Không có hóa đơn để thanh toán.
          </Alert>
          <Button 
            variant="contained" 
            onClick={() => navigate('/checkout')}
            sx={{ mt: 2 }}
          >
            Quay lại Checkout
          </Button>
        </Box>
      </Box>
    );
  }

  return (
    <Box sx={{ minHeight: '100vh', backgroundColor: '#f5f5f5' }}>
      <Header />
      <Box sx={{ maxWidth: 1200, mx: 'auto', pt: 4, px: 2 }}>
        <Typography variant="h4" gutterBottom sx={{ fontWeight: 600, mb: 4 }}>
          Thanh toán hóa đơn
        </Typography>
        
        <Alert severity="info" sx={{ mb: 3 }}>
          <Typography variant="body2">
            <strong>Hướng dẫn thanh toán:</strong> Nhấn nút "Thanh toán VnPay" để mở trang thanh toán trong tab mới. 
            Sau khi thanh toán xong, hệ thống sẽ tự động cập nhật trạng thái đơn hàng.
          </Typography>
        </Alert>

        <Box sx={{ display: 'flex', flexDirection: 'column', gap: 3 }}>
          {invoices.map((inv, idx) => {
            const invoiceKey = inv.id || idx;
            const isLoading = paymentLoading[invoiceKey] || false;
            const error = paymentError[invoiceKey] || '';
            
            return (
              <Paper key={invoiceKey} elevation={2} sx={{ p: 3 }}>
                <Typography variant="h6" sx={{ fontWeight: 600, mb: 2 }}>
                  Hóa đơn #{inv.id || (idx + 1)}
                </Typography>
                <Divider sx={{ mb: 2 }} />

                {/* Thông tin sản phẩm */}
                <Typography variant="subtitle1" sx={{ fontWeight: 500, mb: 1 }}>
                  Danh sách sản phẩm:
                </Typography>
                {inv.productList && inv.productList.length > 0 ? (
                  <Box sx={{ mb: 2 }}>
                    {inv.productList.map((item, itemIdx) => (
                      <Box 
                        key={item.productID || itemIdx} 
                        sx={{ 
                          display: 'flex', 
                          justifyContent: 'space-between', 
                          alignItems: 'center',
                          py: 1,
                          borderBottom: '1px solid #f0f0f0'
                        }}
                      >
                        <Box sx={{ flex: 1 }}>
                          <Typography variant="body1" sx={{ fontWeight: 500 }}>
                            {item.productName || item.name || `Sản phẩm ${item.productID}`}
                          </Typography>
                          <Typography variant="body2" color="text.secondary">
                            ID: {item.productID}
                          </Typography>
                          <Typography variant="body2" color="text.secondary">
                            Đơn giá: {formatPrice(item.price)}
                          </Typography>
                        </Box>
                        <Box sx={{ display: 'flex', alignItems: 'center', gap: 2 }}>
                          <Typography variant="body1">
                            x {item.quantity}
                          </Typography>
                          <Typography variant="body1" sx={{ fontWeight: 500, minWidth: 100, textAlign: 'right' }}>
                            {formatPrice(item.price * item.quantity)}
                          </Typography>
                        </Box>
                      </Box>
                    ))}
                  </Box>
                ) : (
                  <Typography color="text.secondary" sx={{ mb: 2 }}>
                    Không có sản phẩm trong hóa đơn này.
                  </Typography>
                )}

                {/* Chi tiết thanh toán */}
                <Box sx={{ mt: 3, p: 2, backgroundColor: '#f9f9f9', borderRadius: 1 }}>
                  {!isRushOrder && inv.productPriceExVAT && (
                    <Box sx={{ display: 'flex', justifyContent: 'space-between', mb: 1 }}>
                      <Typography>Tổng tiền hàng (chưa VAT):</Typography>
                      <Typography>{formatPrice(inv.productPriceExVAT)}</Typography>
                    </Box>
                  )}
                  
                  <Box sx={{ display: 'flex', justifyContent: 'space-between', mb: 1 }}>
                    <Typography>Tổng tiền hàng{!isRushOrder ? ' (đã VAT)' : ''}:</Typography>
                    <Typography>{formatPrice(inv.productPriceIncVAT || inv.subtotal || 0)}</Typography>
                  </Box>
                  
                  <Box sx={{ display: 'flex', justifyContent: 'space-between', mb: 1 }}>
                    <Typography>Phí giao hàng:</Typography>
                    <Typography sx={{ 
                      color: isRushOrder ? 'warning.main' : 'inherit',
                      fontWeight: isRushOrder ? 600 : 400
                    }}>
                      {formatPrice(inv.shippingFee || 0)}
                      {isRushOrder && (
                        <Typography component="span" sx={{ fontSize: '0.8rem', ml: 1 }}>
                          (Nhanh)
                        </Typography>
                      )}
                    </Typography>
                  </Box>
                  
                  <Divider sx={{ my: 1 }} />
                  
                  <Box sx={{ display: 'flex', justifyContent: 'space-between' }}>
                    <Typography variant="h6" sx={{ fontWeight: 600 }}>
                      Tổng cộng:
                    </Typography>
                    <Typography variant="h6" color="primary" sx={{ fontWeight: 700 }}>
                      {formatPrice(inv.totalAmount)}
                    </Typography>
                  </Box>
                </Box>

                {/* Error message */}
                {error && (
                  <Alert severity="error" sx={{ mt: 2 }}>
                    {error}
                  </Alert>
                )}

                {/* Action buttons */}
                <Box sx={{ display: 'flex', gap: 2, mt: 3 }}>
                  <Button 
                    variant="outlined" 
                    color="secondary" 
                    onClick={() => navigate(-1)}
                    disabled={isLoading}
                  >
                    Quay lại
                  </Button>
                  <Button 
                    variant="contained" 
                    color="primary"
                    onClick={() => handlePay(inv, idx)} 
                    disabled={isLoading}
                    sx={{ minWidth: 150 }}
                  >
                    {isLoading ? (
                      <>
                        <CircularProgress size={20} sx={{ mr: 1 }} />
                        Đang tạo liên kết...
                      </>
                    ) : (
                      'Thanh toán VnPay'
                    )}
                  </Button>
                </Box>
              </Paper>
            );
          })}
        </Box>

        {/* Thông tin giao hàng */}
        {deliveryForm && (
          <Paper elevation={2} sx={{ p: 3, mt: 3 }}>
            <Typography variant="h6" gutterBottom sx={{ fontWeight: 600 }}>
              Thông tin giao hàng
            </Typography>
            <Divider sx={{ mb: 2 }} />
            
            <Box sx={{ display: 'grid', gap: 2, gridTemplateColumns: { xs: '1fr', md: '1fr 1fr' } }}>
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
              
              <Box sx={{ gridColumn: { xs: '1', md: '1 / -1' } }}>
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
              
              {deliveryForm.expectedDateTime && (
                <Box>
                  <Typography variant="subtitle2" color="text.secondary">
                    Thời gian mong muốn:
                  </Typography>
                  <Typography variant="body1" sx={{ color: 'warning.main', fontWeight: 500 }}>
                    {new Date(deliveryForm.expectedDateTime).toLocaleString('vi-VN')}
                  </Typography>
                </Box>
              )}
              
              {deliveryForm.deliveryInstructions && (
                <Box>
                  <Typography variant="subtitle2" color="text.secondary">
                    Ghi chú:
                  </Typography>
                  <Typography variant="body1">
                    {deliveryForm.deliveryInstructions}
                  </Typography>
                </Box>
              )}
            </Box>
          </Paper>
        )}
      </Box>
    </Box>
  );
};

export default InvoicePage;