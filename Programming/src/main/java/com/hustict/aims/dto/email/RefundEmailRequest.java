package com.hustict.aims.dto.email;

import com.hustict.aims.dto.payment.PaymentTransactionDTO;


import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class RefundEmailRequest extends BaseEmailRequest {
    private PaymentTransactionDTO refundPayment;

}
