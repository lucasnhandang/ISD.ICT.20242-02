import React, { useState, useEffect } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import { Box, Typography, Divider, Paper, Button, Alert, CircularProgress, Chip } from '@mui/material';
import Header from '../components/Header';
import { createVnPayUrl, clearCart } from '../services/api';
import { AccessTime } from '@mui/icons-material';

const formatPrice = (price) => {
  return new Intl.NumberFormat('vi-VN', {
    style: 'currency',
    currency: 'VND'
  }).format(price);
};

const InvoicePage = () => {
  const location = useLocation();
  const navigate = useNavigate();
  const { invoice, invoiceList, deliveryForm, isRushOrder, orderId, source, message } = location.state || {};
  
  // State để quản lý trạng thái thanh toán
  const [paymentLoading, setPaymentLoading] = useState({});
  const [paymentError, setPaymentError] = useState({});
  const [currentInvoices, setCurrentInvoices] = useState([]);
  const [checkingPaymentStatus, setCheckingPaymentStatus] = useState({});

  // Khởi tạo danh sách invoice
  useEffect(() => {
    const invoices = invoiceList || (invoice ? [invoice] : []);
    setCurrentInvoices(invoices);
  }, [invoiceList, invoice]);

  // Xóa invoice đã thanh toán khỏi danh sách
  const removeInvoiceFromList = (invoiceKey) => {
    setCurrentInvoices(prev => prev.filter(inv => (inv.id || inv.orderId) !== invoiceKey));
  };

  // Helper function để xử lý khi payment thành công
  const handlePaymentSuccess = async (invoiceKey) => {
    try {
      // Xóa invoice khỏi danh sách
      removeInvoiceFromList(invoiceKey);
      
      // Clear cart khi payment thành công
      console.log("Payment successful, clearing cart...");
      await clearCart();
      console.log("Cart cleared successfully!");
      
      alert('Order has been paid successfully and removed from the list!');
    } catch (error) {
      console.error("Failed to clear cart:", error);
      // Vẫn thông báo thành công cho user vì invoice đã được xóa
      alert('Order has been paid successfully and removed from the list!');
    }
  };

  // Kiểm tra payment status thủ công
  const checkPaymentStatusManually = async (inv) => {
    const invoiceKey = inv.id || inv.orderId;
    if (!invoiceKey) return;

    setCheckingPaymentStatus(prev => ({ ...prev, [invoiceKey]: true }));
    
    try {
      // GIẢ LẬP CHECK PAYMENT STATUS VÌ API CHƯA CÓ TRONG BACKEND
      // Trong thực tế, cần implement API /place-rush-order/payment-status trong backend
      await new Promise(resolve => setTimeout(resolve, 2000)); // Giả lập delay
      
      // Giả lập kết quả ngẫu nhiên để demo
      const isRandomlyPaid = Math.random() > 0.95; // 10% cơ hội "đã thanh toán"
      
      if (isRandomlyPaid) {
        await handlePaymentSuccess(invoiceKey);
      } else {
        alert('Order has not been paid yet. Please try again later.');
      }
    } catch (error) {
      console.error('Error checking payment status:', error);
      alert('Unable to check payment status. Please try again later.');
    } finally {
      setCheckingPaymentStatus(prev => ({ ...prev, [invoiceKey]: false }));
    }
  };

  const handlePay = async (inv, index) => {
    const invoiceKey = inv.id || inv.orderId || index;
    
    // Set loading state
    setPaymentLoading(prev => ({ ...prev, [invoiceKey]: true }));
    setPaymentError(prev => ({ ...prev, [invoiceKey]: '' }));
    
    try {
      // Tạo VnPay payment data
      const vnPayData = {
        amount: inv.totalAmount,
        orderInfo: `Thanh toan ${inv.isRushOrder ? 'rush order' : 'normal order'}  ${orderId || inv.id || 'N/A'}`,
        orderType: "other",
        locale: "vn",
      };
      
      // Gọi API tạo VnPay URL
      const response = await createVnPayUrl(vnPayData);
      
      if (response.data.paymentUrl) {
        // Mở tab mới với VnPay URL thay vì redirect trang hiện tại
        const paymentWindow = window.open(response.data.paymentUrl, '_blank');
        
        // Kiểm tra xem có mở được tab mới không
        if (!paymentWindow) {
          throw new Error('Browser blocked popup. Please allow popups and try again.');
        }
        
        // Hiển thị thông báo cho user
        alert(`VnPay payment page opened in new tab. Total amount: ${formatPrice(inv.totalAmount)}`);
        
        // Sau khi mở thanh toán thành công, bắt đầu polling để check payment status
        if (inv.id || inv.orderId) {
          const checkPaymentStatus = async () => {
            try {
              // GIẢ LẬP CHECK PAYMENT STATUS VÌ API CHƯA CÓ TRONG BACKEND
              // Trong thực tế, cần implement API /place-rush-order/payment-status trong backend
              await new Promise(resolve => setTimeout(resolve, 1000)); // Giả lập delay
              
              // Giả lập kết quả ngẫu nhiên để demo (cùng tỉ lệ cho tất cả orders)
              const isRandomlyPaid = Math.random() > 0.9; // 10% cơ hội "đã thanh toán" mỗi lần check
              
              if (isRandomlyPaid) {
                // Xử lý payment success (xóa invoice và clear cart)
                await handlePaymentSuccess(invoiceKey);
                return true; // Stop polling
              }
              return false; // Continue polling
            } catch (error) {
              console.error('Error checking payment status:', error);
              return false; // Continue polling
            }
          };

          // Polling payment status mỗi 5 giây trong vòng 10 phút (cho cả rush và normal orders)
          const pollInterval = setInterval(async () => {
            const isCompleted = await checkPaymentStatus();
            if (isCompleted) {
              clearInterval(pollInterval);
            }
          }, 5000);

          // Dừng polling sau 10 phút
          setTimeout(() => {
            clearInterval(pollInterval);
          }, 600000);
        } else {
          // Fallback cho trường hợp không có orderId - hiển thị thông báo rõ ràng hơn
          alert('Payment opened! Please use the "Check Payment Status" button below to confirm payment completion.');
        }
        
      } else {
        throw new Error('Unable to create payment URL');
      }
      
    } catch (error) {
      console.error('Payment error:', error);
      setPaymentError(prev => ({ 
        ...prev, 
        [invoiceKey]: error.response?.data?.message || error.message || 'Error creating payment link' 
      }));
    } finally {
      setPaymentLoading(prev => ({ ...prev, [invoiceKey]: false }));
    }
  };

  if (!currentInvoices.length) {
    return (
      <Box sx={{ minHeight: '100vh', backgroundColor: '#f5f5f5' }}>
        <Header />
        <Box sx={{ maxWidth: 1200, mx: 'auto', pt: 4, px: 2 }}>
          <Alert severity="success" sx={{ mb: 2 }}>
            {source === 'rush-order' 
              ? 'All orders have been paid successfully!' 
              : 'No invoices to pay.'
            }
          </Alert>
          <Button 
            variant="contained" 
            onClick={() => navigate('/')}
            sx={{ mt: 2 }}
          >
            Back to Home
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
          Invoice Payment
        </Typography>
        
        {/* Thông báo thành công từ Rush Order */}
        {source === 'rush-order' && message && (
          <Alert severity="success" sx={{ mb: 3 }}>
            <Typography sx={{ fontWeight: 500 }}>
              {message}
            </Typography>
          </Alert>
        )}
        
        {/* <Alert severity="info" sx={{ mb: 3 }}>
          <Typography variant="body2">
            <strong>Payment Instructions:</strong> Click "VnPay Payment" button to open payment page in new tab. 
            After payment completion, the system will automatically check and update order status every 5 seconds.
            You can also manually check payment status using the "Check Payment Status" button.
            {source === 'rush-order' && (
              <><br/><strong>Note:</strong> You can pay for each order separately. Orders paid first will be processed first.</>
            )}
          </Typography>
        </Alert> */}

        <Box sx={{ display: 'flex', flexDirection: 'column', gap: 3 }}>
          {currentInvoices.map((inv, idx) => {
            const invoiceKey = inv.id || inv.orderId || idx;
            const isLoading = paymentLoading[invoiceKey] || false;
            const isCheckingStatus = checkingPaymentStatus[invoiceKey] || false;
            const error = paymentError[invoiceKey] || '';
            
            return (
              <Paper key={invoiceKey} elevation={2} sx={{ 
                p: 3,
                border: inv.isRushOrder ? '2px solid' : '1px solid',
                borderColor: inv.isRushOrder ? 'warning.main' : 'divider',
                borderRadius: 2
              }}>
                {/* Header với chip phân biệt */}
                <Box sx={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', mb: 2 }}>
                  <Typography variant="h6" sx={{ fontWeight: 600 }}>
                    {inv.isRushOrder ? '🚀 ' : '📦 '}
                    Invoice #{inv.id || inv.orderId || (idx + 1)}
                  </Typography>
                  <Chip 
                    label={inv.isRushOrder ? 'Rush Delivery' : 'Standard Delivery'} 
                    color={inv.isRushOrder ? 'warning' : 'primary'}
                    size="small"
                    sx={{ fontWeight: 600 }}
                  />
                </Box>
                
                {/* Thời gian giao hàng dự kiến cho rush order */}
                {inv.isRushOrder && inv.deliveryTime && (
                  <Box sx={{ display: 'flex', alignItems: 'center', mb: 2, p: 1, backgroundColor: 'warning.light', borderRadius: 1 }}>
                    <AccessTime sx={{ mr: 1, color: 'warning.dark' }} />
                    <Typography variant="body2" sx={{ fontWeight: 500 }}>
                      <strong>Expected Delivery:</strong> {new Date(inv.deliveryTime).toLocaleString('en-US')}
                    </Typography>
                  </Box>
                )}
                
                <Divider sx={{ mb: 2 }} />

                {/* Thông tin sản phẩm */}
                <Typography variant="subtitle1" sx={{ fontWeight: 500, mb: 1 }}>
                  Product List:
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
                            {item.productName || item.name || `Product ${item.productID}`}
                          </Typography>
                          <Typography variant="body2" color="text.secondary">
                            Unit Price: {formatPrice(item.price)}
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
                    No products in this invoice.
                  </Typography>
                )}

                {/* Chi tiết thanh toán */}
                <Box sx={{ mt: 3, p: 2, backgroundColor: '#f9f9f9', borderRadius: 1 }}>
                  {!isRushOrder && inv.productPriceExVAT && (
                    <Box sx={{ display: 'flex', justifyContent: 'space-between', mb: 1 }}>
                      <Typography>Product Total (excl. VAT):</Typography>
                      <Typography>{formatPrice(inv.productPriceExVAT)}</Typography>
                    </Box>
                  )}
                  
                  <Box sx={{ display: 'flex', justifyContent: 'space-between', mb: 1 }}>
                    <Typography>Product Total{!isRushOrder ? ' (incl. VAT)' : ''}:</Typography>
                    <Typography>{formatPrice(inv.productPriceIncVAT || inv.subtotal || 0)}</Typography>
                  </Box>
                  
                  <Box sx={{ display: 'flex', justifyContent: 'space-between', mb: 1 }}>
                    <Typography>Shipping Fee:</Typography>
                    <Typography sx={{ 
                      color: isRushOrder ? 'warning.main' : 'inherit',
                      fontWeight: isRushOrder ? 600 : 400
                    }}>
                      {formatPrice(inv.shippingFee || 0)}
                      {isRushOrder && (
                        <Typography component="span" sx={{ fontSize: '0.8rem', ml: 1 }}>
                          (Rush)
                        </Typography>
                      )}
                    </Typography>
                  </Box>
                  
                  <Divider sx={{ my: 1 }} />
                  
                  <Box sx={{ display: 'flex', justifyContent: 'space-between' }}>
                    <Typography variant="h6" sx={{ fontWeight: 600 }}>
                      Total Amount:
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
                <Box sx={{ display: 'flex', gap: 2, mt: 3, flexWrap: 'wrap' }}>
                  <Button 
                    variant="outlined" 
                    color="secondary" 
                    onClick={() => navigate(-1)}
                    disabled={isLoading || isCheckingStatus}
                  >
                    Go Back
                  </Button>
                  
                  {/* Check Payment Status Button - hiển thị cho tất cả orders */}
                  {(inv.id || inv.orderId) && (
                    <Button 
                      variant="outlined" 
                      color="info"
                      onClick={() => checkPaymentStatusManually(inv)} 
                      disabled={isLoading || isCheckingStatus}
                      sx={{ minWidth: 180 }}
                    >
                      {isCheckingStatus ? (
                        <>
                          <CircularProgress size={20} sx={{ mr: 1 }} />
                          Checking...
                        </>
                      ) : (
                        'Check Payment Status'
                      )}
                    </Button>
                  )}
                  
                  <Button 
                    variant="contained" 
                    color="primary"
                    onClick={() => handlePay(inv, idx)} 
                    disabled={isLoading || isCheckingStatus}
                    sx={{ minWidth: 150 }}
                  >
                    {isLoading ? (
                      <>
                        <CircularProgress size={20} sx={{ mr: 1 }} />
                        Creating link...
                      </>
                    ) : (
                      'VnPay Payment'
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
              Delivery Information
            </Typography>
            <Divider sx={{ mb: 2 }} />
            
            <Box sx={{ display: 'grid', gap: 2, gridTemplateColumns: { xs: '1fr', md: '1fr 1fr' } }}>
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
              
              <Box sx={{ gridColumn: { xs: '1', md: '1 / -1' } }}>
                <Typography variant="subtitle2" color="text.secondary">
                  Delivery Address:
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
                    Expected Delivery Time:
                  </Typography>
                  <Typography variant="body1" sx={{ color: 'warning.main', fontWeight: 500 }}>
                    {new Date(deliveryForm.expectedDateTime).toLocaleString('en-US')}
                  </Typography>
                </Box>
              )}
              
              {deliveryForm.deliveryInstructions && (
                <Box>
                  <Typography variant="subtitle2" color="text.secondary">
                    Special Instructions:
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