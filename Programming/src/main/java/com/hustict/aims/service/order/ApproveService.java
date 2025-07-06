package com.hustict.aims.service.order;

import org.springframework.transaction.annotation.Transactional;

import com.hustict.aims.exception.OrderOperationException;
import com.hustict.aims.model.order.Order;
import com.hustict.aims.model.order.OrderStatus;

public interface ApproveService {

    public void approveOrder(Long orderId);
}
