import React, { useState } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import { Box, Typography, Divider, Paper, Button, CircularProgress, Alert } from '@mui/material';
import Header from '../components/Header';
import { checkRushOrderEligibility, submitRushOrderInfo, processRushOrder } from '../services/api';

const formatPrice = (price) => {
  return new Intl.NumberFormat('vi-VN', {
    style: 'currency',
    currency: 'VND'
  }).format(price);
};

const RushOrderPage = () => {
  const location = useLocation();
  const navigate = useNavigate();
  const { invoice, deliveryForm } = location.state || {};
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [rushInfo, setRushInfo] = useState({ expectedDateTime: '', deliveryInstructions: '' });
  const [invoiceList, setInvoiceList] = useState(null);

  const handleRushOrder = async () => {
    setLoading(true);
    setError('');
    try {
      // 1. Check eligibility
      await checkRushOrderEligibility();
      // 2. Submit rush info
      await submitRushOrderInfo(rushInfo);
      // 3. Process rush order
      const res = await processRushOrder();
      setInvoiceList(res.data.invoiceList);
      navigate('/invoice', { state: { invoiceList: res.data.invoiceList, deliveryForm } });
    } catch (err) {
      setError(err.response?.data?.message || 'Có lỗi xảy ra, vui lòng thử lại.');
    } finally {
      setLoading(false);
    }
  };

  if (!invoice || !deliveryForm) {
    return <Typography color="error">Không có dữ liệu đơn hàng để xử lý rush order.</Typography>;
  }

  return (
    <Box sx={{ minHeight: '100vh', backgroundColor: 'background.default' }}>
      <Header />
      <Box sx={{ maxWidth: '900px', mx: 'auto', mt: 4 }}>
        <Paper elevation={2} sx={{ p: 3, mb: 4 }}>
          <Typography variant="h5" fontWeight={600} gutterBottom>Giao nhanh (Rush Order)</Typography>
          <Divider sx={{ mb: 2 }} />
          <Box sx={{ display: 'flex', flexDirection: 'column', gap: 2 }}>
            <Typography>Thời gian mong muốn nhận hàng:</Typography>
            <input
              type="datetime-local"
              value={rushInfo.expectedDateTime}
              onChange={e => setRushInfo({ ...rushInfo, expectedDateTime: e.target.value })}
              style={{ padding: 8, borderRadius: 6, border: '1px solid #ccc', fontSize: 16 }}
            />
            <Typography>Hướng dẫn giao hàng (tuỳ chọn):</Typography>
            <textarea
              value={rushInfo.deliveryInstructions}
              onChange={e => setRushInfo({ ...rushInfo, deliveryInstructions: e.target.value })}
              rows={2}
              style={{ padding: 8, borderRadius: 6, border: '1px solid #ccc', fontSize: 16 }}
            />
            {error && <Alert severity="error">{error}</Alert>}
            <Box sx={{ display: 'flex', gap: 2, mt: 2 }}>
              <Button variant="outlined" color="secondary" onClick={() => navigate(-1)} disabled={loading}>Quay lại</Button>
              <Button variant="contained" color="primary" onClick={handleRushOrder} disabled={loading}>
                {loading ? <CircularProgress size={24} /> : 'Xác nhận giao nhanh'}
              </Button>
            </Box>
          </Box>
        </Paper>
        {invoiceList && (
          <Paper elevation={2} sx={{ p: 3 }}>
            <Typography variant="h6" fontWeight={600} gutterBottom>Danh sách hóa đơn</Typography>
            <Divider sx={{ mb: 2 }} />
            {invoiceList.map((inv, idx) => (
              <Box key={inv.id || idx} sx={{ mb: 3, p: 2, border: '1px solid #e0e0e0', borderRadius: 2 }}>
                <Typography variant="subtitle1" fontWeight={500}>Hóa đơn #{inv.id || idx + 1}</Typography>
                {inv.productList?.map((item) => (
                  <Typography key={item.productID}>
                    {item.productName} x {item.quantity} - {formatPrice(item.price)}
                  </Typography>
                ))}
                <Typography>Phí giao hàng: {formatPrice(inv.shippingFee)}</Typography>
                <Typography>Tổng cộng: {formatPrice(inv.totalAmount)}</Typography>
                <Box sx={{ mt: 2, display: 'flex', gap: 2 }}>
                  <Button variant="contained" color="primary">Thanh toán</Button>
                </Box>
              </Box>
            ))}
          </Paper>
        )}
      </Box>
    </Box>
  );
};

export default RushOrderPage; 