package com.hustict.aims.repository;

import com.hustict.aims.model.payment.RefundTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefundTransactionRepository extends JpaRepository<RefundTransaction, Long> {
} 