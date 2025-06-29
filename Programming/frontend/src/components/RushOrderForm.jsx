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
      // Send rush order information to backend
      await submitRushOrderInfo(form);
      // Process rush order and receive invoice list
      const res = await processRushOrder();
      onSuccess(res.data.invoiceList || []);
      onClose();
    } catch (err) {
      setError(err.response?.data?.message || 'An error occurred, please try again.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <Dialog open={open} onClose={onClose} maxWidth="sm" fullWidth>
      <DialogTitle>Rush Order Information</DialogTitle>
      <DialogContent>
        <Box component="form" onSubmit={handleSubmit} sx={{ mt: 2 }}>
          <TextField
            label="Expected Delivery Date & Time"
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
            label="Delivery Instructions"
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
        <Button onClick={onClose} disabled={loading}>Cancel</Button>
        <Button onClick={handleSubmit} variant="contained" disabled={loading}>
          {loading ? <CircularProgress size={24} /> : 'Confirm'}
        </Button>
      </DialogActions>
    </Dialog>
  );
};

export default RushOrderForm; 