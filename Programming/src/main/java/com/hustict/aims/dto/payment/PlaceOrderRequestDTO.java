package com.hustict.aims.dto.payment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlaceOrderRequestDTO {
    private PaymentTransactionDTO paymentTransaction;
    private Long orderId;

    // Getters and setters
}