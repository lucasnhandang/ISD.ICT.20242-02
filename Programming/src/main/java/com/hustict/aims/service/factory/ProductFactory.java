package com.hustict.aims.service.factory;

import com.hustict.aims.model.product.Product;
import com.hustict.aims.dto.product.ProductDTO;
import com.hustict.aims.dto.product.ProductModifyRequest;

public interface ProductFactory {
    String getCategory();
    ProductDTO createProduct(ProductModifyRequest request);
    ProductDTO updateProduct(Product existing, ProductModifyRequest request);
    ProductDTO viewProduct(Product product);
}