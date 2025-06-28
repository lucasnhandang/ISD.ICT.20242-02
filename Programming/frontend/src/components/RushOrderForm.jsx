import React, { useState } from 'react';
import {
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  Button,
  TextField,
  Box,
  Alert,
  CircularProgress
} from '@mui/material';
import { submitRushOrderInfo, processRushOrder } from '../services/api';

const RushOrderForm = ({ open, onClose, onSuccess }) => {
  const [form, setForm] = useState({
    expectedDateTime: '',
    deliveryInstructions: ''
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
      // Gửi thông tin rush order lên backend
      await submitRushOrderInfo(form);
      // Xử lý rush order và nhận về invoice list
      const res = await processRushOrder();
      onSuccess(res.data.invoiceList || []);
      onClose();
    } catch (err) {
      setError(err.response?.data?.message || 'Có lỗi xảy ra, vui lòng thử lại.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <Dialog open={open} onClose={onClose} maxWidth="sm" fullWidth>
      <DialogTitle>Thông tin giao nhanh (Rush Order)</DialogTitle>
      <DialogContent>
        <Box component="form" onSubmit={handleSubmit} sx={{ mt: 2 }}>
          <TextField
            label="Thời gian mong muốn nhận hàng"
            name="expectedDateTime"
            type="datetime-local"
            value={form.expectedDateTime}
            onChange={handleChange}
            fullWidth
            required
            sx={{ mb: 2 }}
            InputLabelProps={{ shrink: true }}
          />
          <TextField
            label="Hướng dẫn giao hàng (tuỳ chọn)"
            name="deliveryInstructions"
            value={form.deliveryInstructions}
            onChange={handleChange}
            fullWidth
            multiline
            minRows={2}
            sx={{ mb: 2 }}
          />
          {error && <Alert severity="error" sx={{ mb: 2 }}>{error}</Alert>}
        </Box>
      </DialogContent>
      <DialogActions>
        <Button onClick={onClose} disabled={loading}>Hủy</Button>
        <Button onClick={handleSubmit} variant="contained" disabled={loading}>
          {loading ? <CircularProgress size={24} /> : 'Xác nhận'}
        </Button>
      </DialogActions>
    </Dialog>
  );
};

export default RushOrderForm; 