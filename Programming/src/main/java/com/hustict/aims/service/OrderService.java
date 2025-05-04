package com.hustict.aims.service;

import com.hustict.aims.model.Order;

public class OrderService {

    public void updateRejectedStatusAndReason(Order order, String reason) {
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
