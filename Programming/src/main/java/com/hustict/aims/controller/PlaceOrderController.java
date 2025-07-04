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
import com.hustict.aims.dto.payment.PlaceOrderRequestDTO;
import com.hustict.aims.dto.cart.CartRequestDTO;
import com.hustict.aims.service.payment.SavePaymentTransaction;
import com.hustict.aims.service.placeOrder.DeliveryFormValidator;
import com.hustict.aims.service.placeOrder.PaymentHandlerService;
import com.hustict.aims.service.placeOrder.NormalOrderService;

import com.hustict.aims.service.placeOrder.SaveTempOrder;
import com.hustict.aims.service.placeOrder.request.HandleRequestService;
import com.hustict.aims.service.placeOrder.CartCleanupService;
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

    @Autowired
    private SessionManagementServiceImpl sessionManagementService;

   
   @PostMapping("/request")
    public ResponseEntity<String> requestToPlaceOrder(@RequestBody CartRequestDTO cart, HttpSession session) {
        handleRequestService.requestToPlaceOrder(cart,session.getId()); // Tight coupling
        sessionManagementService.saveCart(session, cart);
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
        invoice.setRushOrder(false);
        cart.setRushOrder(false);
        Long orderid = saveTempOrder.save(cart, deliveryForm, invoice,session);
        
        List<InvoiceDTO> invoiceList = new java.util.ArrayList<>();
        List<CartRequestDTO> cartList = new java.util.ArrayList<>();

        invoiceList.add(invoice);
        cartList.add(cart);

        session.setAttribute("invoiceList", invoiceList);
        session.setAttribute("cartList", cartList);
        
        
        System.out.println("=======START CART LIST =======");
        if (cartList != null) {
            cartList.forEach(cartdto -> System.out.println(cartdto.toString()));
        } 

        // In ra invoiceList
        System.out.println("=======START INVOICE LIST =======");
        if (invoiceList != null) {
            invoiceList.forEach(invoicedto -> System.out.println(invoicedto.toString()));
        } 
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
        } 
        paymentHandlerService.handlePaymentSuccess(paymentTransaction, orderId);
        return ResponseEntity.ok("Order is successfully created (Mock Test)");
    }

    @PostMapping("/confirm-order")
    public ResponseEntity<String> cartCleanUp(HttpSession session, @RequestBody ConfirmOrderRequestDTO confirmOrderRequestDTO) {
        
        logger.info("Sucess or not?:" + confirmOrderRequestDTO.isSuccess());
        if (confirmOrderRequestDTO.isSuccess()) {
            logger.info("Remove cart for order" + confirmOrderRequestDTO.getOrderId());

            cartCleanupService.removePurchasedItems(session, confirmOrderRequestDTO.getOrderId());
            logger.info("Confirm reservation for order" + confirmOrderRequestDTO.getOrderId());

            reservationService.confirmReservation(session,confirmOrderRequestDTO.getOrderId());
          
        } else//  return ResponseEntity.ok("Nothing to remove!");

        logger.info("Order is not sucess " + confirmOrderRequestDTO.getOrderId());

        return ResponseEntity.ok("Cart is cleaned and reservation is cleaned for");
    }
}