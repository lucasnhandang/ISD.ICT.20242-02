import React, { useState } from 'react';
import { Button, Box, Typography, Card, CardContent, Alert } from '@mui/material';
import { orderManagementAPI } from '../services/api';

const ApiTestComponent = () => {
  const [result, setResult] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const testConnection = async () => {
    setLoading(true);
    setError(null);
    setResult(null);

    try {
      console.log('🧪 Bắt đầu test API connection...');
      
      // Test basic connection
      const response = await fetch('http://localhost:8080/api/v1/product-manager/orders/pending', {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
          'Accept': 'application/json',
        },
      });

      if (!response.ok) {
        throw new Error(`HTTP ${response.status}: ${response.statusText}`);
      }

      const data = await response.json();
      setResult({
        success: true,
        method: 'Fetch API',
        data: data,
        count: data.length
      });
      
      console.log('✅ Test fetch API thành công:', data);
      
    } catch (err) {
      console.error('❌ Test fetch API thất bại:', err);
      setError({
        method: 'Fetch API',
        message: err.message,
        stack: err.stack
      });
    } finally {
      setLoading(false);
    }
  };

  const testAxios = async () => {
    setLoading(true);
    setError(null);
    setResult(null);

    try {
      console.log('🧪 Bắt đầu test Axios API...');
      const data = await orderManagementAPI.getPendingOrders();
      
      setResult({
        success: true,
        method: 'Axios API',
        data: data,
        count: data.length
      });
      
      console.log('✅ Test Axios API thành công:', data);
      
    } catch (err) {
      console.error('❌ Test Axios API thất bại:', err);
      setError({
        method: 'Axios API',
        message: err.message,
        stack: err.stack
      });
    } finally {
      setLoading(false);
    }
  };

  return (
    <Box sx={{ p: 3, maxWidth: 800, margin: '0 auto' }}>
      <Typography variant="h4" gutterBottom>
        🔧 API Connection Test
      </Typography>
      
      <Box sx={{ mb: 2, display: 'flex', gap: 2 }}>
        <Button 
          variant="contained" 
          onClick={testConnection}
          disabled={loading}
          color="primary"
        >
          Test Fetch API
        </Button>
        <Button 
          variant="contained" 
          onClick={testAxios}
          disabled={loading}
          color="secondary"
        >
          Test Axios API
        </Button>
      </Box>

      {loading && (
        <Alert severity="info" sx={{ mb: 2 }}>
          🔄 Đang test API connection...
        </Alert>
      )}

      {error && (
        <Card sx={{ mb: 2 }}>
          <CardContent>
            <Alert severity="error" sx={{ mb: 2 }}>
              ❌ Lỗi {error.method}
            </Alert>
            <Typography variant="body2" color="error">
              <strong>Thông báo lỗi:</strong> {error.message}
            </Typography>
            {error.stack && (
              <Typography variant="body2" color="text.secondary" sx={{ mt: 1, fontSize: '0.8rem' }}>
                <strong>Stack trace:</strong>
                <pre>{error.stack}</pre>
              </Typography>
            )}
          </CardContent>
        </Card>
      )}

      {result && (
        <Card>
          <CardContent>
            <Alert severity="success" sx={{ mb: 2 }}>
              ✅ Thành công {result.method}
            </Alert>
            <Typography variant="body1" gutterBottom>
              <strong>Số lượng orders:</strong> {result.count}
            </Typography>
            <Typography variant="body2" color="text.secondary">
              <strong>Dữ liệu trả về:</strong>
            </Typography>
            <pre style={{ 
              backgroundColor: '#f5f5f5', 
              padding: '8px', 
              borderRadius: '4px',
              fontSize: '0.8rem',
              overflow: 'auto',
              maxHeight: '300px'
            }}>
              {JSON.stringify(result.data, null, 2)}
            </pre>
          </CardContent>
        </Card>
      )}
    </Box>
  );
};

export default ApiTestComponent; 