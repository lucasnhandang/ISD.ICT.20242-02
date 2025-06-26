package com.hustict.aims.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Qualifier;

import jakarta.servlet.http.HttpSession;

import com.hustict.aims.dto.deliveryForm.DeliveryFormDTO;
import com.hustict.aims.dto.invoice.InvoiceDTO;
import com.hustict.aims.dto.payment.PaymentTransactionDTO;
import com.hustict.aims.dto.cart.CartRequestDTO;
import com.hustict.aims.service.placeOrder.DeliveryFormValidator;
import com.hustict.aims.service.placeOrder.HandleRequestService;
import com.hustict.aims.service.placeOrder.PaymentHandlerService;
import com.hustict.aims.service.sessionValidator.SessionValidatorService;
import com.hustict.aims.service.placeOrder.NormalOrderService;


@RestController
@RequestMapping("/api/v1/place-order")
public class PlaceOrderController {

    private final HandleRequestService handleRequestService;
    private final DeliveryFormValidator deliveryFormValidator;
    private final NormalOrderService normalOrderService;
    private final PaymentHandlerService paymentHandlerService;
    private final SessionValidatorService sessionValidatorService;

    public PlaceOrderController(HandleRequestService handleRequestService,
                            @Qualifier("deliveryFormValidatorImpl") DeliveryFormValidator deliveryFormValidator,
                            NormalOrderService normalOrderService,
                            PaymentHandlerService paymentHandlerService,
                            SessionValidatorService sessionValidatorService) {
        this.handleRequestService = handleRequestService;
        this.deliveryFormValidator = deliveryFormValidator;
        this.normalOrderService = normalOrderService;
        this.paymentHandlerService = paymentHandlerService;
        this.sessionValidatorService = sessionValidatorService;
    }

   @PostMapping("/request")
    public ResponseEntity<String> requestToPlaceOrder(@RequestBody CartRequestDTO cart, HttpSession session) {
        handleRequestService.requestToPlaceOrder(cart,session.getId());
        session.setAttribute("cartRequested", cart);
        return ResponseEntity.ok("Order request successfully submitted");
    }

    @PostMapping("/submit-form")
    public ResponseEntity<String> submitDeliveryForm(@RequestBody DeliveryFormDTO form, HttpSession session) {
        sessionValidatorService.validateCartRequestedPresent(session);
        deliveryFormValidator.validate(form, session.getId());
        session.setAttribute("deliveryForm", form);
        return ResponseEntity.ok("Delivery form successfully submitted");
    }

    @PostMapping("/normal-order")
    public ResponseEntity<String> handleNormalOrder(HttpSession session) {
        sessionValidatorService.validateDeliveryAndCartForCheckout(session);
        DeliveryFormDTO deliveryForm = (DeliveryFormDTO) session.getAttribute("deliveryForm");
        CartRequestDTO cart = (CartRequestDTO) session.getAttribute("cartRequested");

        InvoiceDTO invoice = normalOrderService.handleNormalOrder(deliveryForm, cart);
        session.setAttribute("invoice", invoice);
        session.setAttribute("invoice", invoice);
        return ResponseEntity.ok("Normal order handled successfully with delivery info and shipping fee.");
    }

  @PostMapping("/handle-payment")
    public ResponseEntity<String> handlePaymentSuccess(HttpSession session) {
        PaymentTransactionDTO paymentTransaction = (PaymentTransactionDTO) session.getAttribute("paymentTransaction");

        if (paymentTransaction == null) {
            paymentTransaction = new PaymentTransactionDTO();
            paymentTransaction.setBankTransactionId("FAKEBANK-" + System.currentTimeMillis());
            paymentTransaction.setContent("Mock payment transaction for test user");
            paymentTransaction.setPaymentTime(LocalDateTime.now());
            paymentTransaction.setPaymentAmount(999_000); // Số tiền giả định
            paymentTransaction.setCardType("TEST_CARD");
            paymentTransaction.setCurrency("VND");

            session.setAttribute("paymentTransaction", paymentTransaction);
        }
        sessionValidatorService.validateAfterPayment(session);
        
        paymentHandlerService.handlePaymentSuccess(session);
        return ResponseEntity.ok("Order is successfully created");
    }
}
