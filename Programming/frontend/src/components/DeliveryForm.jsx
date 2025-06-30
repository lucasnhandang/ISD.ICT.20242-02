import React, { useState, useEffect } from 'react';
import {
  Button,
  TextField,
  MenuItem,
  Box,
  Typography,
  Alert,
  CircularProgress,
  Paper,
  Divider,
  List,
  ListItem,
  ListItemText
} from '@mui/material';
import { submitDeliveryForm, checkRushOrderEligibility } from '../services/api';

// Latest province/city list (updated according to 2024 merger)
const provinces = [
  'Hanoi', 'Hochiminh', 'Hai Phong', 'Da Nang', 'Can Tho',
  'Binh Duong', 'Dong Nai', 'Hai Duong', 'Thanh Hoa', 'Nghe An',
  'Thua Thien Hue', 'Quang Ninh', 'Bac Ninh', 'Quang Nam', 'Lam Dong',
  'Nam Dinh', 'Thai Binh', 'Phu Tho', 'Bac Giang', 'Hung Yen',
  'Ha Nam', 'Vinh Phuc', 'Ninh Binh', 'Quang Binh', 'Quang Tri',
  'Binh Dinh', 'Binh Thuan', 'Khanh Hoa', 'Ba Ria - Vung Tau', 'Long An',
  'Kien Giang', 'Dak Lak', 'Ca Mau', 'Binh Phuoc', 'Bac Kan',
  'Lao Cai', 'Lang Son', 'Tuyen Quang', 'Yen Bai', 'Dien Bien',
  'Son La', 'Hoa Binh', 'Lai Chau', 'Ha Giang', 'Cao Bang',
  'Kon Tum', 'Gia Lai', 'Dak Nong', 'Soc Trang', 'Tra Vinh',
  'Ben Tre', 'Vinh Long', 'An Giang', 'Tien Giang', 'Hau Giang',
  'Ninh Thuan', 'Phu Yen', 'Quang Ngai', 'Bac Lieu'
];

const DeliveryForm = ({ onClose, onSuccess, initialValues, disabled }) => {
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
  const [validationErrors, setValidationErrors] = useState([]);

  // Cập nhật form khi có initialValues
  useEffect(() => {
    if (initialValues) {
      setForm({
        customerName: initialValues.customerName || '',
        phoneNumber: initialValues.phoneNumber || '',
        email: initialValues.email || '',
        deliveryAddress: initialValues.deliveryAddress || '',
        deliveryProvince: initialValues.deliveryProvince || '',
        isRushOrder: initialValues.isRushOrder || false
      });
    }
  }, [initialValues]);

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
    // Clear errors when user starts editing
    if (validationErrors.length > 0) {
      setValidationErrors([]);
    }
    if (error) {
      setError('');
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError('');
    setValidationErrors([]);
    
    try {
      await submitDeliveryForm(form);
      onSuccess(form);
    } catch (err) {
      console.log('Error response:', err.response?.data);
      
      // Check if it's validation errors from backend
      if (err.response?.data?.errors && Array.isArray(err.response.data.errors)) {
        setValidationErrors(err.response.data.errors);
        setError(err.response.data.message || 'Please check the information below');
      } else {
        // Other errors (network, server error, etc.)
        setError(err.response?.data?.message || 'An error occurred, please try again.');
      }
    } finally {
      setLoading(false);
    }
  };

  return (
    <Paper elevation={0} sx={{ p: 2, bgcolor: 'transparent', boxShadow: 'none' }}>
      <Typography variant="h6" fontWeight={600} gutterBottom>
        Enter Delivery Information
      </Typography>
      <Divider sx={{ mb: 2 }} />
      <Box component="form" onSubmit={handleSubmit} sx={{ mt: 2 }}>
        <TextField
          label="Recipient Name"
          name="customerName"
          value={form.customerName}
          onChange={handleChange}
          fullWidth
          required
          disabled={disabled}
          sx={{ mb: 2 }}
        />
        <TextField
          label="Phone Number"
          name="phoneNumber"
          value={form.phoneNumber}
          onChange={handleChange}
          fullWidth
          required
          disabled={disabled}
          sx={{ mb: 2 }}
        />
        <TextField
          label="Email"
          name="email"
          value={form.email}
          onChange={handleChange}
          fullWidth
          required
          disabled={disabled}
          sx={{ mb: 2 }}
          type="email"
        />
        <TextField
          label="Delivery Address"
          name="deliveryAddress"
          value={form.deliveryAddress}
          onChange={handleChange}
          fullWidth
          required
          disabled={disabled}
          sx={{ mb: 2 }}
        />
        <TextField
          select
          label="Province/City"
          name="deliveryProvince"
          value={form.deliveryProvince}
          onChange={handleChange}
          fullWidth
          required
          disabled={disabled}
          sx={{ mb: 2 }}
        >
          {provinces.map((province) => (
            <MenuItem key={province} value={province}>
              {province}
            </MenuItem>
          ))}
        </TextField>

        {/* Display specific validation errors */}
        {validationErrors.length > 0 && (
          <Alert severity="error" sx={{ mb: 2 }}>
            <Typography variant="subtitle2" sx={{ mb: 1 }}>
              Please fix the following errors:
            </Typography>
            <List dense sx={{ py: 0 }}>
              {validationErrors.map((errorMsg, index) => (
                <ListItem key={index} sx={{ py: 0, pl: 0 }}>
                  <ListItemText 
                    primary={`• ${errorMsg}`}
                    primaryTypographyProps={{ variant: 'body2' }}
                  />
                </ListItem>
              ))}
            </List>
          </Alert>
        )}

        {/* Display general errors */}
        {error && validationErrors.length === 0 && (
          <Alert severity="error" sx={{ mb: 2 }}>
            {error}
          </Alert>
        )}

        <Box sx={{ display: 'flex', justifyContent: 'flex-end', gap: 2, mt: 2 }}>
          {onClose && (
            <Button onClick={onClose} disabled={loading || disabled}>
              Cancel
            </Button>
          )}
          <Button type="submit" variant="contained" disabled={loading || disabled}>
            {loading ? <CircularProgress size={24} /> : 'Continue'}
          </Button>
        </Box>
      </Box>
    </Paper>
  );
};

export default DeliveryForm; 