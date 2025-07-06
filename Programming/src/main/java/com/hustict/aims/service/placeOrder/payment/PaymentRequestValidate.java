package com.hustict.aims.service.placeOrder.payment;

import com.hustict.aims.dto.payment.AfterPaymentDTO;

public interface PaymentRequestValidate {
    public void validate(AfterPaymentDTO afterPaymentDTO);

}
