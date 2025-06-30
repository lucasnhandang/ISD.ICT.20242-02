package com.hustict.aims.service.factory;

import com.hustict.aims.model.product.Product;
import com.hustict.aims.dto.product.ProductDetailDTO;

import java.util.Map;

public interface ProductFactory<T extends Product, D extends ProductDetailDTO> {
    String getProductType();
    boolean supports(String productType);
    
    D createProduct(Map<String, Object> data);
    D updateProduct(T existing, Map<String, Object> data);
    D viewProduct(T product);
}