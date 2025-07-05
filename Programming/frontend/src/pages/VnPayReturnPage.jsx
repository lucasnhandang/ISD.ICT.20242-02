import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { 
  Box, 
  Typography, 
  Paper, 
  CircularProgress, 
  Alert,
  Button,
  Divider,
  Grid
} from '@mui/material';
import { 
  CheckCircle as CheckCircleIcon,
  Error as ErrorIcon,
  AccessTime as AccessTimeIcon 
} from '@mui/icons-material';
import axios from 'axios';
import Header from '../components/Header';
import { confirmOrder } from '../services/api';


const VnPayReturnPage = () => {
  const [result, setResult] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [confirmOrderStatus, setConfirmOrderStatus] = useState({ loading: false, success: false, error: null });
  const navigate = useNavigate();

  // Hàm xử lý gọi API confirm-order
  const handleConfirmOrder = async (orderId, isSuccess) => {
    try {
      setConfirmOrderStatus({ loading: true, success: false, error: null });
      console.log("Đang gọi API confirm-order với:", { orderId, isSuccess, type: typeof orderId });
      
      // Đảm bảo orderId là số
      const orderIdNumber = typeof orderId === 'string' ? parseInt(orderId, 10) : orderId;
      if (isNaN(orderIdNumber)) {
        throw new Error('Invalid order ID: ' + orderId);
      }
      
      const response = await confirmOrder(orderIdNumber, isSuccess);
      console.log("API confirm-order thành công:", response.data);
      
      setConfirmOrderStatus({ loading: false, success: true, error: null });
    } catch (error) {
      console.error("Lỗi khi gọi API confirm-order:", error);
      setConfirmOrderStatus({ loading: false, success: false, error: error.message });
    }
  };

  useEffect(() => {
    // Lấy params từ URL
    const urlParams = new URLSearchParams(window.location.search);
    
    console.log("Processing VnPay return with params:", window.location.search);
    console.log("Full URL:", window.location.href);

    // Lấy thông tin từ URL params (backend đã redirect với params)
    const vnpResponseCode = urlParams.get('vnp_ResponseCode') || '';
    const vnpTxnRef = urlParams.get('vnp_TxnRef') || '';
    const vnpTransactionNo = urlParams.get('vnp_TransactionNo') || '';
    const vnpAmount = urlParams.get('vnp_Amount') || '';
    const vnpPayDate = urlParams.get('vnp_PayDate') || '';
    const vnpOrderInfo = urlParams.get('vnp_OrderInfo') || '';
    const orderId = urlParams.get('orderId') || '';

    // Parse paymentSuccess từ URL params
    const paymentSuccessParam = urlParams.get('paymentSuccess') || 'false';
    const isPaymentSuccess = paymentSuccessParam === 'true';
    
    console.log("Parsed parameters:", {
      orderId,
      paymentSuccessParam,
      isPaymentSuccess,
      vnpResponseCode
    });

    // Tạo object result từ params
    const paymentResult = {
      responseCode: vnpResponseCode,
      txnRef: vnpTxnRef,
      transactionNo: vnpTransactionNo,
      amount: vnpAmount,
      payDate: vnpPayDate,
      orderInfo: vnpOrderInfo,
      orderId: orderId,
      paymentSuccess: isPaymentSuccess
    };

    console.log("VnPay return result:", paymentResult);
    console.log("Order ID:", orderId);
    console.log("Payment Success:", isPaymentSuccess);

    setResult(paymentResult);
    setLoading(false);

    // Gọi API confirm-order nếu có orderId hợp lệ
    if (orderId && !isNaN(orderId) && orderId.trim() !== '') {
      const orderIdNumber = parseInt(orderId, 10);
      console.log("Parsed Order ID:", orderIdNumber);
      handleConfirmOrder(orderIdNumber, isPaymentSuccess);
    } else if (orderId) {
      console.error("Invalid orderId:", orderId);
      setError("Invalid order ID format");
    }
  }, []);



  const formatAmount = (amount) => {
    if (!amount) return "0";
    return new Intl.NumberFormat('vi-VN', {
      style: 'currency',
      currency: 'VND'
    }).format(amount / 100); // VnPay trả về amount * 100
  };

  const formatDate = (dateStr) => {
    if (!dateStr) return "N/A";
    // VnPay format: yyyyMMddHHmmss
    const year = dateStr.substring(0, 4);
    const month = dateStr.substring(4, 6);
    const day = dateStr.substring(6, 8);
    const hour = dateStr.substring(8, 10);
    const minute = dateStr.substring(10, 12);
    const second = dateStr.substring(12, 14);
    
    return `${day}/${month}/${year} ${hour}:${minute}:${second}`;
  };

  const getStatusIcon = () => {
    if (loading) return <CircularProgress />;
    if (error) return <ErrorIcon color="error" sx={{ fontSize: 64 }} />;
    if (result?.responseCode === '00') {
      return <CheckCircleIcon color="success" sx={{ fontSize: 64 }} />;
    } else {
      return <ErrorIcon color="error" sx={{ fontSize: 64 }} />;
    }
  };

  const getStatusText = () => {
    if (loading) return "Processing payment...";
    if (error) return "Payment processing error";
    if (result?.responseCode === '00') return "Payment successful";
    return "Payment failed";
  };

  const getStatusColor = () => {
    if (loading) return "info";
    if (error) return "error";
    if (result?.responseCode === '00') return "success";
    return "error";
  };

  if (loading) {
    return (
      <Box>
        <Header />
        <Box 
          display="flex" 
          justifyContent="center" 
          alignItems="center" 
          minHeight="400px"
          flexDirection="column"
        >
          <CircularProgress size={60} />
          <Typography variant="h6" sx={{ mt: 2 }}>
            Processing payment...
          </Typography>
        </Box>
      </Box>
    );
  }

  return (
    <Box>
      <Header />
      <Box sx={{ maxWidth: 800, mx: 'auto', p: 3 }}>
        <Paper elevation={3} sx={{ p: 4 }}>
          <Box textAlign="center" mb={4}>
            {getStatusIcon()}
            <Typography variant="h4" sx={{ mt: 2, mb: 1 }}>
              {getStatusText()}
            </Typography>
            
            <Alert severity={getStatusColor()} sx={{ mt: 2 }}>
              {error ? error : (
                result?.responseCode === '00' 
                  ? "Transaction processed successfully!" 
                  : `Transaction failed. Error code: ${result?.responseCode}`
              )}
            </Alert>

            {/* Hiển thị status confirm order */}
            {result?.orderId && (
              <Box sx={{ mt: 2 }}>
                {confirmOrderStatus.loading && (
                  <Alert severity="info" sx={{ mt: 1 }}>
                    <Box sx={{ display: 'flex', alignItems: 'center', gap: 1 }}>
                      <CircularProgress size={20} />
                      <Typography>Confirming order...</Typography>
                    </Box>
                  </Alert>
                )}
                
                {confirmOrderStatus.success && (
                  <Alert severity="success" sx={{ mt: 1 }}>
                    Order has been confirmed successfully! Cart has been cleared.
                  </Alert>
                )}
                
                {confirmOrderStatus.error && (
                  <Alert severity="error" sx={{ mt: 1 }}>
                    Error confirming order: {confirmOrderStatus.error}
                  </Alert>
                )}
              </Box>
            )}
          </Box>

          {result && !error && (
            <>
              <Divider sx={{ my: 3 }} />
              <Typography variant="h6" gutterBottom>
                Transaction Details
              </Typography>
              
              <Grid container spacing={2}>
                <Grid item xs={12} sm={6}>
                  <Typography variant="body2" color="textSecondary">
                    VnPay Transaction ID
                  </Typography>
                  <Typography variant="body1" fontWeight="bold">
                    {result.transactionNo || "N/A"}
                  </Typography>
                </Grid>
                
                <Grid item xs={12} sm={6}>
                  <Typography variant="body2" color="textSecondary">
                    Order ID
                  </Typography>
                  <Typography variant="body1" fontWeight="bold">
                    {result.orderId || "N/A"}
                  </Typography>
                </Grid>
                
                <Grid item xs={12} sm={6}>
                  <Typography variant="body2" color="textSecondary">
                    Payment Amount
                  </Typography>
                  <Typography variant="body1" fontWeight="bold" color="primary">
                    {formatAmount(result.amount)}
                  </Typography>
                </Grid>
                
                <Grid item xs={12} sm={6}>
                  <Typography variant="body2" color="textSecondary">
                    Payment Time
                  </Typography>
                  <Typography variant="body1" fontWeight="bold">
                    {formatDate(result.payDate)}
                  </Typography>
                </Grid>
                
                <Grid item xs={12}>
                  <Typography variant="body2" color="textSecondary">
                    Order Information
                  </Typography>
                  <Typography variant="body1">
                    {result.orderInfo || "N/A"}
                  </Typography>
                </Grid>
                
                {result.cardType && (
                  <Grid item xs={12} sm={6}>
                    <Typography variant="body2" color="textSecondary">
                      Card Type
                    </Typography>
                    <Typography variant="body1">
                      {result.cardType}
                    </Typography>
                  </Grid>
                )}
                
                <Grid item xs={12} sm={6}>
                  <Typography variant="body2" color="textSecondary">
                    Payment Status
                  </Typography>
                  <Typography 
                    variant="body1" 
                    fontWeight="bold"
                    color={result.paymentSuccess ? "success.main" : "error.main"}
                  >
                    {result.paymentSuccess ? "Success" : "Failed"}
                  </Typography>
                </Grid>
              </Grid>
            </>
          )}

          <Divider sx={{ my: 3 }} />
          
          <Box textAlign="center">
            <Button 
              variant="contained" 
              color="primary" 
              onClick={() => navigate('/')}
              sx={{ mr: 2 }}
            >
              Back to Home
            </Button>
            
            {/* {result?.responseCode === '00' && (
              <Button 
                variant="outlined" 
                color="primary"
                onClick={() => navigate('/order-history')}
              >
                Xem đơn hàng
              </Button>
            )} */}
          </Box>
        </Paper>
      </Box>
    </Box>
  );
};

export default VnPayReturnPage; 