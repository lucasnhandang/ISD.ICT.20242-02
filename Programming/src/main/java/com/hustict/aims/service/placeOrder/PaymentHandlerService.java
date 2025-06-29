package com.hustict.aims.service.placeOrder;

import com.hustict.aims.dto.order.OrderDTO;
import com.hustict.aims.dto.order.OrderInformationDTO;
import com.hustict.aims.dto.payment.PaymentTransactionDTO;
import com.hustict.aims.service.email.EmailSenderFactory;
import com.hustict.aims.service.payment.SavePaymentTransaction;
import com.hustict.aims.service.reservation.ReservationService;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentHandlerService {

    @Autowired
    private CartCleanupService cartCleanupService;

    @Autowired
    private SaveOrderService saveOrderService;

    @Autowired
    private EmailSenderFactory emailSenderFactory;

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private SavePaymentTransaction savePaymentTransaction;


    public void handlePaymentSuccess(PaymentTransactionDTO paymentTransaction, Long orderid) {

        //reservationService.confirmReservation(session);
        OrderDTO orderinfo = savePaymentTransaction.save(paymentTransaction, orderid);
        
        //cartCleanupService.removePurchasedItems(session);

        emailSenderFactory.process("orderSuccess", orderinfo);
    }
}
