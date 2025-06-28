import React from 'react';
import {
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  Button,
  List,
  ListItem,
  ListItemText,
  Typography,
  Divider
} from '@mui/material';

const formatPrice = (price) => {
  return new Intl.NumberFormat('vi-VN', {
    style: 'currency',
    currency: 'VND'
  }).format(price);
};

const InvoiceList = ({ open, onClose, invoiceList = [] }) => {
  return (
    <Dialog open={open} onClose={onClose} maxWidth="sm" fullWidth>
      <DialogTitle>Danh sách hóa đơn</DialogTitle>
      <DialogContent>
        {invoiceList.length === 0 ? (
          <Typography color="text.secondary">Không có hóa đơn nào.</Typography>
        ) : (
          <List>
            {invoiceList.map((invoice, idx) => (
              <React.Fragment key={invoice.id || idx}>
                <ListItem alignItems="flex-start">
                  <ListItemText
                    primary={
                      <>
                        <Typography variant="subtitle1" fontWeight={600}>
                          Mã hóa đơn: {invoice.id || 'N/A'}
                        </Typography>
                        <Typography variant="body2">
                          Tổng tiền: {formatPrice(invoice.totalAmount)}
                        </Typography>
                        <Typography variant="body2">
                          Trạng thái: {invoice.status || 'Chưa thanh toán'}
                        </Typography>
                      </>
                    }
                    secondary={
                      <>
                        <Typography variant="caption" color="text.secondary">
                          Sản phẩm: {invoice.productList ? invoice.productList.map(p => p.productName).join(', ') : 'N/A'}
                        </Typography>
                      </>
                    }
                  />
                </ListItem>
                {idx < invoiceList.length - 1 && <Divider />}
              </React.Fragment>
            ))}
          </List>
        )}
      </DialogContent>
      <DialogActions>
        <Button onClick={onClose} variant="contained">Đóng</Button>
      </DialogActions>
    </Dialog>
  );
};

export default InvoiceList; 