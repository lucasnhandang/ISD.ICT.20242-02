package com.hustict.aims.dto.invoice;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import lombok.Data;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceDTO {
    private Long id;
    private int productPriceExVAT;
    private int productPriceIncVAT;
    private int shippingFee;
    private Long paymentTransactionId;
    private int totalAmount;
}