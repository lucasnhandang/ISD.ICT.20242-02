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
import com.hustict.aims.dto.payment.PaymentTransactionDTO;
import com.hustict.aims.dto.cart.CartItemRequestDTO;
import com.hustict.aims.dto.cart.CartRequestDTO;
import com.hustict.aims.service.placeOrder.DeliveryFormValidator;
import com.hustict.aims.service.placeOrder.HandleRequestService;
import com.hustict.aims.service.placeOrder.PaymentHandlerService;
import com.hustict.aims.service.reservation.ReservationService;
import com.hustict.aims.service.sessionValidator.SessionValidatorService;
import com.hustict.aims.service.placeOrder.NormalOrderService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
@RestController
@RequestMapping("/api/v1/place-order")
public class PlaceOrderController {

    private final HandleRequestService handleRequestService;
    private final DeliveryFormValidator deliveryFormValidator;
    private final NormalOrderService normalOrderService;
    private final PaymentHandlerService paymentHandlerService;
    private final SessionValidatorService sessionValidatorService;

    // xóa
    @Lazy
    @Autowired
    private ReservationService reservationService;  // Lazy initialization

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
    public ResponseEntity<Map<String, Object>> handleNormalOrder(HttpSession session) {
        sessionValidatorService.validateDeliveryAndCartForCheckout(session);
        DeliveryFormDTO deliveryForm = (DeliveryFormDTO) session.getAttribute("deliveryForm");
        //CartRequestDTO cart = (CartRequestDTO) session.getAttribute("cartRequested");
        CartRequestDTO cart = (CartRequestDTO) session.getAttribute("cartNormal");

        InvoiceDTO invoice = normalOrderService.handleNormalOrder(deliveryForm, cart);
        //session.setAttribute("invoice", invoice);
        session.setAttribute("invoiceNormal", invoice);

        Map<String, Object> response = new HashMap<>();
        response.put("cart", cart);
        response.put("invoice", invoice);
        response.put("deliveryForm", deliveryForm);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/handle-payment")
    public ResponseEntity<String> handlePaymentSuccess(HttpSession session) {

        // // 1. Fake Cart
        // CartRequestDTO cart = new CartRequestDTO();
        // cart.setCurrency("VND");

        // CartItemRequestDTO item = new CartItemRequestDTO();
        // item.setProductID(1L);
        // item.setQuantity(2);
        // item.setPrice(499500); // mỗi sản phẩm giá giả định

        // cart.setProductList(List.of(item));
        // session.setAttribute("cartRequested", cart);

        // // 2. Fake DeliveryForm
        // DeliveryFormDTO form = new DeliveryFormDTO();
        // form.setCustomerName("Nguyen Van Test");
        // form.setEmail("hobaothu202@gmail.com");
        // form.setPhoneNumber("0900000000");
        // form.setDeliveryAddress("123 Đường Test, Quận 1, TP.HCM");
        // form.setDeliveryProvince("TP.HCM");
        // form.setRushOrder(false);

        // session.setAttribute("deliveryForm", form);

        // // 3. Fake Invoice
        // InvoiceDTO invoice = new InvoiceDTO();
        // invoice.setProductPriceExVAT(999000);
        // invoice.setProductPriceIncVAT(999000);  // giả định chưa tính VAT
        // invoice.setShippingFee(0);
        // invoice.setTotalAmount(999000);

        // session.setAttribute("invoice", invoice);

        // 4. Fake Payment
        PaymentTransactionDTO paymentTransaction = new PaymentTransactionDTO();
        paymentTransaction.setBankTransactionId("FAKEBANK-" + System.currentTimeMillis());
        paymentTransaction.setContent("Mock payment transaction for test user");
        paymentTransaction.setPaymentTime(LocalDateTime.now());
        paymentTransaction.setPaymentAmount(999000);
        paymentTransaction.setCardType("TEST_CARD");
        paymentTransaction.setCurrency("VND");

        session.setAttribute("paymentTransaction", paymentTransaction);

        // String sessionId = session.getId();

        // reservationService.createReservation(cart, sessionId);
     
        sessionValidatorService.validateAfterPayment(session);
        paymentHandlerService.handlePaymentSuccess(session);

        return ResponseEntity.ok("Order is successfully created (Mock Test)");
    }
}
