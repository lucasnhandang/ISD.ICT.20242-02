import React from 'react';
import { useNavigate } from 'react-router-dom';
import { 
  Box, 
  Typography, 
  Paper, 
  Button, 
  Divider, 
  Alert,
  Grid,
  Chip
} from '@mui/material';
import { 
  AccessTime, 
  LocalShipping, 
  Payment,
  CheckCircle 
} from '@mui/icons-material';

const formatPrice = (price) => {
  return new Intl.NumberFormat('vi-VN', {
    style: 'currency',
    currency: 'VND'
  }).format(price);
};

const formatDateTime = (dateTimeString) => {
  if (!dateTimeString) return 'Chưa xác định';
  return new Date(dateTimeString).toLocaleString('vi-VN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit'
  });
};

const InvoiceCard = ({ 
  title, 
  icon, 
  invoice, 
  orderId, 
  onPay, 
  deliveryTime = null, 
  chipColor = 'primary',
  description 
}) => {
  const navigate = useNavigate();

  return (
    <Paper 
      elevation={3} 
      sx={{ 
        p: 3, 
        mb: 3,
        border: '2px solid',
        borderColor: chipColor === 'warning' ? 'warning.main' : 'primary.main',
        borderRadius: 2,
        transition: 'all 0.3s ease',
        '&:hover': {
          boxShadow: 6,
          transform: 'translateY(-2px)'
        }
      }}
    >
      {/* Header */}
      <Box sx={{ display: 'flex', alignItems: 'center', mb: 2 }}>
        {icon}
        <Typography variant="h6" sx={{ ml: 1, fontWeight: 600, flex: 1 }}>
          {title}
        </Typography>
        <Chip 
          label={chipColor === 'warning' ? 'Giao Nhanh' : 'Giao Thường'} 
          color={chipColor}
          size="small"
          sx={{ fontWeight: 600 }}
        />
      </Box>

      {/* Description */}
      {description && (
        <Typography variant="body2" color="text.secondary" sx={{ mb: 2 }}>
          {description}
        </Typography>
      )}

      {/* Delivery Time */}
      {deliveryTime && (
        <Box sx={{ display: 'flex', alignItems: 'center', mb: 2 }}>
          <AccessTime sx={{ mr: 1, color: 'text.secondary' }} />
          <Typography variant="body2">
            <strong>Dự kiến giao hàng:</strong> {formatDateTime(deliveryTime)}
          </Typography>
        </Box>
      )}

      <Divider sx={{ mb: 2 }} />

      {/* Invoice Details */}
      <Grid container spacing={2} sx={{ mb: 3 }}>
        <Grid item xs={6}>
          <Typography variant="body2" color="text.secondary">
            Tiền hàng (có VAT):
          </Typography>
          <Typography variant="body1" sx={{ fontWeight: 500 }}>
            {formatPrice(invoice.productPriceIncVAT)}
          </Typography>
        </Grid>
        <Grid item xs={6}>
          <Typography variant="body2" color="text.secondary">
            Phí giao hàng:
          </Typography>
          <Typography variant="body1" sx={{ fontWeight: 500 }}>
            {formatPrice(invoice.shippingFee)}
          </Typography>
        </Grid>
        <Grid item xs={6}>
          <Typography variant="body2" color="text.secondary">
            VAT:
          </Typography>
          <Typography variant="body1" sx={{ fontWeight: 500 }}>
            {formatPrice(invoice.productPriceIncVAT - invoice.productPriceExVAT)}
          </Typography>
        </Grid>
        <Grid item xs={6}>
          <Typography variant="body2" color="text.secondary">
            Mã đơn hàng:
          </Typography>
          <Typography variant="body1" sx={{ fontWeight: 500 }}>
            #{orderId}
          </Typography>
        </Grid>
      </Grid>

      <Divider sx={{ mb: 2 }} />

      {/* Total Amount */}
      <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 3 }}>
        <Typography variant="h6" sx={{ fontWeight: 600 }}>
          Tổng cộng:
        </Typography>
        <Typography variant="h5" color="primary" sx={{ fontWeight: 700 }}>
          {formatPrice(invoice.totalAmount)}
        </Typography>
      </Box>

      {/* Payment Button */}
      <Button
        variant="contained"
        color="primary"
        fullWidth
        size="large"
        startIcon={<Payment />}
        onClick={onPay}
        sx={{ 
          fontWeight: 600,
          py: 1.5,
          fontSize: '1rem',
          borderRadius: 2
        }}
      >
        Thanh toán đơn hàng này
      </Button>
    </Paper>
  );
};

