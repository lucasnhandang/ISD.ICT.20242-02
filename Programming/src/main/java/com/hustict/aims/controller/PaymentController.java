package com.hustict.aims.controller;

import com.hustict.aims.dto.payment.PaymentResultDTO;
import com.hustict.aims.dto.payment.VnPayCreateRequestDTO;
import com.hustict.aims.dto.payment.VnPayIpnResponseDTO;
import com.hustict.aims.dto.payment.VnPayReturnResponseDTO;
import com.hustict.aims.service.PaymentResultService;
import com.hustict.aims.service.placeOrder.VnPayService;
import com.hustict.aims.utils.VnPayConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/payment")
@CrossOrigin(origins = "*")
public class PaymentController {
    @Autowired
    private VnPayService vnPayService;
    @Autowired
    private VnPayConfig vnPayConfig;
    @Autowired
    private PaymentResultService paymentResultService;
    
    // Frontend URL để redirect sau khi xử lý VnPay
    private static final String FRONTEND_URL = "http://localhost:3000";

    @PostMapping("/vnpay-create")
    public ResponseEntity<?> createVnPayUrl(@RequestBody VnPayCreateRequestDTO req, HttpServletRequest request) {
        String clientIp = request.getRemoteAddr();
        // Tạo mã đơn hàng (txnRef) ở đây, ví dụ lấy từ DB hoặc random
        String txnRef = String.valueOf(System.currentTimeMillis());
        String url = vnPayService.createPaymentUrl(req, clientIp, vnPayConfig.getReturnUrl(), txnRef);
        Map<String, String> resp = new HashMap<>();
        resp.put("paymentUrl", url);
        resp.put("txnRef", txnRef);
        return ResponseEntity.ok(resp);
    }

    @GetMapping("/vnpay-return")
    public void vnPayReturn(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Map<String, String> params = new HashMap<>();
        request.getParameterMap().forEach((k, v) -> params.put(k, v[0]));
        
        // Xử lý kết quả thanh toán
        PaymentResultDTO paymentResult = paymentResultService.processPaymentReturn(params);
        
        // Build redirect URL với parameters
        StringBuilder redirectUrl = new StringBuilder(FRONTEND_URL + "/vnpay-return");
        redirectUrl.append("?vnp_TxnRef=").append(params.getOrDefault("vnp_TxnRef", ""));
        redirectUrl.append("&vnp_ResponseCode=").append(params.getOrDefault("vnp_ResponseCode", ""));
        redirectUrl.append("&vnp_TransactionNo=").append(params.getOrDefault("vnp_TransactionNo", ""));
        redirectUrl.append("&vnp_Amount=").append(params.getOrDefault("vnp_Amount", ""));
        redirectUrl.append("&vnp_PayDate=").append(params.getOrDefault("vnp_PayDate", ""));
        redirectUrl.append("&vnp_CardType=").append(params.getOrDefault("vnp_CardType", ""));
        
        // Redirect đến frontend
        response.sendRedirect(redirectUrl.toString());
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
        
        // TODO: Implement logic to check payment status from database
        // For now, return a mock response
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
        boolean success = vnPayService.handleVnPayReturn(params);
        if (success) {
            return ResponseEntity.ok(new VnPayIpnResponseDTO("00", "Confirm Success"));
        } else {
            return ResponseEntity.ok(new VnPayIpnResponseDTO("97", "Invalid Checksum or Failed"));
        }
    }
} 
