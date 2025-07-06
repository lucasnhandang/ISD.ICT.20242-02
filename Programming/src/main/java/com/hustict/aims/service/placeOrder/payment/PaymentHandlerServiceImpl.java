package com.hustict.aims.service.placeOrder.payment;

import com.hustict.aims.dto.order.OrderDTO;
import com.hustict.aims.dto.payment.AfterPaymentDTO;
import com.hustict.aims.dto.payment.PaymentTransactionDTO;
import com.hustict.aims.service.email.EmailSenderFactory;
import com.hustict.aims.service.payment.SavePaymentTransaction;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentHandlerServiceImpl implements PaymentHandlerService {

    @Autowired
    private EmailSenderFactory emailSenderFactory;

    @Autowired
    private SavePaymentTransaction savePaymentTransaction;

    public void handlePaymentSuccess(AfterPaymentDTO afterPaymentDTO) {
        PaymentTransactionDTO paymentTransaction = afterPaymentDTO.getPaymentTransaction();
        Long orderId = afterPaymentDTO.getOrderId();
        OrderDTO orderinfo = savePaymentTransaction.save(paymentTransaction, orderId);
        if (afterPaymentDTO.isSucess()) emailSenderFactory.process("orderSuccess", orderinfo);
    }
}
