package com.hustict.aims.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;

import com.hustict.aims.dto.deliveryForm.DeliveryFormDTO;
import com.hustict.aims.dto.cart.CartRequestDTO;
import com.hustict.aims.service.placeOrder.DeliveryFormService;
import com.hustict.aims.service.placeOrder.HandleRequestService;
import com.hustict.aims.service.placeOrder.PaymentHandlerService;   

// import com.hustict.aims.dto.CartDTO;                     
// import com.hustict.aims.dto.DeliveryFormDTO;             

@RestController
@RequestMapping("/api/v1/place-order")
public class PlaceOrderController {

    private final HandleRequestService handleRequestService;
    private final DeliveryFormService deliveryFormService;
    private final PaymentHandlerService paymentHandlerService;

    public PlaceOrderController(HandleRequestService handleRequestService,
                                DeliveryFormService deliveryFormService,
                                PaymentHandlerService paymentHandlerService) {
        this.handleRequestService = handleRequestService;
        this.deliveryFormService = deliveryFormService;
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
        deliveryFormService.submitDeliveryForm(form, session.getId());
        session.setAttribute("deliveryForm", form);

        return ResponseEntity.ok("Delivery form successfully submitted");
    }

    @PostMapping("/handle-payment")
    public ResponseEntity<String> handlePaymentSuccess() {
        paymentHandlerService.handlePaymentSuccess();
        return ResponseEntity.ok("Order is successfully created");
    }


}
