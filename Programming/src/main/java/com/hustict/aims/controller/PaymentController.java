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
import jakarta.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.net.URLEncoder;

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
        String ipAddr = request.getRemoteAddr();
        String paymentUrl = vnpayService.createPaymentUrl(amount, orderInfo, txnRef, ipAddr);
        Map<String, String> response = new HashMap<>();
        response.put("paymentUrl", paymentUrl);
        response.put("txnRef", txnRef);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/vnpay-return")
    public void vnpayReturn(@RequestParam Map<String, String> allParams, HttpServletResponse response) throws java.io.IOException {
        boolean valid = vnpayService.validateVnpayResponse(new HashMap<>(allParams));
        String result = "fail";
        String message = "Thanh toán thất bại!";
        if (valid && "00".equals(allParams.get("vnp_ResponseCode"))) {
            result = "success";
            message = "Thanh toán thành công!";
        } else if (!valid) {
            message = "Sai chữ ký!";
        }
        String redirectUrl = "http://localhost:3000/payment-result?result=" + result + "&message=" + URLEncoder.encode(message, "UTF-8");
        response.sendRedirect(redirectUrl);
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