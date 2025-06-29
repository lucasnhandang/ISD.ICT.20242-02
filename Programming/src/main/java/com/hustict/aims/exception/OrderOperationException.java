package com.hustict.aims.exception;

public class OrderOperationException extends RuntimeException {
    public OrderOperationException(String message) {
        super(message);
    }
     public OrderOperationException(String message, Throwable cause) {
        super(message, cause);
    }
} 