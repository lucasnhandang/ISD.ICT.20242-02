package com.hustict.aims.service.refund;

import org.springframework.stereotype.Service;

@Service("PaypalRefundService") 
public class PaypalRefundService implements RefundService {

    @Override
    public void processRefund(Long orderId) {
        System.out.println("Processing Paypal refund for order ID: " + orderId);
    }
}
