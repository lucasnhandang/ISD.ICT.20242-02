package com.hustict.aims.repository;

import com.hustict.aims.model.payment.PaymentTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface PaymentTransactionRepository extends JpaRepository<PaymentTransaction, Long> {

    @Query("SELECT p FROM PaymentTransaction p WHERE p.bankTransactionId = :txnId")
    Optional<PaymentTransaction> findByBankTransactionId(@Param("txnId") String txnId);

    @Modifying
    @Transactional
    @Query("UPDATE PaymentTransaction p SET p.paymentAmount = :amount WHERE p.id = :id")
    void updatePaymentAmountById(@Param("id") Long id, @Param("amount") int amount);
}
