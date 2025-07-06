import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import {
  Box,
  Typography,
  Paper,
  Button,
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  CircularProgress,
  Alert,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Chip
} from '@mui/material';
import { orderManagementAPI } from '../services/api';

// Hàm định dạng số
const formatCurrency = (number) => {
  if (number == null) return '0';
  return new Intl.NumberFormat('vi-VN', {
    style: 'currency',
    currency: 'VND'
  }).format(number);
};

// Hàm chuyển đổi trạng thái đơn hàng sang tiếng Việt
const getOrderStatusText = (status) => {
  const statusMap = {
    'NEW': 'Chưa thanh toán',
    'PENDING': 'Chờ xử lý',
    'APPROVED': 'Đã duyệt',
    'REJECTED_PENDING': 'Đang chờ hoàn tiền (Từ chối)',
    'REJECTED_REFUNDED': 'Đã hoàn tiền (Từ chối)',
    'CANCELLED_PENDING': 'Đang chờ hoàn tiền (Hủy)',
    'CANCELLED_REFUNDED': 'Đã hoàn tiền (Hủy)'
  };
  return statusMap[status] || status;
};

// Hàm lấy màu cho trạng thái
const getStatusColor = (status) => {
  const colorMap = {
    'PENDING': 'warning',
    'APPROVED': 'success',
    'REJECTED_PENDING': 'error',
    'REJECTED_REFUNDED': 'error',
    'CANCELLED_PENDING': 'error',
    'CANCELLED_REFUNDED': 'error'
  };
  return colorMap[status] || 'default';
};

const OrderCancellationPage = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const [order, setOrder] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [confirmDialog, setConfirmDialog] = useState(false);
  const [successDialog, setSuccessDialog] = useState(false);

  useEffect(() => {
    fetchOrderDetails();
  }, [id]);

  const fetchOrderDetails = async () => {
    try {
      console.log('Fetching order details for ID:', id);
      const response = await orderManagementAPI.getOrderDetails(id);
      console.log('Order details received:', response);
      setOrder(response);
      setError(null);
    } catch (err) {
      console.error('Error fetching order details:', err);
      setError('Không thể tải thông tin đơn hàng. Vui lòng thử lại sau.');
      setOrder(null);
    } finally {
      setLoading(false);
    }
  };

  const handleCancelOrder = async () => {
    try {
      console.log('Cancelling order:', id);
      await orderManagementAPI.cancelOrder(id);
      console.log('Order cancelled successfully');
      setConfirmDialog(false);
      setSuccessDialog(true);
      setTimeout(() => {
        navigate('/');
      }, 5000);
    } catch (err) {
      console.error('Error cancelling order:', err);
      setError('Không thể hủy đơn hàng. Vui lòng thử lại sau.');
      setConfirmDialog(false);
    }
  };

  // Tính tổng tiền từ danh sách sản phẩm
  const calculateTotal = (products) => {
    if (!products) return 0;
    return products.reduce((sum, product) => sum + (product.price * product.quantity), 0);
  };

  if (loading) {
    return (
      <Box sx={{ display: 'flex', justifyContent: 'center', alignItems: 'center', height: '100vh' }}>
        <CircularProgress />
      </Box>
    );
  }

  if (error) {
    return (
      <Box sx={{ p: 3 }}>
        <Alert severity="error">{error}</Alert>
      </Box>
    );
  }

  return (
    <Box sx={{ p: 3, maxWidth: 800, margin: '0 auto' }}>
      <Paper sx={{ p: 3 }}>
        <Typography variant="h5" gutterBottom>
          Xác nhận hủy đơn hàng #{id}
        </Typography>

        {order && (
          <>
            <Box sx={{ my: 3 }}>
              <Typography variant="h6" gutterBottom>
                Thông tin đơn hàng
              </Typography>
              <TableContainer>
                <Table>
                  <TableHead>
                    <TableRow>
                      <TableCell>Sản phẩm</TableCell>
                      <TableCell align="right">Số lượng</TableCell>
                      <TableCell align="right">Đơn giá</TableCell>
                      <TableCell align="right">Thành tiền</TableCell>
                    </TableRow>
                  </TableHead>
                  <TableBody>
                    {order.productList && order.productList.map((product) => (
                      <TableRow key={product.productId}>
                        <TableCell>{product.productName}</TableCell>
                        <TableCell align="right">{product.quantity}</TableCell>
                        <TableCell align="right">{formatCurrency(product.price)}</TableCell>
                        <TableCell align="right">
                          {formatCurrency(product.price * product.quantity)}
                        </TableCell>
                      </TableRow>
                    ))}
                    <TableRow>
                      <TableCell colSpan={3} align="right">
                        <strong>Tổng tiền sản phẩm:</strong>
                      </TableCell>
                      <TableCell align="right">
                        <strong>{formatCurrency(calculateTotal(order.productList))}</strong>
                      </TableCell>
                    </TableRow>
                    {order.shippingFee && (
                      <TableRow>
                        <TableCell colSpan={3} align="right">
                          <strong>Phí vận chuyển:</strong>
                        </TableCell>
                        <TableCell align="right">
                          <strong>{formatCurrency(order.shippingFee)}</strong>
                        </TableCell>
                      </TableRow>
                    )}
                    <TableRow>
                      <TableCell colSpan={3} align="right">
                        <strong>Tổng cộng:</strong>
                      </TableCell>
                      <TableCell align="right">
                        <strong>{formatCurrency(order.totalAmount)}</strong>
                      </TableCell>
                    </TableRow>
                  </TableBody>
                </Table>
              </TableContainer>
            </Box>

            <Box sx={{ my: 2 }}>
              {order.isRushOrder && (
                <Typography variant="subtitle1" gutterBottom>
                  <Chip label="Đơn hàng nhanh" color="error" />
                </Typography>
              )}
            </Box>

            <Box sx={{ mt: 3, display: 'flex', justifyContent: 'center', gap: 2 }}>
              <Button
                variant="contained"
                color="error"
                onClick={() => setConfirmDialog(true)}
              >
                Hủy đơn hàng
              </Button>
              <Button
                variant="outlined"
                onClick={() => navigate('/')}
              >
                Quay lại
              </Button>
            </Box>
          </>
        )}
      </Paper>

      {/* Dialog xác nhận hủy */}
      <Dialog open={confirmDialog} onClose={() => setConfirmDialog(false)}>
        <DialogTitle>Xác nhận hủy đơn hàng</DialogTitle>
        <DialogContent>
          <Typography>
            Bạn có chắc chắn muốn hủy đơn hàng này không? Hành động này không thể hoàn tác.
          </Typography>
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setConfirmDialog(false)}>Không</Button>
          <Button onClick={handleCancelOrder} color="error" variant="contained">
            Có, hủy đơn hàng
          </Button>
        </DialogActions>
      </Dialog>

      {/* Dialog thông báo thành công */}
      <Dialog open={successDialog}>
        <DialogTitle>Hủy đơn hàng thành công</DialogTitle>
        <DialogContent>
          <Typography>
            Đơn hàng của bạn đã được hủy thành công. Nếu bạn đã thanh toán, 
            tiền sẽ được hoàn trả trong vòng 3-5 ngày làm việc.
          </Typography>
          <Typography sx={{ mt: 2 }}>
            Trang sẽ tự động đóng sau 5 giây...
          </Typography>
        </DialogContent>
      </Dialog>
    </Box>
  );
};

export default OrderCancellationPage;