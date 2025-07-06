package com.hustict.aims.service.order;

import com.hustict.aims.exception.OrderOperationException;
import com.hustict.aims.model.order.Order;
import com.hustict.aims.model.order.OrderStatus;
import com.hustict.aims.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderCancellationServiceImpl implements OrderCancellationService {

    @Autowired
    private OrderRepository orderRepository;

    @Override
    @Transactional
    public void cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderOperationException("Order not found with id: " + orderId));

        if (order.getOrderStatus() != OrderStatus.PENDING) {
            throw new OrderOperationException("Only PENDING orders can be cancelled. Current status: " + order.getOrderStatus());
        }

        orderRepository.cancelOrderById(orderId);
    }
} 