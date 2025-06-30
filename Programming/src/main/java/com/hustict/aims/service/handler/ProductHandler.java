package com.hustict.aims.service.handler;

import com.hustict.aims.dto.product.ProductDetailDTO;
import com.hustict.aims.model.product.Product;

import java.util.Map;

public interface ProductHandler {
    String getType();
    boolean supports(String type);
    Product toEntity(Map<String, Object> data);
    Product updateEntity(Product existing, Map<String, Object> data);
    Product save(Product product);
    ProductDetailDTO toDTO(Product product);
}