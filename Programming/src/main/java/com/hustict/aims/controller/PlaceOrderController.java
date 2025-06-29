package com.hustict.aims.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import jakarta.servlet.http.HttpSession;

import com.hustict.aims.dto.deliveryForm.DeliveryFormDTO;
import com.hustict.aims.dto.invoice.InvoiceDTO;
import com.hustict.aims.dto.order.OrderDTO;
import com.hustict.aims.dto.payment.PaymentTransactionDTO;
import com.hustict.aims.dto.payment.PlaceOrderRequestDTO;
import com.hustict.aims.dto.cart.CartItemRequestDTO;
import com.hustict.aims.dto.cart.CartRequestDTO;
import com.hustict.aims.service.payment.SavePaymentTransaction;
import com.hustict.aims.service.placeOrder.DeliveryFormValidator;
import com.hustict.aims.service.placeOrder.HandleRequestService;
import com.hustict.aims.service.placeOrder.PaymentHandlerService;
import com.hustict.aims.service.placeOrder.NormalOrderService;
import com.hustict.aims.service.placeOrder.SaveTempOrder;
import com.hustict.aims.service.placeOrder.CartCleanupService;


import com.hustict.aims.service.reservation.ReservationService;
import com.hustict.aims.service.sessionValidator.SessionValidatorService;

import java.util.HashMap;
import java.util.Map;
        
@RestController
@RequestMapping("/api/v1/place-order")
public class PlaceOrderController {

    @Autowired
    private HandleRequestService handleRequestService;

    @Autowired
    @Qualifier("deliveryFormValidatorImpl")
    private DeliveryFormValidator deliveryFormValidator;

    @Autowired
    private NormalOrderService normalOrderService;

    @Autowired
    private PaymentHandlerService paymentHandlerService;

    @Autowired
    private SessionValidatorService sessionValidatorService;

    @Lazy
    @Autowired
    private ReservationService reservationService;

    @Autowired
    private SaveTempOrder saveTempOrder;

    @Autowired
    private CartCleanupService cartCleanupService;


   
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

    @PostMapping("/cancel")
    public ResponseEntity<String> cancelOrder(HttpSession session) {
        session.removeAttribute("cartRequested");
        session.removeAttribute("deliveryForm");
        session.removeAttribute("invoice");
        session.removeAttribute("paymentTransaction");

        reservationService.releaseReservation(session);
        return ResponseEntity.ok("Order has been canceled.");
    }

    @PostMapping("/normal-order")
    public ResponseEntity<Map<String, Object>> handleNormalOrder(HttpSession session, @RequestBody CartRequestDTO cart) {
        // Validate session
        sessionValidatorService.validateDeliveryAndCartForCheckout(session);

        DeliveryFormDTO deliveryForm = (DeliveryFormDTO) session.getAttribute("deliveryForm");
        InvoiceDTO invoice = normalOrderService.handleNormalOrder(deliveryForm, cart);

        cart.setRushOrder(false);
        Long orderid = saveTempOrder.save(cart, deliveryForm, invoice);


        Map<String, Object> response = new HashMap<>();
        response.put("cart", cart);
        response.put("invoice", invoice);
        response.put("deliveryForm", deliveryForm);
        response.put("orderid", orderid);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/handle-payment")
    public ResponseEntity<String> handlePayment(@RequestBody PlaceOrderRequestDTO placeOrderRequestDTO) {     
        // sessionValidatorService.validateAfterPayment(session);
       
        PaymentTransactionDTO paymentTransaction = placeOrderRequestDTO.getPaymentTransaction();
        Long orderId = placeOrderRequestDTO.getOrderId();

        if (paymentTransaction == null) {
            throw new IllegalArgumentException("PaymentTransactionDTO must not be null");
        } else if (orderId == null) {
            throw new IllegalArgumentException("orderId must not be null");
        } else {
            System.out.println("PaymentTransactionDTO and orderId are valid");
        }
        paymentHandlerService.handlePaymentSuccess(paymentTransaction, orderId);
        return ResponseEntity.ok("Order is successfully created (Mock Test)");
    }

    @PostMapping("/confirm-order")
    public ResponseEntity<String> cartCleanUp(HttpSession session, String TransactionStatus, Long orderId) {
        
        
        return ResponseEntity.ok("Cart is cleaned and reservation is cleaned for");
    }
}