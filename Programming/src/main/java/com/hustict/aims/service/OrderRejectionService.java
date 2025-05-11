package com.hustict.aims.service;
import com.hustict.aims.model.Order;

/*
The OrderRejectionService has functional cohesion.
Its responsibility is to validate and update an order’s rejection status and reason.
It should only change if the rejection rules or status logic change.
 */
public class OrderRejectionService {
    public void reject(Order order, String reason) {
        if (reason == null || reason.trim().isEmpty()) {
            throw new IllegalArgumentException("Lý do từ chối không được để trống.");
        }

        if (!"pending".equals(order.getOrderStatus())) {
            throw new IllegalStateException("Trạng thái đơn hàng không đạt yêu cầu để từ chối.");
        }

        order.setOrderStatus("rejectedNotRefunded");
        order.setRejectionReason(reason);
    }
}
