package com.hustict.aims.dto.invoice;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class InvoiceDTO {
    private int totalPriceExVat;
    private int totalPriceIncludingVat;
    private int shippingFee;
    private int totalAmount;
}