import React, { useState, useEffect } from 'react';
import { Alert, Snackbar } from '@mui/material';

const ConnectionStatus = ({ open, message, severity }) => {
  const [isOpen, setIsOpen] = useState(open);

  useEffect(() => {
    setIsOpen(open);
    if (open) {
      const timer = setTimeout(() => {
        setIsOpen(false);
      }, 1500);
      return () => clearTimeout(timer);
    }
  }, [open]);

  return (
    <Snackbar 
      open={isOpen} 
      anchorOrigin={{ vertical: 'top', horizontal: 'center' }}
      sx={{ mt: 8 }}
      onClose={() => setIsOpen(false)}
    >
      <Alert severity={severity} sx={{ width: '100%' }}>
        {message}
      </Alert>
    </Snackbar>
  );
};

export default ConnectionStatus;