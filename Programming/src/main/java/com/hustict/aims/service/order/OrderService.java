package com.hustict.aims.service.order;

public interface OrderService {
    void approveOrder(Long orderId);
    void rejectOrder(Long orderId,String reason);
} 