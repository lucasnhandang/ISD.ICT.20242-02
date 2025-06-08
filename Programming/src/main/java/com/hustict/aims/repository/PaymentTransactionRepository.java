package com.hustict.aims.repository;

import com.hustict.aims.model.payment.PaymentTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentTransactionRepository extends JpaRepository<PaymentTransaction, Long> {
} 