package com.hustict.aims.exception;

public class ProductNotFoundException extends ProductException {
    private final Long productId;
    private final String identifier;
    
    public ProductNotFoundException(Long productId) {
        super("Product not found with ID: " + productId);
        this.productId = productId;
        this.identifier = String.valueOf(productId);
    }
    
    public ProductNotFoundException(String identifier) {
        super("Product not found: " + identifier);
        this.productId = null;
        this.identifier = identifier;
    }

    @Override
    public String getErrorCode() {
        return "PRODUCT_NOT_FOUND";
    }
    
    public Long getProductId() {
        return productId;
    }
    
    public String getIdentifier() {
        return identifier;
    }
} 