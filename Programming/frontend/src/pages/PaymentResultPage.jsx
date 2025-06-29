import React, { useEffect, useState } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import { 
  Box, 
  Typography, 
  Paper, 
  Button, 
  CircularProgress, 
  Alert,
  Divider,
  Chip
} from '@mui/material';
import { 
  CheckCircle as CheckCircleIcon, 
  Error as ErrorIcon,
  Home as HomeIcon,
  Receipt as ReceiptIcon
} from '@mui/icons-material';
import Header from '../components/Header';
import { getPaymentResult } from '../services/api';

const formatPrice = (price) => {
  return new Intl.NumberFormat('vi-VN', {
    style: 'currency',
    currency: 'VND'
  }).format(price);
};

const PaymentResultPage = () => {
  const location = useLocation();
  const navigate = useNavigate();
  const [paymentResult, setPaymentResult] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    const fetchPaymentResult = async () => {
      try {
        setLoading(true);
        
        // Lấy parameters từ URL
        const urlParams = new URLSearchParams(location.search);
        const txnRef = urlParams.get('vnp_TxnRef');
        const responseCode = urlParams.get('vnp_ResponseCode');
        
        if (!txnRef || !responseCode) {
          setError('Thiếu thông tin giao dịch');
          return;
        }

        // Gọi API để lấy kết quả thanh toán
        const response = await getPaymentResult(txnRef, responseCode);
        setPaymentResult(response.data);
        
      } catch (err) {
        console.error('Error fetching payment result:', err);
        setError(err.response?.data?.message || 'Có lỗi khi lấy thông tin thanh toán');
      } finally {
        setLoading(false);
      }
    };

    fetchPaymentResult();
  }, [location.search]);

  // Loading state
  if (loading) {
    return (
      <Box sx={{ minHeight: '100vh', backgroundColor: '#f5f5f5' }}>
        <Header />
        <Box sx={{ 
          display: 'flex', 
          justifyContent: 'center', 
          alignItems: 'center', 
          minHeight: '60vh',
          flexDirection: 'column',
          gap: 2
        }}>
          <CircularProgress size={60} />
          <Typography variant="h6">Đang xử lý kết quả thanh toán...</Typography>
          <Typography variant="body2" color="text.secondary">
            Vui lòng không đóng trang này
          </Typography>
        </Box>
      </Box>
    );
  }

  // Error state
  if (error) {
    return (
      <Box sx={{ minHeight: '100vh', backgroundColor: '#f5f5f5' }}>
        <Header />
        <Box sx={{ maxWidth: 600, mx: 'auto', pt: 4, px: 2 }}>
          <Paper elevation={2} sx={{ p: 4, textAlign: 'center' }}>
            <ErrorIcon sx={{ fontSize: 80, color: 'error.main', mb: 2 }} />
            <Typography variant="h5" gutterBottom color="error">
              Có lỗi xảy ra
            </Typography>
            <Typography variant="body1" sx={{ mb: 3 }}>
              {error}
            </Typography>
            <Button 
              variant="contained" 
              startIcon={<HomeIcon />}
              onClick={() => navigate('/')}
              sx={{ mr: 2 }}
            >
              Về trang chủ
            </Button>
            <Button 
              variant="outlined"
              onClick={() => navigate(-1)}
            >
              Quay lại
            </Button>
          </Paper>
        </Box>
      </Box>
    );
  }

  // Success/Failure result
  const isSuccess = paymentResult?.success;
  const resultIcon = isSuccess ? 
    <CheckCircleIcon sx={{ fontSize: 80, color: 'success.main', mb: 2 }} /> :
    <ErrorIcon sx={{ fontSize: 80, color: 'error.main', mb: 2 }} />;

  return (
    <Box sx={{ minHeight: '100vh', backgroundColor: '#f5f5f5' }}>
      <Header />
      <Box sx={{ maxWidth: 800, mx: 'auto', pt: 4, px: 2 }}>
        <Paper elevation={2} sx={{ p: 4 }}>
          {/* Header with icon */}
          <Box sx={{ textAlign: 'center', mb: 4 }}>
            {resultIcon}
            <Typography variant="h4" gutterBottom color={isSuccess ? 'success.main' : 'error.main'}>
              {isSuccess ? 'Thanh toán thành công!' : 'Thanh toán không thành công'}
            </Typography>
            <Typography variant="body1" color="text.secondary">
              {paymentResult?.message || 'Không có thông tin chi tiết'}
            </Typography>
          </Box>

          <Divider sx={{ mb: 3 }} />

          {/* Payment details */}
          <Typography variant="h6" gutterBottom sx={{ fontWeight: 600 }}>
            Chi tiết giao dịch
          </Typography>
          
          <Box sx={{ display: 'grid', gap: 2, gridTemplateColumns: { xs: '1fr', md: '1fr 1fr' } }}>
            {paymentResult?.txnRef && (
              <Box>
                <Typography variant="subtitle2" color="text.secondary">
                  Mã giao dịch:
                </Typography>
                <Typography variant="body1" sx={{ fontWeight: 500 }}>
                  {paymentResult.txnRef}
                </Typography>
              </Box>
            )}
            
            {paymentResult?.transactionNo && (
              <Box>
                <Typography variant="subtitle2" color="text.secondary">
                  Mã GD ngân hàng:
                </Typography>
                <Typography variant="body1" sx={{ fontWeight: 500 }}>
                  {paymentResult.transactionNo}
                </Typography>
              </Box>
            )}
            
            {paymentResult?.amount && (
              <Box>
                <Typography variant="subtitle2" color="text.secondary">
                  Số tiền:
                </Typography>
                <Typography variant="body1" sx={{ fontWeight: 600, color: 'primary.main' }}>
                  {formatPrice(parseInt(paymentResult.amount) / 100)}
                </Typography>
              </Box>
            )}
            
            {paymentResult?.bankCode && (
              <Box>
                <Typography variant="subtitle2" color="text.secondary">
                  Ngân hàng:
                </Typography>
                <Typography variant="body1">
                  {paymentResult.bankCode}
                </Typography>
              </Box>
            )}
            
            {paymentResult?.cardType && (
              <Box>
                <Typography variant="subtitle2" color="text.secondary">
                  Loại thẻ:
                </Typography>
                <Chip 
                  label={paymentResult.cardType} 
                  size="small" 
                  variant="outlined"
                />
              </Box>
            )}
            
            {paymentResult?.payDate && (
              <Box>
                <Typography variant="subtitle2" color="text.secondary">
                  Thời gian thanh toán:
                </Typography>
                <Typography variant="body1">
                  {new Date(paymentResult.payDate).toLocaleString('vi-VN')}
                </Typography>
              </Box>
            )}
          </Box>

          {paymentResult?.orderInfo && (
            <Box sx={{ mt: 3 }}>
              <Typography variant="subtitle2" color="text.secondary">
                Thông tin đơn hàng:
              </Typography>
              <Typography variant="body1">
                {paymentResult.orderInfo}
              </Typography>
            </Box>
          )}

          {/* Status message */}
          {isSuccess ? (
            <Alert severity="success" sx={{ mt: 3 }}>
              <Typography variant="body2">
                <strong>Thanh toán đã hoàn tất!</strong> Đơn hàng của bạn đã được xử lý thành công. 
                Bạn sẽ nhận được email xác nhận trong vài phút tới.
              </Typography>
            </Alert>
          ) : (
            <Alert severity="error" sx={{ mt: 3 }}>
              <Typography variant="body2">
                <strong>Thanh toán không thành công.</strong> Vui lòng kiểm tra lại thông tin 
                hoặc thử lại sau. Nếu vấn đề vẫn tiếp tục, hãy liên hệ với chúng tôi.
              </Typography>
            </Alert>
          )}

          {/* Action buttons */}
          <Box sx={{ display: 'flex', gap: 2, justifyContent: 'center', mt: 4 }}>
            <Button 
              variant="contained" 
              startIcon={<HomeIcon />}
              onClick={() => navigate('/')}
              size="large"
            >
              Về trang chủ
            </Button>
            {isSuccess && (
              <Button 
                variant="outlined"
                startIcon={<ReceiptIcon />}
                onClick={() => navigate('/order-history')}
                size="large"
              >
                Xem đơn hàng
              </Button>
            )}
            {!isSuccess && (
              <Button 
                variant="outlined"
                onClick={() => navigate('/cart')}
                size="large"
              >
                Thử lại
              </Button>
            )}
          </Box>
        </Paper>
      </Box>
    </Box>
  );
};

export default PaymentResultPage; 