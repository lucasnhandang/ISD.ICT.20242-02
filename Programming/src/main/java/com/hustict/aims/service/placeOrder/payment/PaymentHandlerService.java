package com.hustict.aims.service.placeOrder.payment;

import com.hustict.aims.dto.payment.AfterPaymentDTO;

public interface PaymentHandlerService {
    public void handlePaymentSuccess(AfterPaymentDTO afterPaymentDTO);
}
