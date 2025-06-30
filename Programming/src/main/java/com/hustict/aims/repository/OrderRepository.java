package com.hustict.aims.repository;

import com.hustict.aims.model.order.Order;
import com.hustict.aims.model.order.OrderStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("SELECT SUM(oi.quantity) FROM Order o JOIN o.orderItems oi WHERE o.id = :id")
    int getQuantityById(@Param("id") Long id);

    @Modifying
    @Transactional
    @Query("UPDATE Order o SET o.orderStatus = 'APPROVED' WHERE o.id = :id")
    void approveOrderById(@Param("id") Long id);

    @Modifying
    @Transactional
    @Query("UPDATE Order o SET o.orderStatus = 'CANCELLED_PENDING' WHERE o.id = :id")
    void cancelOrderById(@Param("id") Long id);

    @Modifying
    @Transactional
    @Query("UPDATE Order o SET o.orderStatus = 'REJECTED_PENDING' WHERE o.id = :id")
    void rejectOrderById(@Param("id") Long id);

    @Modifying
    @Transactional
    @Query("UPDATE Order o SET o.orderStatus = 'CANCELLED_REFUNDED' WHERE o.id = :id")
    void updateCancelRefundedOrderById(@Param("id") Long id);

    @Modifying
    @Transactional
    @Query("UPDATE Order o SET o.orderStatus = 'REJECTED_REFUNDED' WHERE o.id = :id")
    void updateRejectRefundedOrderById(@Param("id") Long id);

    List<Order> findByOrderStatus(OrderStatus status, Pageable pageable);
    
    long countByOrderStatus(OrderStatus status);
}
