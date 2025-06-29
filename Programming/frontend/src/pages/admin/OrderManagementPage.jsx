import React, { useState, useEffect } from 'react';
import {
  Box,
  Typography,
  Paper,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Button,
  Chip,
  IconButton,
  Collapse,
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  Snackbar,
  Alert,
  TablePagination,
  CircularProgress,
  Card,
  CardContent,
} from '@mui/material';
import {
  CheckCircle as CheckCircleIcon,
  Cancel as CancelIcon,
  KeyboardArrowDown as KeyboardArrowDownIcon,
  KeyboardArrowUp as KeyboardArrowUpIcon,
  Refresh as RefreshIcon,
  Warning as WarningIcon,
} from '@mui/icons-material';
import { orderManagementAPI, checkBackendConnection } from '../../services/api';

const Row = ({ order, onApprove, onReject }) => {
  const [open, setOpen] = useState(false);

  return (
    <>
      <TableRow sx={{ '& > *': { borderBottom: 'unset' } }}>
        <TableCell>
          <IconButton size="small" onClick={() => setOpen(!open)}>
            {open ? <KeyboardArrowUpIcon /> : <KeyboardArrowDownIcon />}
          </IconButton>
        </TableCell>
        <TableCell>#{order.orderId}</TableCell>
        <TableCell>{order.orderDate}</TableCell>
        <TableCell>
          <Chip 
            label={order.isRushOrder ? "Rush" : "Normal"} 
            color={order.isRushOrder ? "error" : "primary"} 
            size="small" 
          />
        </TableCell>
        <TableCell>{order.currency} {order.productList.reduce((sum, item) => sum + item.price * item.quantity, 0)}</TableCell>
        <TableCell>
          <Button
            startIcon={<CheckCircleIcon />}
            variant="contained"
            color="success"
            size="small"
            onClick={() => onApprove(order.orderId)}
            sx={{ mr: 1 }}
          >
            Approve
          </Button>
          <Button
            startIcon={<CancelIcon />}
            variant="contained"
            color="error"
            size="small"
            onClick={() => onReject(order.orderId)}
          >
            Reject
          </Button>
        </TableCell>
      </TableRow>
      <TableRow>
        <TableCell style={{ paddingBottom: 0, paddingTop: 0 }} colSpan={6}>
          <Collapse in={open} timeout="auto" unmountOnExit>
            <Box sx={{ margin: 1 }}>
              <Typography variant="h6" gutterBottom component="div">
                Order Details
              </Typography>
              <Table size="small">
                <TableHead>
                  <TableRow>
                    <TableCell>Product Name</TableCell>
                    <TableCell align="right">Quantity</TableCell>
                    <TableCell align="right">Price</TableCell>
                    <TableCell align="right">Total</TableCell>
                  </TableRow>
                </TableHead>
                <TableBody>
                  {order.productList.map((product) => (
                    <TableRow key={product.productID}>
                      <TableCell component="th" scope="row">
                        {product.productName}
                      </TableCell>
                      <TableCell align="right">{product.quantity}</TableCell>
                      <TableCell align="right">{order.currency} {product.price}</TableCell>
                      <TableCell align="right">{order.currency} {product.price * product.quantity}</TableCell>
                    </TableRow>
                  ))}
                </TableBody>
              </Table>
              {order.isRushOrder && order.rushDeliveryTime && (
                <Typography variant="body2" color="text.secondary" sx={{ mt: 2 }}>
                  Rush Delivery Time: {order.rushDeliveryTime}
                </Typography>
              )}
            </Box>
          </Collapse>
        </TableCell>
      </TableRow>
    </>
  );
};

