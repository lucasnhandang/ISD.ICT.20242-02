package com.hustict.aims.repository;

import com.hustict.aims.model.invoice.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
} 