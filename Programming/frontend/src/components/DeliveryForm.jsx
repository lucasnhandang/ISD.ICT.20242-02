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
  Divider
} from '@mui/material';
import { submitDeliveryForm, checkRushOrderEligibility } from '../services/api';
import { provinces, getBackendValue, getDisplayName } from '../utils/provinceUtils';

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

  // Cập nhật form khi có initialValues
  useEffect(() => {
    if (initialValues) {
      setForm({
        customerName: initialValues.customerName || '',
        phoneNumber: initialValues.phoneNumber || '',
        email: initialValues.email || '',
        deliveryAddress: initialValues.deliveryAddress || '',
        deliveryProvince: getDisplayName(initialValues.deliveryProvince) || '',
        isRushOrder: initialValues.isRushOrder || false
      });
    }
  }, [initialValues]);

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError('');
    try {
      // Convert display name to backend value before sending
      const formDataForBackend = {
        ...form,
        deliveryProvince: getBackendValue(form.deliveryProvince)
      };
      await submitDeliveryForm(formDataForBackend);
      onSuccess(formDataForBackend);
    } catch (err) {
      setError(err.response?.data?.message || 'An error occurred, please try again.');
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
        {error && <Alert severity="error" sx={{ mb: 2 }}>{error}</Alert>}
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