package com.hustict.aims.service.placeOrder;

import com.hustict.aims.dto.order.OrderInformationDTO;
import com.hustict.aims.service.email.EmailSenderFactory;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentHandlerService {

    private final CartCleanupService cartCleanupService;
    private final SaveOrderService saveOrderService;
    private final EmailSenderFactory emailSenderFactory;

    @Autowired
    public PaymentHandlerService(
        CartCleanupService cartCleanupService,
        SaveOrderService saveOrderService,
        EmailSenderFactory emailSenderFactory
    ) {
        this.cartCleanupService = cartCleanupService;
        this.saveOrderService   = saveOrderService;
        this.emailSenderFactory = emailSenderFactory;
    }

    public void handlePaymentSuccess(HttpSession session) {
        OrderInformationDTO savedOrderInfo = saveOrderService.saveAll(session);
        session.setAttribute("orderInformation", savedOrderInfo);
        session.setAttribute("deliveryForm", session.getAttribute("deliveryForm"));

        cartCleanupService.removePurchasedItems(session);
        emailSenderFactory.process("orderSuccess", session);
    }
}
