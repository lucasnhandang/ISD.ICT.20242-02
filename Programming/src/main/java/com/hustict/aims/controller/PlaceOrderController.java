package com.hustict.aims.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Qualifier;

import jakarta.persistence.criteria.CriteriaBuilder.In;
import jakarta.servlet.http.HttpSession;

import com.hustict.aims.dto.deliveryForm.DeliveryFormDTO;
import com.hustict.aims.dto.invoice.InvoiceDTO;
import com.hustict.aims.dto.cart.CartRequestDTO;
import com.hustict.aims.service.placeOrder.DeliveryFormValidator;
import com.hustict.aims.service.placeOrder.HandleRequestService;
import com.hustict.aims.service.placeOrder.PaymentHandlerService;
import com.hustict.aims.service.placeOrder.NormalOrderService;


@RestController
@RequestMapping("/api/v1/place-order")
public class PlaceOrderController {

    private final HandleRequestService handleRequestService;
    private final DeliveryFormValidator deliveryFormValidator;
    private final NormalOrderService normalOrderService;
    private final PaymentHandlerService paymentHandlerService;
    public PlaceOrderController(HandleRequestService handleRequestService,
                                @Qualifier("deliveryFormValidatorImpl") DeliveryFormValidator deliveryFormValidator,
                                NormalOrderService normalOrderService,
                                PaymentHandlerService paymentHandlerService) {
        this.handleRequestService = handleRequestService;
        this.deliveryFormValidator = deliveryFormValidator;
        this.normalOrderService = normalOrderService;
        this.paymentHandlerService = paymentHandlerService;
    }

   @PostMapping("/request")
    public ResponseEntity<String> requestToPlaceOrder(@RequestBody CartRequestDTO cart, HttpSession session) {
        handleRequestService.requestToPlaceOrder(cart,session.getId());
        session.setAttribute("cart", cart);
        return ResponseEntity.ok("Order request successfully submitted");
    }

    @PostMapping("/submit-form")
    public ResponseEntity<String> submitDeliveryForm(@RequestBody DeliveryFormDTO form, HttpSession session) {
        deliveryFormValidator.validate(form, session.getId());
        session.setAttribute("deliveryForm", form);
        return ResponseEntity.ok("Delivery form successfully submitted");
    }

    @PostMapping("/normal-order")
    public ResponseEntity<String> handleNormalOrder(HttpSession session) {
        DeliveryFormDTO deliveryForm = (DeliveryFormDTO) session.getAttribute("deliveryForm");
        CartRequestDTO cart = (CartRequestDTO) session.getAttribute("cart"); 
        if (deliveryForm == null) {
            return ResponseEntity.badRequest().body("Delivery form not found in session.");
        }
        if (cart == null) {
            return ResponseEntity.badRequest().body("Cart not found in session.");
        }
        InvoiceDTO invoice = normalOrderService.handleNormalOrder(deliveryForm,cart);
        session.setAttribute("invoice", invoice);
        return ResponseEntity.ok("Normal order handled successfully with delivery info and shipping fee.");
    }

    @PostMapping("/handle-payment")
    public ResponseEntity<String> handlePaymentSuccess(HttpSession session) {
        DeliveryFormDTO deliveryForm = (DeliveryFormDTO) session.getAttribute("deliveryForm");
        CartRequestDTO cart = (CartRequestDTO) session.getAttribute("cart"); 
        InvoiceDTO invoice = (InvoiceDTO) session.getAttribute("invoice");

        paymentHandlerService.handlePaymentSuccess();
        return ResponseEntity.ok("Order is successfully created");
    }

}
