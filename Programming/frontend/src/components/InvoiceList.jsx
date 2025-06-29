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
      <DialogTitle>Invoice List</DialogTitle>
      <DialogContent>
        {invoiceList.length === 0 ? (
          <Typography color="text.secondary">No invoices available.</Typography>
        ) : (
          <List>
            {invoiceList.map((invoice, idx) => (
              <React.Fragment key={invoice.id || idx}>
                <ListItem alignItems="flex-start">
                  <ListItemText
                    primary={
                      <>
                        <Typography variant="subtitle1" fontWeight={600}>
                          Invoice ID: {invoice.id || 'N/A'}
                        </Typography>
                        <Typography variant="body2">
                          Total Amount: {formatPrice(invoice.totalAmount)}
                        </Typography>
                        <Typography variant="body2">
                          Status: {invoice.status || 'Unpaid'}
                        </Typography>
                      </>
                    }
                    secondary={
                      <>
                        <Typography variant="caption" color="text.secondary">
                          Products: {invoice.productList ? invoice.productList.map(p => p.productName).join(', ') : 'N/A'}
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
        <Button onClick={onClose} variant="contained">Close</Button>
      </DialogActions>
    </Dialog>
  );
};

export default InvoiceList; 