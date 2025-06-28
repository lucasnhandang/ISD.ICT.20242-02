package com.hustict.aims.service.placeOrder;

import com.hustict.aims.dto.order.OrderInformationDTO;
import com.hustict.aims.service.email.EmailSenderFactory;
import com.hustict.aims.service.reservation.ReservationService;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

@Service
public class PaymentHandlerService {

    private final CartCleanupService cartCleanupService;
    private final SaveOrderService saveOrderService;
    private final EmailSenderFactory emailSenderFactory;
    private final ReservationService reservationService;

    public PaymentHandlerService(
        CartCleanupService cartCleanupService,
        SaveOrderService saveOrderService,
        EmailSenderFactory emailSenderFactory,
        ReservationService reservationService
    ) {
        this.cartCleanupService = cartCleanupService;
        this.saveOrderService   = saveOrderService;
        this.emailSenderFactory = emailSenderFactory;
        this.reservationService = reservationService;
    }

    public void handlePaymentSuccess(HttpSession session) {
        OrderInformationDTO savedOrderInfo = saveOrderService.saveAll(session);
        
        reservationService.confirmReservation(session);
        
        session.setAttribute("orderInformation", savedOrderInfo);
        //session.setAttribute("deliveryForm", session.getAttribute("deliveryForm"));
        
        cartCleanupService.removePurchasedItems(session);

        emailSenderFactory.process("orderSuccess", session);
    }
}
