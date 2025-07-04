package com.hustict.aims.dto;

import com.hustict.aims.dto.invoice.InvoiceDTO;

public class NormalOrderResult {
    private Long orderId;
    private InvoiceDTO invoice;

    public NormalOrderResult(Long orderId, InvoiceDTO invoice) {
        this.orderId = orderId;
        this.invoice = invoice;
    }

    public Long getOrderId() {
        return orderId;
    }

    public InvoiceDTO getInvoice() {
        return invoice;
    }
}
