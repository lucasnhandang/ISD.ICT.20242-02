import React, { useEffect, useState } from 'react';
import { useNavigate, useSearchParams } from 'react-router-dom';
import { 
  Box, 
  Typography, 
  CircularProgress, 
  Alert,
  Paper
} from '@mui/material';
import CheckCircleOutlineIcon from '@mui/icons-material/CheckCircleOutline';
import ErrorOutlineIcon from '@mui/icons-material/ErrorOutline';
import Header from '../components/Header';
import { getPaymentResult } from '../services/api';

const VnPayReturnPage = () => {
  const navigate = useNavigate();
  const [searchParams] = useSearchParams();
  const [processing, setProcessing] = useState(true);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState(false);
  const [paymentResult, setPaymentResult] = useState(null);
  const [orderInfo, setOrderInfo] = useState(null);

  useEffect(() => {
    const processPaymentReturn = async () => {
      try {
        // Lấy parameters từ URL
        const txnRef = searchParams.get('vnp_TxnRef');
        const responseCode = searchParams.get('vnp_ResponseCode');
        const transactionNo = searchParams.get('vnp_TransactionNo');
        const amount = searchParams.get('vnp_Amount');
        
        console.log('VnPay Return Parameters:', {
          txnRef,
          responseCode,
          transactionNo,
          amount
        });

        if (!txnRef || !responseCode) {
          throw new Error('Thiếu thông tin giao dịch từ VnPay');
        }

        // Lấy kết quả thanh toán từ backend
        const resultResponse = await getPaymentResult(txnRef, responseCode);
        setPaymentResult(resultResponse.data);

        // Lấy thông tin đã lưu từ sessionStorage
        const pendingPaymentInfo = sessionStorage.getItem('pendingPaymentInfo');
        if (!pendingPaymentInfo) {
          throw new Error('Không tìm thấy thông tin đơn hàng');
        }

        const paymentInfo = JSON.parse(pendingPaymentInfo);

        if (responseCode === '00') {
          // Thanh toán thành công
          console.log('Payment successful - VnPay verification completed');
          
          // Xóa thông tin tạm thời
          sessionStorage.removeItem('pendingPaymentInfo');
          
          // Set success state để hiển thị thông báo thành công
          setSuccess(true);
          setOrderInfo({
            orderInformation: paymentInfo.orderInformation,
            invoice: paymentInfo.invoice,
            deliveryForm: paymentInfo.deliveryForm,
            paymentMessage: 'Thanh toán VnPay thành công!',
            isRushOrder: paymentInfo.isRushOrder,
            paymentResult: resultResponse.data
          });
        } else {
          // Thanh toán thất bại
          setError(`Thanh toán thất bại: ${resultResponse.data.message || 'Lỗi không xác định'}`);
        }

      } catch (err) {
        console.error('Error processing VnPay return:', err);
        setError(err.message || 'Có lỗi xảy ra khi xử lý kết quả thanh toán');
      } finally {
        setProcessing(false);
      }
    };

    processPaymentReturn();
  }, [searchParams, navigate]);

  if (processing) {
    return (
      <Box sx={{ minHeight: '100vh', backgroundColor: '#f5f5f5' }}>
        <Header />
        
        <Box sx={{ 
          maxWidth: 600, 
          mx: 'auto', 
          pt: 8, 
          px: 2,
          display: 'flex',
          flexDirection: 'column',
          alignItems: 'center'
        }}>
          <Paper elevation={3} sx={{ p: 4, textAlign: 'center', width: '100%' }}>
            <CircularProgress size={60} sx={{ mb: 3 }} />
            <Typography variant="h5" gutterBottom sx={{ fontWeight: 600 }}>
              Đang xử lý kết quả thanh toán...
            </Typography>
            <Typography variant="body1" color="text.secondary">
              Vui lòng đợi trong giây lát, chúng tôi đang xác nhận giao dịch với VnPay.
            </Typography>
          </Paper>
        </Box>
      </Box>
    );
  }

  if (success) {
    return (
      <Box sx={{ minHeight: '100vh', backgroundColor: '#f5f5f5' }}>
        <Header />
        
        <Box sx={{ maxWidth: 600, mx: 'auto', pt: 8, px: 2 }}>
          <Paper elevation={3} sx={{ p: 4, textAlign: 'center' }}>
            <CheckCircleOutlineIcon 
              sx={{ 
                fontSize: 80, 
                color: 'success.main', 
                mb: 2 
              }} 
            />
            <Typography variant="h5" gutterBottom sx={{ fontWeight: 600, color: 'success.main' }}>
              Thanh toán thành công!
            </Typography>
            
            <Alert severity="success" sx={{ mt: 2, mb: 3, textAlign: 'left' }}>
              {orderInfo?.paymentMessage || 'Thanh toán đã được xử lý thành công.'}
            </Alert>

            {paymentResult && (
              <Box sx={{ 
                mt: 2, 
                p: 2, 
                backgroundColor: '#f0f9ff', 
                borderRadius: 1,
                textAlign: 'left'
              }}>
                <Typography variant="subtitle2" gutterBottom>
                  Chi tiết giao dịch:
                </Typography>
                <Typography variant="body2">
                  Mã giao dịch: {paymentResult.txnRef}
                </Typography>
                <Typography variant="body2">
                  Mã phản hồi: {paymentResult.responseCode}
                </Typography>
                {paymentResult.transactionNo && (
                  <Typography variant="body2">
                    Mã GD VnPay: {paymentResult.transactionNo}
                  </Typography>
                )}
                <Typography variant="body2">
                  Số tiền: {paymentResult.amount ? parseInt(paymentResult.amount)/100 : 'N/A'} VND
                </Typography>
              </Box>
            )}

            <Box sx={{ mt: 3, display: 'flex', gap: 2, justifyContent: 'center' }}>
              <button
                onClick={() => navigate('/')}
                style={{
                  padding: '10px 20px',
                  backgroundColor: '#2563eb',
                  color: 'white',
                  border: 'none',
                  borderRadius: '8px',
                  cursor: 'pointer',
                  fontWeight: 600
                }}
              >
                Về trang chủ
              </button>
              <button
                onClick={() => navigate('/cart')}
                style={{
                  padding: '10px 20px',
                  backgroundColor: '#16a34a',
                  color: 'white',
                  border: 'none',
                  borderRadius: '8px',
                  cursor: 'pointer',
                  fontWeight: 600
                }}
              >
                Tiếp tục mua sắm
              </button>
            </Box>
          </Paper>
        </Box>
      </Box>
    );
  }

  if (error) {
    return (
      <Box sx={{ minHeight: '100vh', backgroundColor: '#f5f5f5' }}>
        <Header />
        
        <Box sx={{ maxWidth: 600, mx: 'auto', pt: 8, px: 2 }}>
          <Paper elevation={3} sx={{ p: 4, textAlign: 'center' }}>
            <ErrorOutlineIcon 
              sx={{ 
                fontSize: 80, 
                color: 'error.main', 
                mb: 2 
              }} 
            />
            <Typography variant="h5" gutterBottom sx={{ fontWeight: 600, color: 'error.main' }}>
              Thanh toán thất bại
            </Typography>
            
            <Alert severity="error" sx={{ mt: 2, mb: 3, textAlign: 'left' }}>
              {error}
            </Alert>

            {paymentResult && (
              <Box sx={{ 
                mt: 2, 
                p: 2, 
                backgroundColor: '#f5f5f5', 
                borderRadius: 1,
                textAlign: 'left'
              }}>
                <Typography variant="subtitle2" gutterBottom>
                  Chi tiết giao dịch:
                </Typography>
                <Typography variant="body2">
                  Mã giao dịch: {paymentResult.txnRef}
                </Typography>
                <Typography variant="body2">
                  Mã phản hồi: {paymentResult.responseCode}
                </Typography>
                {paymentResult.transactionNo && (
                  <Typography variant="body2">
                    Mã GD VnPay: {paymentResult.transactionNo}
                  </Typography>
                )}
              </Box>
            )}

            <Box sx={{ mt: 3, display: 'flex', gap: 2, justifyContent: 'center' }}>
              <button
                onClick={() => navigate('/invoice')}
                style={{
                  padding: '10px 20px',
                  backgroundColor: '#2563eb',
                  color: 'white',
                  border: 'none',
                  borderRadius: '8px',
                  cursor: 'pointer',
                  fontWeight: 600
                }}
              >
                Thử lại
              </button>
              <button
                onClick={() => navigate('/')}
                style={{
                  padding: '10px 20px',
                  backgroundColor: '#6b7280',
                  color: 'white',
                  border: 'none',
                  borderRadius: '8px',
                  cursor: 'pointer',
                  fontWeight: 600
                }}
              >
                Về trang chủ
              </button>
            </Box>
          </Paper>
        </Box>
      </Box>
    );
  }

  // Fallback (không nên đến đây)
  return (
    <Box sx={{ minHeight: '100vh', backgroundColor: '#f5f5f5' }}>
      <Header />
      <Box sx={{ maxWidth: 600, mx: 'auto', pt: 8, px: 2 }}>
        <Paper elevation={3} sx={{ p: 4, textAlign: 'center' }}>
          <Typography variant="h5" gutterBottom>
            Đang xử lý...
          </Typography>
        </Paper>
      </Box>
    </Box>
  );
};

export default VnPayReturnPage; 