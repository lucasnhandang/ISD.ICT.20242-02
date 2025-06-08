package com.hustict.aims.exception;

public class ProductActionLimitException extends RuntimeException {
    private final String limitType;
    private final int currentCount;
    private final int maxAllowed;
    private final String action;

    public ProductActionLimitException(String limitType, int currentCount, int maxAllowed, String action) {
        super(String.format("%s limit exceeded for %s. Current: %d, Max allowed: %d", 
            limitType, action, currentCount, maxAllowed));
        this.limitType = limitType;
        this.currentCount = currentCount;
        this.maxAllowed = maxAllowed;
        this.action = action;
    }

    public String getLimitType() { return limitType; }
    public int getCurrentCount() { return currentCount; }
    public int getMaxAllowed() { return maxAllowed; }
    public String getAction() { return action; }
} 