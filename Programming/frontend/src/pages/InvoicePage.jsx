import React, { useState } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import { Box, Typography, Divider, Paper, Button, CircularProgress, Alert } from '@mui/material';
import Header from '../components/Header';
import { handlePayment } from '../services/api';

const formatPrice = (price) => {
  return new Intl.NumberFormat('vi-VN', {
    style: 'currency',
    currency: 'VND'
  }).format(price);
};

const InvoicePage = () => {
  const location = useLocation();
  const navigate = useNavigate();
  const { invoice, invoiceList, deliveryForm } = location.state || {};
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [paid, setPaid] = useState(false);

  const invoices = invoiceList || (invoice ? [invoice] : []);

  const handlePay = async (inv) => {
    setLoading(true);
    setError('');
    try {
      await handlePayment(); // Gọi API backend để xử lý thanh toán
      setPaid(true);
    } catch (err) {
      setError(err.response?.data?.message || 'Có lỗi khi thanh toán.');
    } finally {
      setLoading(false);
    }
  };

  if (!invoices.length) {
    return <Typography color="error">Không có hóa đơn để thanh toán.</Typography>;
  }

  return (
    <Box sx={{ minHeight: '100vh', backgroundColor: 'background.default' }}>
      <Header />
      <Box sx={{ maxWidth: '900px', mx: 'auto', mt: 4 }}>
        <Typography variant="h5" fontWeight={600} gutterBottom>Thanh toán hóa đơn</Typography>
        <Divider sx={{ mb: 2 }} />
        {invoices.map((inv, idx) => (
          <Paper key={inv.id || idx} elevation={2} sx={{ p: 3, mb: 4 }}>
            <Typography variant="subtitle1" fontWeight={500}>Hóa đơn #{inv.id || idx + 1}</Typography>
            {inv.productList?.map((item) => (
              <Typography key={item.productID}>
                {item.productName} x {item.quantity} - {formatPrice(item.price)}
              </Typography>
            ))}
            <Typography>Phí giao hàng: {formatPrice(inv.shippingFee)}</Typography>
            <Typography>Tổng cộng: {formatPrice(inv.totalAmount)}</Typography>
            <Divider sx={{ my: 2 }} />
            {error && <Alert severity="error">{error}</Alert>}
            {paid ? (
              <Alert severity="success">Thanh toán thành công!</Alert>
            ) : (
              <Box sx={{ display: 'flex', gap: 2 }}>
                <Button variant="outlined" color="secondary" onClick={() => navigate(-1)} disabled={loading}>Quay lại</Button>
                <Button variant="contained" color="primary" onClick={() => handlePay(inv)} disabled={loading}>
                  {loading ? <CircularProgress size={24} /> : 'Thanh toán'}
                </Button>
              </Box>
            )}
          </Paper>
        ))}
      </Box>
    </Box>
  );
};

export default InvoicePage; 