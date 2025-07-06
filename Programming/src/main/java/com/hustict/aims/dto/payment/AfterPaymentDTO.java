package com.hustict.aims.dto.payment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AfterPaymentDTO {
    private PaymentTransactionDTO paymentTransaction;
    private Long orderId;
    private boolean sucess;
}