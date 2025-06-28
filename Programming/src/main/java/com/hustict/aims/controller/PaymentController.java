package com.hustict.aims.controller;

import com.hustict.aims.dto.payment.PaymentResultDTO;
import com.hustict.aims.dto.payment.VnPayCreateRequestDTO;
import com.hustict.aims.dto.payment.VnPayIpnResponseDTO;
import com.hustict.aims.dto.payment.VnPayReturnResponseDTO;
import com.hustict.aims.model.payment.PaymentTransaction;
import com.hustict.aims.repository.PaymentTransactionRepository;
import com.hustict.aims.service.PaymentResultService;
import com.hustict.aims.service.placeOrder.VnPayService;
import com.hustict.aims.utils.VnPayConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import com.hustict.aims.dto.payment.PaymentTransactionDTO;
import jakarta.servlet.http.HttpSession;
import com.hustict.aims.utils.mapper.PaymentTransactionMapper;

@RestController
@RequestMapping("/api/payment")
@CrossOrigin(origins = "*")
public class PaymentController {
    private static final Logger logger = LoggerFactory.getLogger(PaymentController.class);
    
    @Autowired
    private VnPayService vnPayService;
    @Autowired
    private VnPayConfig vnPayConfig;
    @Autowired
    private PaymentResultService paymentResultService;
    @Autowired
    private PaymentTransactionRepository paymentTransactionRepository;
    @Autowired
    private PaymentTransactionMapper paymentTransactionMapper;

    @PostMapping("/vnpay-create")
    public ResponseEntity<?> createVnPayUrl(@RequestBody VnPayCreateRequestDTO req, HttpServletRequest request) {
        String clientIp = request.getRemoteAddr();
        // Tạo mã đơn hàng (txnRef) ở đây, ví dụ lấy từ DB hoặc random
        String txnRef = String.valueOf(System.currentTimeMillis());
        String url = vnPayService.createPaymentUrl(req, clientIp, vnPayConfig.getReturnUrl(), txnRef);
        
        // Log thông tin về URL
        logger.info("Generated VNPay URL length: {}", url.length());
        logger.info("Generated VNPay URL: {}", url);
        
        // Lưu thông tin giao dịch vào database
        PaymentTransaction paymentTransaction = new PaymentTransaction();
        paymentTransaction.setBankTransactionId(txnRef);
        paymentTransaction.setContent(req.getOrderInfo());
        paymentTransaction.setPaymentAmount(req.getAmount());
        paymentTransaction.setPaymentTime(LocalDateTime.now());
        paymentTransaction.setCardType("VNPay");
        paymentTransaction.setCurrency("VND");
        paymentTransaction.setSystems("VNPay");
        paymentTransaction.setPaymentUrl(url);
        
        try {
            paymentTransactionRepository.save(paymentTransaction);
            logger.info("Payment transaction saved successfully with ID: {}", paymentTransaction.getId());
        } catch (Exception e) {
            logger.error("Failed to save payment transaction. URL length: {}, Error: {}", url.length(), e.getMessage());
            return ResponseEntity.badRequest().body("Failed to save payment transaction. URL length: " + url.length() + ", Error: " + e.getMessage());
        }
        
        Map<String, String> resp = new HashMap<>();
        resp.put("paymentUrl", url);
        resp.put("txnRef", txnRef);
        resp.put("transactionId", paymentTransaction.getId().toString());
        resp.put("urlLength", String.valueOf(url.length()));
        return ResponseEntity.ok(resp);
    }

    @GetMapping("/vnpay-return")
    public ResponseEntity<PaymentResultDTO> vnPayReturn(HttpServletRequest request) {
        Map<String, String> params = new HashMap<>();
        request.getParameterMap().forEach((k, v) -> params.put(k, v[0]));
        
        PaymentResultDTO response = paymentResultService.processPaymentReturn(params);
        return ResponseEntity.ok(response);
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

    @PostMapping("/save-transaction")
    public ResponseEntity<String> savePaymentTransactionToDatabase(@RequestBody PaymentTransactionDTO paymentTransactionDTO) {
        try {
            // Log thông tin về payment URL
            if (paymentTransactionDTO.getPaymentUrl() != null) {
                logger.info("Payment URL length: {}", paymentTransactionDTO.getPaymentUrl().length());
                logger.info("Payment URL: {}", paymentTransactionDTO.getPaymentUrl());
            }
            
            PaymentTransaction entity = paymentTransactionMapper.toEntity(paymentTransactionDTO);
            PaymentTransaction savedEntity = paymentTransactionRepository.save(entity);
            logger.info("PaymentTransaction saved to database successfully with ID: {}", savedEntity.getId());
            return ResponseEntity.ok("PaymentTransaction saved to database successfully with ID: " + savedEntity.getId());
        } catch (Exception e) {
            logger.error("Failed to save payment transaction. Error: {}", e.getMessage());
            if (paymentTransactionDTO.getPaymentUrl() != null) {
                logger.error("Payment URL length: {}", paymentTransactionDTO.getPaymentUrl().length());
            }
            return ResponseEntity.badRequest().body("Failed to save payment transaction: " + e.getMessage());
        }
    }

    @PutMapping("/update-payment-url")
    public ResponseEntity<String> updatePaymentUrl(@RequestParam String txnRef, @RequestParam String paymentUrl) {
        try {
            // Tìm payment transaction theo txnRef
            var paymentTransactionOpt = paymentTransactionRepository.findByBankTransactionId(txnRef);
            if (paymentTransactionOpt.isPresent()) {
                PaymentTransaction paymentTransaction = paymentTransactionOpt.get();
                paymentTransaction.setPaymentUrl(paymentUrl);
                paymentTransactionRepository.save(paymentTransaction);
                return ResponseEntity.ok("Payment URL updated successfully for transaction: " + txnRef);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error updating payment URL: " + e.getMessage());
        }
    }

    @GetMapping("/get-payment-url")
    public ResponseEntity<Map<String, String>> getPaymentUrl(@RequestParam String txnRef) {
        try {
            var paymentTransactionOpt = paymentTransactionRepository.findByBankTransactionId(txnRef);
            if (paymentTransactionOpt.isPresent()) {
                PaymentTransaction paymentTransaction = paymentTransactionOpt.get();
                Map<String, String> response = new HashMap<>();
                response.put("txnRef", txnRef);
                response.put("paymentUrl", paymentTransaction.getPaymentUrl());
                response.put("status", "FOUND");
                return ResponseEntity.ok(response);
            } else {
                Map<String, String> response = new HashMap<>();
                response.put("txnRef", txnRef);
                response.put("status", "NOT_FOUND");
                return ResponseEntity.ok(response);
            }
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
} 
