import { styled, alpha } from '@mui/material/styles';
import { InputBase, CircularProgress } from '@mui/material';

export const Search = styled('div')(({ theme }) => ({
  position: 'relative',
  borderRadius: '45px',
  backgroundColor: theme.palette.background.paper,
  border: `1px solid ${alpha(theme.palette.text.primary, 0.1)}`,
  '&:hover': {
    border: `1px solid ${alpha(theme.palette.text.primary, 0.2)}`,
    boxShadow: `0 0 0 4px ${alpha(theme.palette.primary.main, 0.1)}`,
  },
  marginRight: theme.spacing(2),
  marginLeft: 0,
  width: '100%',
  transition: 'all 0.2s ease',
  [theme.breakpoints.up('sm')]: {
    marginLeft: theme.spacing(3),
    width: 'auto',
  },
}));

export const SearchIconWrapper = styled('div')(({ theme }) => ({
  padding: theme.spacing(0, 2),
  height: '100%',
  position: 'absolute',
  pointerEvents: 'none',
  display: 'flex',
  alignItems: 'center',
  justifyContent: 'center',
  color: theme.palette.text.secondary,
}));

export const StyledInputBase = styled(InputBase)(({ theme }) => ({
  color: theme.palette.text.primary,
  width: '100%',
  '& .MuiInputBase-input': {
    padding: theme.spacing(1.5, 1, 1.5, 0),
    paddingLeft: `calc(1em + ${theme.spacing(4)})`,
    paddingRight: theme.spacing(2),
    transition: theme.transitions.create('width'),
    fontSize: '0.95rem',
    width: '100%',
    [theme.breakpoints.up('md')]: {
      width: '40ch',
    },
  },
}));

export const LoadingIndicator = styled(CircularProgress)(({ theme }) => ({
  position: 'absolute',
  right: theme.spacing(1),
  top: '50%',
  transform: 'translateY(-50%)',
  width: '20px !important',
  height: '20px !important',
}));

export const logoStyles = {
  display: { xs: 'none', sm: 'block' },
  background: 'linear-gradient(45deg, #2563eb 30%, #60a5fa 90%)',
  WebkitBackgroundClip: 'text',
  WebkitTextFillColor: 'transparent',
  fontWeight: 700,
  fontSize: '1.75rem',
  letterSpacing: '-0.5px',
  cursor: 'pointer',
  fontFamily: '"Be Vietnam Pro", sans-serif',
};

export const signInButtonStyles = {
  px: 3,
  py: 1,
  fontWeight: 600,
  borderRadius: '45px',
  boxShadow: 'none',
  '&:hover': {
    boxShadow: 'none',
    backgroundColor: 'primary.dark',
  }
}; 