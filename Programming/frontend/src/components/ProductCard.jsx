import React from 'react';
import { Card, CardContent, CardMedia, Typography, Box } from '@mui/material';
import { 
  cardStyles, 
  cardMediaStyles, 
  cardContentStyles,
  titleStyles, 
  categoryStyles,
  priceStyles 
} from '../styles/ProductCard.styles';

const formatPrice = (price) => {
  return new Intl.NumberFormat('vi-VN', {
    style: 'currency',
    currency: 'VND'
  }).format(price);
};

const ProductCard = ({ product }) => {
  return (
    <Card sx={cardStyles}>
      <CardMedia
        component="img"
        image={product.imageUrl || '/placeholder-book.jpg'}
        alt={product.title}
        sx={cardMediaStyles}
      />
      <CardContent sx={cardContentStyles}>
        <Typography 
          variant="h6" 
          component="h2"
          sx={titleStyles}
        >
          {product.title}
        </Typography>
        
        <Typography 
          component="span"
          sx={categoryStyles}
        >
          {product.category}
        </Typography>

        <Typography 
          variant="h6" 
          sx={priceStyles}
        >
          {formatPrice(product.currentPrice)}
        </Typography>
      </CardContent>
    </Card>
  );
};

export default ProductCard; 