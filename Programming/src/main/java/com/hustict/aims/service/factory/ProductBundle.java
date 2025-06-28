package com.hustict.aims.service.factory;

import com.hustict.aims.service.handler.ProductHandler;
import com.hustict.aims.service.validation.ProductValidator;
import com.hustict.aims.utils.mapper.product.ProductMapper;
import com.hustict.aims.model.product.Product;
import com.hustict.aims.dto.product.ProductDetailDTO;

public class ProductBundle<T extends Product, D extends ProductDetailDTO> {
    private final ProductHandler handler;
    private final ProductValidator<T> validator;
    private final ProductMapper<T, D> mapper;
    private final String productType;

    public ProductBundle(
            ProductHandler handler,
            ProductValidator<T> validator,
            ProductMapper<T, D> mapper,
            String productType) {
        this.handler = handler;
        this.validator = validator;
        this.mapper = mapper;
        this.productType = productType;
    }

    public ProductHandler getHandler() {
        return handler;
    }

    public ProductValidator<T> getValidator() {
        return validator;
    }

    public ProductMapper<T, D> getMapper() {
        return mapper;
    }

    public boolean supports(String type) {
        return productType.equalsIgnoreCase(type);
    }
} 