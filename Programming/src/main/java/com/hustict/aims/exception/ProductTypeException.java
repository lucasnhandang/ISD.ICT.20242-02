package com.hustict.aims.exception;

public class ProductTypeException extends ProductException {
    private final String requestedType;
    
    public ProductTypeException(String requestedType) {
        super("Unsupported product type: " + requestedType);
        this.requestedType = requestedType;
    }

    @Override
    public String getErrorCode() {
        return "PRODUCT_TYPE_UNSUPPORTED";
    }
    
    public String getRequestedType() {
        return requestedType;
    }
} 