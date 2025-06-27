package com.hustict.aims.service.invoice;

import com.hustict.aims.dto.invoice.InvoiceDTO;

public interface InvoiceCalculationService {
    InvoiceDTO calculateInvoice(int totalPriceExVat, int shippingFee);
}