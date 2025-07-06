package com.hustict.aims.service.placeOrder.payment;

import org.springframework.stereotype.Service;

import com.hustict.aims.dto.payment.AfterPaymentDTO;
import com.hustict.aims.dto.payment.PaymentTransactionDTO;

@Service
public class PaymentRequestValidateImpl implements PaymentRequestValidate {
    public void validate(AfterPaymentDTO afterPaymentDTO){
        PaymentTransactionDTO paymentTransaction = afterPaymentDTO.getPaymentTransaction();
        Long orderId = afterPaymentDTO.getOrderId();

        if (paymentTransaction == null) {
            throw new IllegalArgumentException("PaymentTransactionDTO must not be null");
        } else if (orderId == null) {
            throw new IllegalArgumentException("orderId must not be null");
        } 
    }
}
