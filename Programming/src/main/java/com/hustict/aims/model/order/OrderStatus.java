package com.hustict.aims.model.order;

public enum OrderStatus {
    NEW,
    PENDING,
    APPROVED,
    REJECTED_PENDING,
    REJECTED_REFUNDED,
    CANCELLED_PENDING,
    CANCELLED_REFUNDED
}