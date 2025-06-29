package com.hustict.aims.service.factory;

import com.hustict.aims.model.product.Product;
import com.hustict.aims.dto.product.ProductDetailDTO;

public interface ProductFactory<T extends Product, D extends ProductDetailDTO> {
    
    String getProductType();

    boolean supports(String productType);
    
    ProductBundle<T, D> createBundle();
}