const RushOrderResults = ({ orderData, onPayment, expectedDateTime }) => {
  const navigate = useNavigate();

  if (!orderData) {
    return (
      <Alert severity="error">
        Không có dữ liệu đơn hàng. Vui lòng thử lại.
      </Alert>
    );
  }

  const { rushOrderId, normalOrderId, rushInvoice, normalInvoice, message } = orderData;

  return (
    <Box sx={{ maxWidth: 1000, mx: 'auto', p: 2 }}>
      {/* Success Message */}
      <Alert severity="success" sx={{ mb: 4 }}>
        <Box sx={{ display: 'flex', alignItems: 'center' }}>
          <CheckCircle sx={{ mr: 1 }} />
          <Typography sx={{ fontWeight: 500 }}>
            {message || 'Đơn hàng đã được tách thành công!'}
          </Typography>
        </Box>
      </Alert>

      {/* Header */}
      <Box sx={{ textAlign: 'center', mb: 4 }}>
        <Typography variant="h4" gutterBottom sx={{ fontWeight: 600 }}>
          Đơn hàng của bạn đã được tách
        </Typography>
        <Typography variant="body1" color="text.secondary">
          Bạn có thể thanh toán từng đơn hàng riêng biệt theo nhu cầu
        </Typography>
      </Box>

      {/* Rush Order Invoice */}
      {rushOrderId && rushInvoice && (
        <InvoiceCard
          title="🚀 Đơn giao hàng nhanh"
          icon={<LocalShipping sx={{ color: 'warning.main' }} />}
          invoice={rushInvoice}
          orderId={rushOrderId}
          deliveryTime={expectedDateTime}
          chipColor="warning"
          description="Các sản phẩm hỗ trợ giao hàng nhanh trong cùng ngày"
                     onPay={() => onPayment(rushOrderId, 'rush', rushInvoice)}
        />
      )}

      {/* Normal Order Invoice */}
      {normalOrderId && normalInvoice && (
        <InvoiceCard
          title="📦 Đơn giao hàng thường"
          icon={<LocalShipping sx={{ color: 'primary.main' }} />}
          invoice={normalInvoice}
          orderId={normalOrderId}
          chipColor="primary"
          description="Các sản phẩm giao hàng theo lịch thường (3-5 ngày)"
                     onPay={() => onPayment(normalOrderId, 'normal', normalInvoice)}
        />
      )}

      {/* Info Alert */}
      <Alert severity="info" sx={{ mt: 3 }}>
        <Typography variant="body2">
          <strong>Lưu ý:</strong> Bạn có thể thanh toán từng đơn hàng độc lập. 
          Đơn hàng nào được thanh toán trước sẽ được xử lý và giao trước.
        </Typography>
      </Alert>

      {/* Navigation Buttons */}
      <Box sx={{ display: 'flex', gap: 2, mt: 4 }}>
        <Button
          variant="outlined"
          onClick={() => navigate('/')}
          sx={{ flex: 1 }}
        >
          Về trang chủ
        </Button>
        <Button
          variant="outlined"
          onClick={() => navigate('/cart')}
          sx={{ flex: 1 }}
        >
          Xem giỏ hàng
        </Button>
      </Box>
    </Box>
  );
};

export default RushOrderResults; 