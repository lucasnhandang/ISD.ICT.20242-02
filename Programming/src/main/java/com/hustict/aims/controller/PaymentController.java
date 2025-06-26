package com.hustict.aims.controller;

import com.hustict.aims.model.payment.PaymentTransaction;
import com.hustict.aims.repository.PaymentTransactionRepository;
import com.hustict.aims.service.VNPayService;
import com.hustict.aims.dto.payment.PayOrderRequestDTO;
import com.hustict.aims.repository.OrderRepository;
import com.hustict.aims.model.order.Order;
import com.hustict.aims.model.order.OrderStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {
    private final VNPayService vnpayService;
    private final PaymentTransactionRepository paymentTransactionRepository;
    private final OrderRepository orderRepository;

    @Autowired
    public PaymentController(VNPayService vnpayService, PaymentTransactionRepository paymentTransactionRepository, OrderRepository orderRepository) {
        this.vnpayService = vnpayService;
        this.paymentTransactionRepository = paymentTransactionRepository;
        this.orderRepository = orderRepository;
    }

    @PostMapping("/create-vnpay-url")
    public ResponseEntity<Map<String, String>> createVnpayUrl(@RequestBody PayOrderRequestDTO requestDTO, HttpServletRequest request) {
        long amount = requestDTO.getAmount();
        String orderInfo = requestDTO.getOrderInfo() != null ? requestDTO.getOrderInfo() : "Thanh toan don hang";
        String txnRef = requestDTO.getTxnRef() != null ? requestDTO.getTxnRef() : String.valueOf(System.currentTimeMillis());
        String ipAddr = "127.0.0.1";
        String paymentUrl = vnpayService.createPaymentUrl(amount, orderInfo, txnRef, ipAddr);
        Map<String, String> response = new HashMap<>();
        response.put("paymentUrl", paymentUrl);
        response.put("txnRef", txnRef);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/vnpay-return")
    public ResponseEntity<String> vnpayReturn(@RequestParam Map<String, String> allParams) {
        System.out.println("VNPAY RETURN PARAMS: " + allParams);
        boolean valid = vnpayService.validateVnpayResponse(new HashMap<>(allParams));
        if (!valid) {
            return ResponseEntity.badRequest().body("Invalid checksum");
        }
        String responseCode = allParams.get("vnp_ResponseCode");
        if ("00".equals(responseCode)) {
            // Save transaction
            PaymentTransaction transaction = new PaymentTransaction();
            transaction.setBankTransactionId(allParams.get("vnp_TransactionNo"));
            transaction.setContent(allParams.get("vnp_OrderInfo"));
            transaction.setPaymentTime(LocalDateTime.now());
            transaction.setPaymentAmount(Integer.parseInt(allParams.get("vnp_Amount")) / 100);
            transaction.setCardType(allParams.get("vnp_CardType"));
            transaction.setCurrency(allParams.get("vnp_CurrCode"));
            paymentTransactionRepository.save(transaction);
            return ResponseEntity.ok("Payment successful");
        } else {
            return ResponseEntity.ok("Payment failed: " + responseCode);
        }
    }

    @GetMapping("/vnpay-ipn")
    public ResponseEntity<String> vnpayIpn(@RequestParam Map<String, String> allParams) {
        System.out.println("VNPAY IPN PARAMS: " + allParams);
        boolean valid = vnpayService.validateVnpayResponse(new HashMap<>(allParams));
        if (!valid) {
            return ResponseEntity.badRequest().body("{\"RspCode\":\"97\",\"Message\":\"Invalid Checksum\"}");
        }
        String responseCode = allParams.get("vnp_ResponseCode");
        if ("00".equals(responseCode)) {
            return ResponseEntity.ok("{\"RspCode\":\"00\",\"Message\":\"Confirm Success\"}");
        } else {
            return ResponseEntity.ok("{\"RspCode\":\"01\",\"Message\":\"Confirm Failed\"}");
        }
    }
} 