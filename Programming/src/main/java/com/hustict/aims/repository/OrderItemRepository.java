package com.hustict.aims.repository;

import com.hustict.aims.model.order.OrderItem;
import com.hustict.aims.model.order.OrderItemKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, OrderItemKey> {
    List<OrderItem> findByOrderId(Long orderId);
}
