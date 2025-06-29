import React, { useState } from 'react';
import {
  Button,
  TextField,
  MenuItem,
  Box,
  Typography,
  Alert,
  CircularProgress,
  Paper,
  Divider
} from '@mui/material';
import { submitDeliveryForm, checkRushOrderEligibility, handleNormalOrder } from '../services/api';

// Danh sách tỉnh/thành mới nhất (cập nhật theo sáp nhập 2024)
const provinces = [
  'Hanoi', 'Hochiminh', 'Hải Phòng', 'Đà Nẵng', 'Cần Thơ',
  'Bình Dương', 'Đồng Nai', 'Hải Dương', 'Thanh Hóa', 'Nghệ An',
  'Thừa Thiên Huế', 'Quảng Ninh', 'Bắc Ninh', 'Quảng Nam', 'Lâm Đồng',
  'Nam Định', 'Thái Bình', 'Phú Thọ', 'Bắc Giang', 'Hưng Yên',
  'Hà Nam', 'Vĩnh Phúc', 'Ninh Bình', 'Quảng Bình', 'Quảng Trị',
  'Bình Định', 'Bình Thuận', 'Khánh Hòa', 'Bà Rịa - Vũng Tàu', 'Long An',
  'Kiên Giang', 'Đắk Lắk', 'Cà Mau', 'Bình Phước', 'Bắc Kạn',
  'Lào Cai', 'Lạng Sơn', 'Tuyên Quang', 'Yên Bái', 'Điện Biên',
  'Sơn La', 'Hòa Bình', 'Lai Châu', 'Hà Giang', 'Cao Bằng',
  'Kon Tum', 'Gia Lai', 'Đắk Nông', 'Sóc Trăng', 'Trà Vinh',
  'Bến Tre', 'Vĩnh Long', 'An Giang', 'Tiền Giang', 'Hậu Giang',
  'Ninh Thuận', 'Phú Yên', 'Quảng Ngãi', 'Bạc Liêu'
];

const DeliveryForm = ({ onClose, onSuccess }) => {
  const [form, setForm] = useState({
    customerName: '',
    phoneNumber: '',
    email: '',
    deliveryAddress: '',
    deliveryProvince: '',
    isRushOrder: false
  });
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError('');
    try {
      await submitDeliveryForm(form);
      onSuccess(form);
    } catch (err) {
      setError(err.response?.data?.message || 'Có lỗi xảy ra, vui lòng thử lại.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <Paper elevation={0} sx={{ p: 2, bgcolor: 'transparent', boxShadow: 'none' }}>
      <Typography variant="h6" fontWeight={600} gutterBottom>Nhập thông tin giao hàng</Typography>
      <Divider sx={{ mb: 2 }} />
      <Box component="form" onSubmit={handleSubmit} sx={{ mt: 2 }}>
        <TextField
          label="Họ tên người nhận"
          name="customerName"
          value={form.customerName}
          onChange={handleChange}
          fullWidth
          required
          sx={{ mb: 2 }}
        />
        <TextField
          label="Số điện thoại"
          name="phoneNumber"
          value={form.phoneNumber}
          onChange={handleChange}
          fullWidth
          required
          sx={{ mb: 2 }}
        />
        <TextField
          label="Email"
          name="email"
          value={form.email}
          onChange={handleChange}
          fullWidth
          required
          sx={{ mb: 2 }}
          type="email"
        />
        <TextField
          label="Địa chỉ giao hàng"
          name="deliveryAddress"
          value={form.deliveryAddress}
          onChange={handleChange}
          fullWidth
          required
          sx={{ mb: 2 }}
        />
        <TextField
          select
          label="Tỉnh/Thành phố"
          name="deliveryProvince"
          value={form.deliveryProvince}
          onChange={handleChange}
          fullWidth
          required
          sx={{ mb: 2 }}
        >
          {provinces.map((province) => (
            <MenuItem key={province} value={province}>
              {province}
            </MenuItem>
          ))}
        </TextField>
        {error && <Alert severity="error" sx={{ mb: 2 }}>{error}</Alert>}
        <Box sx={{ display: 'flex', justifyContent: 'flex-end', gap: 2, mt: 2 }}>
          {onClose && (
            <Button onClick={onClose} disabled={loading}>Hủy</Button>
          )}
          <Button type="submit" variant="contained" disabled={loading}>
            {loading ? <CircularProgress size={24} /> : 'Tiếp tục'}
          </Button>
        </Box>
      </Box>
    </Paper>
  );
};

export default DeliveryForm; 