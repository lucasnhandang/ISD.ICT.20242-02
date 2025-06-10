export const cardStyles = {
  height: '100%',
  display: 'flex',
  flexDirection: 'column',
  backgroundColor: 'background.paper',
  transition: 'all 0.3s cubic-bezier(0.4, 0, 0.2, 1)',
  '&:hover': {
    transform: 'translateY(-4px)',
    boxShadow: '0 12px 24px -10px rgba(0, 0, 0, 0.1)',
  }
};

export const cardMediaStyles = {
  height: 200,
  objectFit: 'contain',
  backgroundColor: '#f8fafc',
  p: 2
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