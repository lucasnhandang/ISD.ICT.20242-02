// src/main/java/com/hustict/aims/exception/GlobalExceptionHandler.java
package com.hustict.aims.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@ControllerAdvice
public class GlobalExceptionHandler {

    // Standardized error response format
    private Map<String, Object> createErrorResponse(String error, String message, HttpStatus status) {
        Map<String, Object> map = new HashMap<>();
        map.put("timestamp", LocalDateTime.now());
        map.put("status", status.value());
        map.put("error", error);
        map.put("message", message);
        return map;
    }

    // === NEW PRODUCT EXCEPTION HANDLERS ===
    
    // Handle base product exceptions (catch-all for ProductException hierarchy)
    @ExceptionHandler(ProductException.class)
    public ResponseEntity<Map<String, Object>> handleProductException(ProductException e) {
        Map<String, Object> response = createErrorResponse(e.getErrorCode(), e.getMessage(), HttpStatus.BAD_REQUEST);
        
        // Add specific details for certain exception types
        if (e instanceof ProductTypeException pte) {
            response.put("requestedType", pte.getRequestedType());
        } else if (e instanceof ProductNotFoundException pnfe) {
            response.put("productId", pnfe.getProductId());
            response.put("identifier", pnfe.getIdentifier());
        } else if (e instanceof ProductOperationException poe) {
            response.put("operation", poe.getOperation());
        } else if (e instanceof ProductValidationException pve) {
            response.put("validationErrors", pve.getValidationErrors());
        }
        
        return ResponseEntity.badRequest().body(response);
    }

    // Handle specific product not found exceptions with 404 status
    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleProductNotFound(ProductNotFoundException e) {
        Map<String, Object> response = createErrorResponse(e.getErrorCode(), e.getMessage(), HttpStatus.NOT_FOUND);
        response.put("productId", e.getProductId());
        response.put("identifier", e.getIdentifier());
        
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    // === EXISTING EXCEPTION HANDLERS (MAINTAINED FOR BACKWARD COMPATIBILITY) ===

    // Handle validation errors
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleValidationError(IllegalArgumentException e) {
        return ResponseEntity.badRequest()
            .body(createErrorResponse("VALIDATION_ERROR", e.getMessage(), HttpStatus.BAD_REQUEST));
    }

    // Handle not found errors
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(NoSuchElementException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(createErrorResponse("NOT_FOUND", e.getMessage(), HttpStatus.NOT_FOUND));
    }

    // Handle authentication/security errors
    @ExceptionHandler(SecurityException.class)
    public ResponseEntity<Map<String, Object>> handleSecurityError(SecurityException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body(createErrorResponse("AUTHENTICATION_ERROR", "Email or password is incorrect.", HttpStatus.UNAUTHORIZED));
    }

    // Handle product action limit errors
    @ExceptionHandler(ProductActionLimitException.class)
    public ResponseEntity<Map<String, Object>> handleActionLimit(ProductActionLimitException e) {
        Map<String, Object> response = createErrorResponse("LIMIT_EXCEEDED", e.getMessage(), HttpStatus.TOO_MANY_REQUESTS);
        response.put("limitType", e.getLimitType());
        response.put("currentCount", e.getCurrentCount());
        response.put("maxAllowed", e.getMaxAllowed());
        response.put("action", e.getAction());
        
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(response);
    }

    // Handle JSON parsing errors
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleJsonValidation(MethodArgumentNotValidException e) {
        return ResponseEntity.badRequest()
            .body(createErrorResponse("JSON_VALIDATION_ERROR", "Invalid request format", HttpStatus.BAD_REQUEST));
    }

    // Handle unexpected errors
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneral(Exception e) {
        // Log the error for debugging
        e.printStackTrace();
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(createErrorResponse("INTERNAL_SERVER_ERROR", 
                "An unexpected error occurred: " + e.getMessage(), 
                HttpStatus.INTERNAL_SERVER_ERROR));
    }

    // Handle unmet item in cart
    @ExceptionHandler(CartAvailabilityException.class)
    public ResponseEntity<Map<String, Object>> handleCartAvailabilityException(CartAvailabilityException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("error", "Cart validation failed");
        body.put("messages", ex.getErrors()); 

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DeliveryFormValidationException.class)
    public ResponseEntity<Map<String, Object>> handleDeliveryFormValidationException(DeliveryFormValidationException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Delivery form validation failed");
        response.put("errors", ex.getErrors());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(OrderOperationException.class)
    public ResponseEntity<Map<String, Object>> handleOrderOperationException(OrderOperationException e) {
        return ResponseEntity.badRequest()
                .body(createErrorResponse("ORDER_OPERATION_ERROR", e.getMessage(), HttpStatus.BAD_REQUEST));
    }

    @ExceptionHandler(BadSessionDataException.class)
    public ResponseEntity<Map<String, Object>> handleBadSessionData(BadSessionDataException e) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", "SESSION_DATA_MISSING");
        response.put("message", "Required session data is missing");
        response.put("missingFields", e.getMissingFields());
        response.put("timestamp", LocalDateTime.now());
        response.put("status", HttpStatus.BAD_REQUEST.value());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RushOrderException.class)
    public ResponseEntity<Map<String, Object>> handleRushOrderException(RushOrderException e) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", "RUSH_ORDER_ERROR");
        response.put("errorCode", e.getErrorCode());
        response.put("message", e.getMessage());
        response.put("expectedDateTime", e.getExpectedDateTime());
        response.put("timestamp", LocalDateTime.now());
        response.put("status", HttpStatus.BAD_REQUEST.value());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}