import React from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import { Box, Typography, Divider, Paper, Button } from '@mui/material';
import Header from '../components/Header';
import { checkRushOrderEligibility } from '../services/api';

const formatPrice = (price) => {
  return new Intl.NumberFormat('vi-VN', {
    style: 'currency',
    currency: 'VND'
  }).format(price);
};

const OrderReviewPage = () => {
  const location = useLocation();
  const navigate = useNavigate();
  const { invoice, deliveryForm } = location.state || {};
  const [rushError, setRushError] = React.useState('');
  const [loadingRush, setLoadingRush] = React.useState(false);

  if (!invoice || !deliveryForm) {
    console.log('DEBUG: invoice', invoice);
    console.log('DEBUG: deliveryForm', deliveryForm);
    return <Typography color="error">Không có dữ liệu đơn hàng để xác nhận.</Typography>;
  }

  // Nếu thiếu trường, hiển thị thông báo rõ ràng
  if (!invoice.productList || typeof invoice.productPriceExVAT === 'undefined' || typeof invoice.productPriceIncVAT === 'undefined' || typeof invoice.shippingFee === 'undefined' || typeof invoice.totalAmount === 'undefined') {
    return <Typography color="error">Dữ liệu hóa đơn trả về từ backend bị thiếu hoặc sai định dạng. Hãy kiểm tra lại API backend và dữ liệu trả về.</Typography>;
  }

  // TODO: Thay bằng logic thực tế kiểm tra eligibility rush order
  const eligibleRushOrder = deliveryForm.deliveryProvince?.toLowerCase().includes('hà nội') || deliveryForm.deliveryProvince?.toLowerCase().includes('hanoi');

  const handleRushOrder = async () => {
    setLoadingRush(true);
    setRushError('');
    try {
      const res = await checkRushOrderEligibility();
      if (res.data.eligible) {
        navigate('/rush-order', { state: { invoice, deliveryForm } });
      } else {
        setRushError('Địa chỉ không hỗ trợ giao nhanh. Rush order chỉ áp dụng cho nội thành Hà Nội.');
      }
    } catch (err) {
      setRushError('Có lỗi khi kiểm tra điều kiện giao nhanh.');
    } finally {
      setLoadingRush(false);
    }
  };

  return (
    <Box sx={{ minHeight: '100vh', backgroundColor: 'background.default' }}>
      <Header />
      <Box sx={{ display: 'flex', flexDirection: { xs: 'column', md: 'row' }, maxWidth: '900px', mx: 'auto', mt: 4, gap: 4 }}>
        <Paper elevation={2} sx={{ flex: 1, p: 3, minWidth: 350 }}>
          <Typography variant="h5" fontWeight={600} gutterBottom>Xác nhận đơn hàng</Typography>
          <Divider sx={{ mb: 2 }} />
          <Typography variant="subtitle1" fontWeight={500}>Sản phẩm:</Typography>
          {invoice.productList?.map((item) => (
            <Box key={item.productID} sx={{ mb: 2 }}>
              <Typography>{item.productName} x {item.quantity}</Typography>
              <Typography variant="body2" color="text.secondary">Đơn giá: {formatPrice(item.price)}</Typography>
            </Box>
          ))}
          <Divider sx={{ my: 2 }} />
          <Typography>Giá sản phẩm (chưa VAT): {formatPrice(invoice.productPriceExVAT)}</Typography>
          <Typography>Giá sản phẩm (đã VAT): {formatPrice(invoice.productPriceIncVAT)}</Typography>
          <Typography>Phí giao hàng: {formatPrice(invoice.shippingFee)}</Typography>
          <Typography variant="h6" color="primary" sx={{ mt: 2 }}>Tổng cộng: {formatPrice(invoice.totalAmount)}</Typography>
        </Paper>
        <Paper elevation={2} sx={{ flex: 1, p: 3, minWidth: 350, display: 'flex', flexDirection: 'column', justifyContent: 'space-between' }}>
          <Box>
            <Typography variant="h6" fontWeight={600} gutterBottom>Thông tin giao hàng</Typography>
            <Divider sx={{ mb: 2 }} />
            <Typography>Họ tên: {deliveryForm.customerName}</Typography>
            <Typography>SĐT: {deliveryForm.phoneNumber}</Typography>
            <Typography>Email: {deliveryForm.email}</Typography>
            <Typography>Địa chỉ: {deliveryForm.deliveryAddress}</Typography>
            <Typography>Tỉnh/Thành: {deliveryForm.deliveryProvince}</Typography>
            <Typography>Ghi chú: {deliveryForm.deliveryInstructions || 'Không có'}</Typography>
          </Box>
          <Box sx={{ mt: 4, display: 'flex', flexDirection: 'column', gap: 2 }}>
            {rushError && <Typography color="error" sx={{ mt: 1 }}>{rushError}</Typography>}
            <Button variant="outlined" color="secondary" onClick={() => navigate('/checkout')}>Quay lại</Button>
            <Button variant="contained" color="primary" onClick={handleRushOrder} disabled={loadingRush}>
              {loadingRush ? 'Đang kiểm tra...' : 'Chọn giao nhanh (Rush Order)'}
            </Button>
            <Button variant="contained" color="primary" onClick={() => navigate('/invoice', { state: { invoice, deliveryForm } })}>
              Thanh toán
            </Button>
          </Box>
        </Paper>
      </Box>
    </Box>
  );
};

export default OrderReviewPage; 