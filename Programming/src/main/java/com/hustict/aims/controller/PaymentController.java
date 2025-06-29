package com.hustict.aims.controller;

import com.hustict.aims.dto.payment.PaymentResultDTO;
import com.hustict.aims.dto.payment.PaymentTransactionDTO;
import com.hustict.aims.dto.payment.PlaceOrderRequestDTO;
import com.hustict.aims.dto.payment.VnPayCreateRequestDTO;
import com.hustict.aims.dto.payment.VnPayIpnResponseDTO;
import com.hustict.aims.dto.order.OrderDTO;

import com.hustict.aims.service.PaymentResultService;
import com.hustict.aims.service.payment.SavePaymentTransaction;
import com.hustict.aims.service.placeOrder.PaymentSubsystem;
import com.hustict.aims.utils.VnPayConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Qualifier;


@RestController
@RequestMapping("/api/v1/payment")
@CrossOrigin(origins = "*")
public class PaymentController {
    @Autowired
    @Qualifier("vnPayPaymentSubsystem")
    private PaymentSubsystem paymentSubsystem;
    @Autowired
    private VnPayConfig vnPayConfig;
    @Autowired
    private PaymentResultService paymentResultService;

    @Autowired
    private SavePaymentTransaction savePaymentTransaction;

    
    @Autowired
    private RestTemplate restTemplate;
    
    // Frontend URL để redirect sau khi xử lý VnPay
    private static final String FRONTEND_URL = "http://localhost:3000";

    @PostMapping("/vnpay-create")
    public ResponseEntity<?> createVnPayUrl(@RequestBody VnPayCreateRequestDTO req, HttpServletRequest request) {
        String clientIp = request.getRemoteAddr();
        // Tạo mã đơn hàng (txnRef) ở đây, ví dụ lấy từ DB hoặc random
        String txnRef = String.valueOf(System.currentTimeMillis());
        String url = paymentSubsystem.createPaymentUrl(req, clientIp, vnPayConfig.getReturnUrl(), txnRef);
        Map<String, String> resp = new HashMap<>();
        resp.put("paymentUrl", url);
        resp.put("txnRef", txnRef);
        return ResponseEntity.ok(resp);
    }

    @GetMapping("/vnpay-return")
    public void vnPayReturn(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String frontendUrl = paymentSubsystem.handleReturnAndBuildRedirect(request, response);
        response.sendRedirect(frontendUrl);
    }

    @GetMapping("/payment-result")
    public ResponseEntity<PaymentResultDTO> getPaymentResult(
            @RequestParam(required = false) String txnRef,
            @RequestParam(required = false) String responseCode) {
        
        PaymentResultDTO result = paymentResultService.createPaymentResult(txnRef, responseCode);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/payment-status")
    public ResponseEntity<Map<String, Object>> getPaymentStatus(@RequestParam String txnRef) {
        Map<String, Object> status = new HashMap<>();
     
        status.put("txnRef", txnRef);
        status.put("status", "PENDING");
        status.put("message", "Đang kiểm tra trạng thái thanh toán...");
        status.put("timestamp", System.currentTimeMillis());
        
        return ResponseEntity.ok(status);
    }

    @PostMapping("/vnpay-ipn")
    public ResponseEntity<VnPayIpnResponseDTO> vnPayIpn(HttpServletRequest request) {
        Map<String, String> params = new HashMap<>();
        request.getParameterMap().forEach((k, v) -> params.put(k, v[0]));
        boolean success = paymentSubsystem.handlePaymentReturn(params);
        if (success) {
            return ResponseEntity.ok(new VnPayIpnResponseDTO("00", "Confirm Success"));
        } else {
            return ResponseEntity.ok(new VnPayIpnResponseDTO("97", "Invalid Checksum or Failed"));
        }
    }
} 
