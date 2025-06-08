package com.hustict.aims.utils.mapper;

import com.hustict.aims.dto.home.ProductSummaryDTO;
import com.hustict.aims.model.product.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductSummaryMapper {
    public ProductSummaryDTO toSummaryDTO(Product product) {
        return new ProductSummaryDTO(
            product.getId(),
            product.getTitle(),
            product.getCategory(),
            product.getCurrentPrice(),
            product.getQuantity(),
            product.getImageUrl()
        );
    }
}
