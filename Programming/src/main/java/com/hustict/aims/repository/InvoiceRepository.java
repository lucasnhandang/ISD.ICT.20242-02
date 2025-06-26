package com.hustict.aims.repository;

import com.hustict.aims.model.invoice.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;


public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

    @Query("SELECT i.totalAmount FROM Invoice i WHERE i.id = :id")
    int getTotalAmountById(@Param("id") Long id);

    @Modifying
    @Transactional
    @Query("UPDATE Invoice i SET i.shippingFee = :fee WHERE i.id = :id")
    void updateShippingFeeById(@Param("id") Long id, @Param("fee") int fee);
}