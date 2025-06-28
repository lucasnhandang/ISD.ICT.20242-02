export const cardStyles = {
  height: '100%',
  display: 'flex',
  flexDirection: 'column',
  backgroundColor: 'background.paper',
  transition: 'all 0.3s cubic-bezier(0.4, 0, 0.2, 1)',
  position: 'relative',
  '&:hover': {
    transform: 'translateY(-4px)',
    boxShadow: '0 12px 24px -10px rgba(0, 0, 0, 0.1)',
    '& .overlay': {
      opacity: 1,
    },
    '& .add-to-cart-btn': {
      opacity: 1,
      transform: 'translate(-50%, -50%)',
    }
  }
};

export const cardMediaStyles = {
  height: 200,
  objectFit: 'contain',
  backgroundColor: '#f8fafc',
  p: 2,
  position: 'relative'
};

export const cardContentStyles = {
  flexGrow: 1,
  display: 'flex',
  flexDirection: 'column',
  p: 2,
  '&:last-child': {
    pb: 2
  }
};

export const titleStyles = {
  fontWeight: 600,
  fontSize: '1rem',
  lineHeight: 1.4,
  mb: 2,
  minHeight: '2.8em',
  display: '-webkit-box',
  WebkitLineClamp: 2,
  WebkitBoxOrient: 'vertical',
  overflow: 'hidden',
  textOverflow: 'ellipsis'
};

export const categoryStyles = {
  color: 'text.secondary',
  fontSize: '0.875rem',
  fontWeight: 500,
  textTransform: 'capitalize',
  backgroundColor: 'background.default',
  py: 0.5,
  px: 1.5,
  borderRadius: 1,
  alignSelf: 'flex-start'
};

export const priceStyles = {
  fontWeight: 700,
  fontSize: '1.125rem',
  color: 'primary.main',
  mt: 'auto',
  pt: 1.5
};

export const overlayStyles = {
  position: 'absolute',
  top: 0,
  left: 0,
  right: 0,
  bottom: 0,
  backgroundColor: 'rgba(0, 0, 0, 0.5)',
  opacity: 0,
  transition: 'opacity 0.3s ease',
  zIndex: 1
};

export const addToCartButtonStyles = {
  position: 'absolute',
  top: '50%',
  left: '50%',
  transform: 'translate(-50%, -50%) translateY(20px)',
  opacity: 0,
  transition: 'all 0.3s ease',
  zIndex: 2,
  backgroundColor: 'primary.main',
  color: 'white',
  padding: '8px 16px',
  minWidth: '140px',
  '&:hover': {
    backgroundColor: 'primary.dark',
  },
  '&.Mui-disabled': {
    backgroundColor: 'rgba(0, 0, 0, 0.12)',
    color: 'rgba(0, 0, 0, 0.26)'
  }
};

export const quantityDialogStyles = {
  '& .MuiDialog-paper': {
    borderRadius: 2,
    padding: 2,
    minWidth: 300,
  }
};

export const quantityInputStyles = {
  width: '100%',
  marginTop: 2,
  marginBottom: 2,
};

export const dialogActionsStyles = {
  padding: 2,
  paddingTop: 0,
}; 