const OrderManagementPage = () => {
  const [pendingOrders, setPendingOrders] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [confirmDialog, setConfirmDialog] = useState({ open: false, orderId: null, action: null });
  const [snackbar, setSnackbar] = useState({ open: false, message: '', severity: 'success' });
  const [page, setPage] = useState(0);
  const [rowsPerPage] = useState(30);

  const fetchPendingOrders = async (retryCount = 0) => {
    try {
      setLoading(true);
      setError(null);
      
      console.log('🔄 Đang kiểm tra kết nối backend...');
      
      // Kiểm tra kết nối backend trước
      const connectionStatus = await checkBackendConnection();
      if (!connectionStatus.connected) {
        throw new Error(`Không thể kết nối backend: ${connectionStatus.message}`);
      }
      
      console.log('✅ Backend connection OK, đang tải pending orders...');
      const data = await orderManagementAPI.getPendingOrders();
      setPendingOrders(data);
      setError(null);
      
      console.log(`✅ Đã tải thành công ${data.length} pending orders`);
      
    } catch (err) {
      console.error(`❌ Lỗi lần thử ${retryCount + 1}:`, err);
      
      // Retry logic - thử lại tối đa 2 lần
      if (retryCount < 2) {
        console.log(`🔄 Đang thử lại lần ${retryCount + 2}...`);
        setTimeout(() => {
          fetchPendingOrders(retryCount + 1);
        }, 2000); // Đợi 2 giây trước khi retry
        return;
      }
      
      // Thông báo lỗi chi tiết hơn
      let errorMessage = 'Không thể tải danh sách đơn hàng. ';
      
      if (err.message.includes('Network Error') || err.message.includes('ERR_NETWORK')) {
        errorMessage += 'Vui lòng kiểm tra:\n• Kết nối mạng\n• Backend server đang chạy\n• Firewall/Antivirus blocking';
      } else if (err.message.includes('timeout')) {
        errorMessage += 'Kết nối quá chậm. Vui lòng thử lại.';
      } else if (err.message.includes('404')) {
        errorMessage += 'API endpoint không tồn tại.';
      } else {
        errorMessage += err.message;
      }
      
      setError(errorMessage);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchPendingOrders();
  }, []);

  const handleChangePage = (event, newPage) => {
    setPage(newPage);
  };

  const handleApprove = async (orderId) => {
    setConfirmDialog({ open: true, orderId, action: 'approve' });
  };

  const handleReject = async (orderId) => {
    setConfirmDialog({ open: true, orderId, action: 'reject' });
  };

  const handleConfirmAction = async () => {
    const { orderId, action } = confirmDialog;
    
    // Close dialog immediately
    setConfirmDialog({ open: false, orderId: null, action: null });
    
    // Show processing message
    setSnackbar({
      open: true,
      message: `Processing ${action} for order #${orderId}...`,
      severity: 'info'
    });

    // Remove the order from the list immediately for better UX
    setPendingOrders(prev => prev.filter(order => order.orderId !== orderId));

    // Set a timeout to refresh the page after 2 seconds
    setTimeout(() => {
      fetchPendingOrders();
    }, 2000);

    try {
      if (action === 'approve') {
        await orderManagementAPI.approveOrder(orderId);
        setSnackbar({
          open: true,
          message: `Order #${orderId} has been approved successfully`,
          severity: 'success'
        });
      } else {
        await orderManagementAPI.rejectOrder(orderId);
        setSnackbar({
          open: true,
          message: `Order #${orderId} has been rejected successfully`,
          severity: 'success'
        });
      }
    } catch (err) {
    }
  };

  const handleCloseSnackbar = () => {
    setSnackbar({ ...snackbar, open: false });
  };

  if (loading) {
    return (
      <Box sx={{ p: 3, display: 'flex', flexDirection: 'column', alignItems: 'center' }}>
        <CircularProgress size={40} sx={{ mb: 2 }} />
        <Typography variant="h6" gutterBottom>Đang tải đơn hàng...</Typography>
        <Typography variant="body2" color="text.secondary">
          Vui lòng đợi trong giây lát
        </Typography>
      </Box>
    );
  }

  // Calculate the orders to display on the current page
  const displayedOrders = pendingOrders.slice(
    page * rowsPerPage,
    page * rowsPerPage + rowsPerPage
  );

  return (
    <Box sx={{ p: 3 }}>
      <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 3 }}>
        <Typography variant="h4">
          Quản lý đơn hàng
        </Typography>
        <Box sx={{ display: 'flex', gap: 2 }}>
          <Button
            variant="outlined"
            startIcon={<RefreshIcon />}
            onClick={() => fetchPendingOrders()}
            disabled={loading}
          >
            Làm mới
          </Button>
          {/* <Button
            variant="text"
            size="small"
            onClick={() => window.open('/api-test', '_blank')}
          >
            🔧 Test API
          </Button> */}
        </Box>
      </Box>
      
      {error && (
        <Card sx={{ mb: 2 }}>
          <CardContent>
            <Alert severity="error" sx={{ mb: 2 }}>
              <Typography variant="h6" sx={{ display: 'flex', alignItems: 'center' }}>
                <WarningIcon sx={{ mr: 1 }} />
                Lỗi kết nối
              </Typography>
            </Alert>
            <Typography variant="body2" sx={{ whiteSpace: 'pre-line', mb: 2 }}>
              {error}
            </Typography>
            <Box sx={{ display: 'flex', gap: 2 }}>
              <Button 
                variant="contained" 
                onClick={() => fetchPendingOrders()}
                disabled={loading}
                startIcon={<RefreshIcon />}
              >
                Thử lại
              </Button>
              <Button 
                variant="outlined" 
                onClick={() => window.open('http://localhost:8080/api/v1/product-manager/orders/pending', '_blank')}
              >
                Test API trực tiếp
              </Button>
            </Box>
          </CardContent>
        </Card>
      )}

      <Paper sx={{ width: '100%', mb: 2 }}>
        <TableContainer>
          <Table>
            <TableHead>
              <TableRow>
                <TableCell />
                <TableCell>Order ID</TableCell>
                <TableCell>Date</TableCell>
                <TableCell>Type</TableCell>
                <TableCell>Total</TableCell>
                <TableCell>Actions</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {displayedOrders.map((order) => (
                <Row
                  key={order.orderId}
                  order={order}
                  onApprove={handleApprove}
                  onReject={handleReject}
                />
              ))}
              {displayedOrders.length === 0 && (
                <TableRow>
                  <TableCell colSpan={6} align="center">
                    <Typography variant="body2" color="text.secondary">
                      No pending orders found
                    </Typography>
                  </TableCell>
                </TableRow>
              )}
            </TableBody>
          </Table>
        </TableContainer>
        <TablePagination
          component="div"
          count={pendingOrders.length}
          page={page}
          onPageChange={handleChangePage}
          rowsPerPage={rowsPerPage}
          rowsPerPageOptions={[30]}
        />
      </Paper>

      <Dialog
        open={confirmDialog.open}
        onClose={() => setConfirmDialog({ open: false, orderId: null, action: null })}
      >
        <DialogTitle>
          Confirm {confirmDialog.action === 'approve' ? 'Approval' : 'Rejection'}
        </DialogTitle>
        <DialogContent>
          <Typography>
            Are you sure you want to {confirmDialog.action} order #{confirmDialog.orderId}?
          </Typography>
        </DialogContent>
        <DialogActions>
          <Button
            onClick={() => setConfirmDialog({ open: false, orderId: null, action: null })}
          >
            Cancel
          </Button>
          <Button
            onClick={handleConfirmAction}
            variant="contained"
            color={confirmDialog.action === 'approve' ? 'success' : 'error'}
            autoFocus
          >
            Confirm
          </Button>
        </DialogActions>
      </Dialog>

      <Snackbar
        open={snackbar.open}
        autoHideDuration={6000}
        onClose={handleCloseSnackbar}
        anchorOrigin={{ vertical: 'bottom', horizontal: 'right' }}
      >
        <Alert onClose={handleCloseSnackbar} severity={snackbar.severity} sx={{ width: '100%' }}>
          {snackbar.message}
        </Alert>
      </Snackbar>
    </Box>
  );
};

export default OrderManagementPage; 