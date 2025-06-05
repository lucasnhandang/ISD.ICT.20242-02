package com.hustict.aims.repository;

import com.hustict.aims.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("SELECT SUM(p.totalQuantity) FROM Order o JOIN o.productList p WHERE o.id = :id")
    int getQuantityById(@Param("id") Long id);

    @Modifying
    @Query("UPDATE Order o SET o.status = 'CANCELLED_PENDING' WHERE o.id = :id")
    void cancelOrderById(@Param("id") Long id);

    @Modifying
    @Query("UPDATE Order o SET o.status = 'REJECTED_PENDING' WHERE o.id = :id")
    void rejectOrderById(@Param("id") Long id);

    @Modifying
    @Query("UPDATE Order o SET o.status = 'CANCELLED_REFUNDED' WHERE o.id = :id")
    void updateRefundedOrderById(@Param("id") Long id);
} 