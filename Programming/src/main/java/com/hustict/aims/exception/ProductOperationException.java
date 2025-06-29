package com.hustict.aims.exception;

public class ProductOperationException extends ProductException {
    private final String operation;
    
    public ProductOperationException(String message) {
        super(message);
        this.operation = "unknown";
    }
    
    public ProductOperationException(String operation, String message) {
        super(message);
        this.operation = operation;
    }
    
    @Override
    public String getErrorCode() {
        return "PRODUCT_OPERATION_FAILED";
    }
    
    public String getOperation() {
        return operation;
    }
}