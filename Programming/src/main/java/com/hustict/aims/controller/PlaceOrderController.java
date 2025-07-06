package com.hustict.aims.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import jakarta.servlet.http.HttpSession;

import com.hustict.aims.dto.deliveryForm.DeliveryFormDTO;
import com.hustict.aims.dto.invoice.InvoiceDTO;
import com.hustict.aims.dto.payment.PaymentTransactionDTO;
import com.hustict.aims.dto.payment.AfterPaymentDTO;
import com.hustict.aims.dto.NormalOrderResult;
import com.hustict.aims.dto.cart.CartRequestDTO;
import com.hustict.aims.service.payment.SavePaymentTransaction;
import com.hustict.aims.service.placeOrder.DeliveryFormValidator;
import com.hustict.aims.service.placeOrder.NormalOrderHandlerService;
import com.hustict.aims.service.placeOrder.confirm.ConfirmOrderService;
import com.hustict.aims.service.placeOrder.normal.NormalOrderService;
import com.hustict.aims.service.placeOrder.payment.PaymentHandlerService;
import com.hustict.aims.service.placeOrder.payment.PaymentRequestValidate;
import com.hustict.aims.service.placeOrder.request.HandleRequestService;
import com.hustict.aims.service.placeOrder.confirm.ConfirmOrderServiceImpl;
import com.hustict.aims.service.placeOrder.payment.PaymentRequestValidateImpl;
import com.hustict.aims.dto.order.ConfirmOrderRequestDTO;

import com.hustict.aims.service.reservation.ReservationService;
import com.hustict.aims.service.session.SessionManagementServiceImpl;
import com.hustict.aims.service.sessionValidator.SessionValidatorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/place-order")
public class PlaceOrderController {

    private static final Logger logger = LoggerFactory.getLogger(SavePaymentTransaction.class);


    @Autowired
    private HandleRequestService handleRequestService;

    @Autowired
    @Qualifier("deliveryFormValidatorImpl")
    private DeliveryFormValidator deliveryFormValidator;


    @Autowired
    private PaymentHandlerService paymentHandlerService;

    @Autowired
    private SessionValidatorService sessionValidatorService;

    @Lazy
    @Autowired
    private ReservationService reservationService;

    @Autowired
    private NormalOrderService normalService;

    @Autowired
    private SessionManagementServiceImpl sessionManagementService;

    @Autowired
    private PaymentRequestValidateImpl paymentRequestValidate;
    
    @Autowired
    private NormalOrderHandlerService normalOrderHandlerService;

    @Autowired
    private ConfirmOrderServiceImpl confirmOrderService;
   
   @PostMapping("/request")
    public ResponseEntity<String> requestToPlaceOrder(@RequestBody CartRequestDTO cart, HttpSession session) {
        handleRequestService.requestToPlaceOrder(cart,session.getId()); 
        sessionManagementService.saveCart(session, cart); 
        return ResponseEntity.ok("Order request successfully submitted");
    }

    @PostMapping("/submit-form")
    public ResponseEntity<String> submitDeliveryForm(@RequestBody DeliveryFormDTO form, HttpSession session) {
        sessionValidatorService.validateCartRequestedPresent(session);
        
        deliveryFormValidator.validate(form, session.getId());
        
        sessionManagementService.saveDeliveryForm(session, form);
        return ResponseEntity.ok("Delivery form successfully submitted");
    }

    @PostMapping("/normal-order")
    public ResponseEntity<Map<String, Object>> handleNormalOrder(HttpSession session, @RequestBody CartRequestDTO cart) {
        // // 1. Validate session
        // sessionValidatorService.validateDeliveryAndCartForCheckout(session);
        // // 2. Create invoice and save order
        // DeliveryFormDTO deliveryForm = (DeliveryFormDTO) sessionManagementService.getDeliveryForm(session);
        // NormalOrderResult results = normalService.processNormalOrder(cart, deliveryForm);

        // // 3. Manage session memory
        // InvoiceDTO invoice = results.getInvoice();
        // Long orderid = results.getOrderId();
        // sessionManagementService.addToCartList(session, cart);
        // sessionManagementService.addToInvoiceList(session, invoice);

        // // 4. Create Response 
        // Map<String, Object> response = new HashMap<>();
        // response.put("cart", cart);
        // response.put("invoice", invoice);
        // response.put("deliveryForm", deliveryForm);
        // response.put("orderid", orderid);

        // return ResponseEntity.ok(response);
        return normalOrderHandlerService.handleNormalOrder(session, cart);

    }

    @PostMapping("/handle-payment")
    public ResponseEntity<String> handlePayment(@RequestBody AfterPaymentDTO afterPaymentDTO) {            
        paymentRequestValidate.validate(afterPaymentDTO);
        paymentHandlerService.handlePaymentSuccess(afterPaymentDTO);
        return ResponseEntity.ok("Order is successfully created (Mock Test)");
    }

    @PostMapping("/confirm-order")
    public ResponseEntity<String> cartCleanUp(HttpSession session, @RequestBody ConfirmOrderRequestDTO confirmOrderRequestDTO) {
        confirmOrderService.confirmOrder(session, confirmOrderRequestDTO);
        return ResponseEntity.ok("Cart is cleaned and reservation is cleaned for");
    }
}