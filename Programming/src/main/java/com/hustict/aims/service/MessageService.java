package com.hustict.aims.service;

import org.springframework.stereotype.Service;

@Service
public class MessageService {
    
    // Rush Order Messages
    public static final String RUSH_ORDER_ADDRESS_ERROR = "Rush order is only available for delivery in Hanoi.";
    public static final String RUSH_ORDER_NO_ELIGIBLE_PRODUCTS = "There are no products eligible for rush order.";
    public static final String RUSH_ORDER_SUCCESS = "Rush order placed successfully";
    
    // Order Messages
    public static final String ORDER_SUCCESS = "Order placed successfully";
    public static final String ORDER_FAILED = "Order placement failed";
    
    // Validation Messages
    public static final String VALIDATION_ERROR = "Validation failed";
    public static final String INVALID_INPUT = "Invalid input provided";
    
    // Authentication Messages
    public static final String AUTH_FAILED = "Authentication failed";
    public static final String TOKEN_INVALID = "Token is invalid";
    public static final String TOKEN_EXPIRED = "Token expired";
    
    // Product Messages
    public static final String PRODUCT_NOT_FOUND = "Product not found";
    public static final String PRODUCT_UNAVAILABLE = "Product is not available";
    
    // Cart Messages
    public static final String CART_EMPTY = "Cart is empty";
    public static final String CART_INVALID = "Cart is invalid";
    
    // Payment Messages
    public static final String PAYMENT_FAILED = "Payment failed";
    public static final String PAYMENT_SUCCESS = "Payment successful";
    
    // Delivery Messages
    public static final String DELIVERY_VALIDATION_FAILED = "Delivery form validation failed";
    public static final String DELIVERY_INFO_REQUIRED = "Delivery information is required";
    
    // System Messages
    public static final String SYSTEM_ERROR = "System error, please try again";
    public static final String UNEXPECTED_ERROR = "An unexpected error occurred";
    
    public String getRushOrderAddressError() {
        return RUSH_ORDER_ADDRESS_ERROR;
    }
    
    public String getRushOrderNoEligibleProducts() {
        return RUSH_ORDER_NO_ELIGIBLE_PRODUCTS;
    }
    
    public String getRushOrderSuccess() {
        return RUSH_ORDER_SUCCESS;
    }
    
    public String getOrderSuccess() {
        return ORDER_SUCCESS;
    }
    
    public String getOrderFailed() {
        return ORDER_FAILED;
    }
    
    public String getValidationError() {
        return VALIDATION_ERROR;
    }
    
    public String getInvalidInput() {
        return INVALID_INPUT;
    }
    
    public String getAuthFailed() {
        return AUTH_FAILED;
    }
    
    public String getTokenInvalid() {
        return TOKEN_INVALID;
    }
    
    public String getTokenExpired() {
        return TOKEN_EXPIRED;
    }
    
    public String getProductNotFound() {
        return PRODUCT_NOT_FOUND;
    }
    
    public String getProductUnavailable() {
        return PRODUCT_UNAVAILABLE;
    }
    
    public String getCartEmpty() {
        return CART_EMPTY;
    }
    
    public String getCartInvalid() {
        return CART_INVALID;
    }
    
    public String getPaymentFailed() {
        return PAYMENT_FAILED;
    }
    
    public String getPaymentSuccess() {
        return PAYMENT_SUCCESS;
    }
    
    public String getDeliveryValidationFailed() {
        return DELIVERY_VALIDATION_FAILED;
    }
    
    public String getDeliveryInfoRequired() {
        return DELIVERY_INFO_REQUIRED;
    }
    
    public String getSystemError() {
        return SYSTEM_ERROR;
    }
    
    public String getUnexpectedError() {
        return UNEXPECTED_ERROR;
    }
} 