package com.hustict.aims.repository;

import com.hustict.aims.model.user.ProductChecklog;
import com.hustict.aims.model.user.ActionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;
import java.util.List;

public interface ProductChecklogRepository extends JpaRepository<ProductChecklog, Long> {
    @Query("SELECT COUNT(p) FROM ProductChecklog p WHERE p.userId = :userId AND p.productId = :productId AND p.actionType = :actionType AND DATE(p.date) = :date")
    int countProductActions(@Param("userId") Long userId, @Param("productId") Long productId, @Param("actionType") ActionType actionType, @Param("date") LocalDate date);

    @Query("SELECT COUNT(p) FROM ProductChecklog p WHERE p.userId = :userId AND p.actionType IN :actionTypes AND DATE(p.date) = :date")
    int countUserActions(@Param("userId") Long userId, @Param("actionTypes") List<ActionType> actionTypes, @Param("date") LocalDate date);
} 