import React, { useEffect, useState } from 'react';
import { Box, Typography, Divider, Paper, CircularProgress, Alert } from '@mui/material';
import { useNavigate } from 'react-router-dom';
import Header from '../components/Header';
import DeliveryForm from '../components/DeliveryForm';
import { getCart, requestToPlaceOrder, submitDeliveryForm, handleNormalOrder } from '../services/api';

const formatPrice = (price) => {
  return new Intl.NumberFormat('vi-VN', {
    style: 'currency',
    currency: 'VND'
  }).format(price);
};

const CheckoutPage = () => {
  const [cart, setCart] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const navigate = useNavigate();

  useEffect(() => {
    const fetchCart = async () => {
      try {
        setLoading(true);
        const response = await getCart();
        setCart(response.data);
      } catch (err) {
        setError('Không thể tải giỏ hàng.');
      } finally {
        setLoading(false);
      }
    };
    fetchCart();
  }, []);

  // Khi submit form giao hàng
  const handleDeliveryFormSuccess = async (deliveryForm) => {
    try {
      setLoading(true);
      if (cart) {
        await requestToPlaceOrder(cart);
        await submitDeliveryForm(deliveryForm);
        const invoiceRes = await handleNormalOrder();
        navigate('/order-review', { state: { invoice: invoiceRes.data, deliveryForm } });
      }
    } catch (err) {
      setError(err.response?.data?.message || 'Có lỗi xảy ra, vui lòng thử lại.');
    } finally {
      setLoading(false);
    }
  };

  if (loading) {
    return (
      <Box sx={{ display: 'flex', justifyContent: 'center', alignItems: 'center', minHeight: '100vh' }}>
        <CircularProgress />
      </Box>
    );
  }

  if (error) {
    return <Alert severity="error">{error}</Alert>;
  }

  return (
    <Box sx={{ minHeight: '100vh', backgroundColor: 'background.default' }}>
      <Header />
      <Box sx={{ display: 'flex', flexDirection: { xs: 'column', md: 'row' }, maxWidth: '1200px', mx: 'auto', mt: 4, gap: 4 }}>
        {/* Bên trái: Thông tin giỏ hàng */}
        <Paper elevation={2} sx={{ flex: 1, p: 3, minWidth: 350 }}>
          <Typography variant="h5" fontWeight={600} gutterBottom>Thông tin đơn hàng</Typography>
          <Divider sx={{ mb: 2 }} />
          {cart.productList.map((item) => (
            <Box key={item.productID} sx={{ mb: 2 }}>
              <Typography variant="subtitle1">{item.productName}</Typography>
              <Typography variant="body2">Số lượng: {item.quantity}</Typography>
              <Typography variant="body2">Đơn giá: {formatPrice(item.price)}</Typography>
              <Typography variant="body2">Thành tiền: {formatPrice(item.price * item.quantity)}</Typography>
              <Divider sx={{ my: 1 }} />
            </Box>
          ))}
          <Box sx={{ mt: 2 }}>
            <Typography>Giá sản phẩm (chưa VAT): {formatPrice(cart.totalPrice - (cart.discount || 0))}</Typography>
            <Typography>Giảm giá: {formatPrice(cart.discount || 0)}</Typography>
            <Typography>Giá sản phẩm (đã VAT): {formatPrice(cart.totalPrice)}</Typography>
            <Typography color="text.secondary" variant="body2" sx={{ mt: 1 }}>
              * Phí giao hàng, phí giao nhanh (nếu có) sẽ được tính sau khi nhập địa chỉ giao hàng.
            </Typography>
          </Box>
          <Divider sx={{ my: 2 }} />
          <Typography variant="h6" color="primary">Tổng cộng: {formatPrice(cart.totalPrice - (cart.discount || 0))}</Typography>
          <Divider sx={{ my: 2 }} />
          <Box sx={{ mt: 2 }}>
            <Typography variant="subtitle2" color="text.secondary" gutterBottom>
              Quy định về phí giao hàng và giao nhanh:
            </Typography>
            <ul style={{ marginLeft: 16, color: '#666', fontSize: 14 }}>
              <li>Phí giao hàng không chịu thuế.</li>
              <li>Đơn hàng trên 100,000 VND được miễn phí ship tối đa 25,000 VND (không áp dụng cho giao nhanh).</li>
              <li>Phí giao hàng tính theo cân nặng và địa chỉ nhận hàng.</li>
              <li>Giao nhanh chỉ áp dụng cho nội thành Hà Nội, phí cộng thêm 10,000 VND/món.</li>
              <li>Chi tiết phí sẽ hiển thị sau khi nhập địa chỉ giao hàng.</li>
            </ul>
          </Box>
        </Paper>
        {/* Bên phải: Form nhập thông tin giao hàng */}
        <Paper elevation={2} sx={{ flex: 1, p: 3, minWidth: 350 }}>
          <Typography variant="h5" fontWeight={600} gutterBottom>Thông tin giao hàng</Typography>
          <Divider sx={{ mb: 2 }} />
          <DeliveryForm onClose={() => navigate('/cart')} onSuccess={handleDeliveryFormSuccess} />
        </Paper>
      </Box>
    </Box>
  );
};

export default CheckoutPage; 