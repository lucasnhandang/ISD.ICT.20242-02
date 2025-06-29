package com.hustict.aims.controller;

import com.hustict.aims.dto.payment.PaymentResultDTO;
import com.hustict.aims.dto.payment.PaymentTransactionDTO;
import com.hustict.aims.dto.payment.PlaceOrderRequestDTO;
import com.hustict.aims.dto.payment.VnPayCreateRequestDTO;
import com.hustict.aims.dto.payment.VnPayIpnResponseDTO;
import com.hustict.aims.dto.order.OrderDTO;

import com.hustict.aims.service.PaymentResultService;
import com.hustict.aims.service.payment.SavePaymentTransaction;
import com.hustict.aims.service.placeOrder.VnPayService;
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


@RestController
@RequestMapping("/api/v1/payment")
@CrossOrigin(origins = "*")
public class PaymentController {
    @Autowired
    private VnPayService vnPayService;
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
        
        PaymentResultDTO paymentResult = paymentResultService.processPaymentReturn(params);
        
        //StringBuilder redirectUrl = new StringBuilder(FRONTEND_URL + "/vnpay-return");
        // redirectUrl.append("?vnp_TxnRef=").append(params.getOrDefault("vnp_TxnRef", ""));
        // redirectUrl.append("&vnp_ResponseCode=").append(params.getOrDefault("vnp_ResponseCode", ""));
        // redirectUrl.append("&vnp_TransactionNo=").append(params.getOrDefault("vnp_TransactionNo", ""));
        // redirectUrl.append("&vnp_Amount=").append(params.getOrDefault("vnp_Amount", ""));
        // redirectUrl.append("&vnp_PayDate=").append(params.getOrDefault("vnp_PayDate", ""));
        // redirectUrl.append("&vnp_CardType=").append(params.getOrDefault("vnp_CardType", ""));
        String vnpTxnRef = params.getOrDefault("vnp_TxnRef", "");
        String vnpResponseCode = params.getOrDefault("vnp_ResponseCode", "");
        String vnpTransactionNo = params.getOrDefault("vnp_TransactionNo", "");
        String vnpAmount = params.getOrDefault("vnp_Amount", "");
        String vnpPayDate = params.getOrDefault("vnp_PayDate", "");
        String vnpCardType = params.getOrDefault("vnp_CardType", "");
        String vnpOrderInfo = params.getOrDefault("vnp_OrderInfo", "");
        String vnpBankCode = params.getOrDefault("vnp_BankCode", "");
        String vnpTransactionStatus = params.getOrDefault("vnp_TransactionStatus", "");

        // Tạo một URL với tất cả các thông tin này
        String paymentUrl = "vnp_TxnRef=" + vnpTxnRef
                            + "&vnp_ResponseCode=" + vnpResponseCode
                            + "&vnp_TransactionNo=" + vnpTransactionNo
                            + "&vnp_Amount=" + vnpAmount
                            + "&vnp_PayDate=" + vnpPayDate
                            + "&vnp_CardType=" + vnpCardType
                            + "&vnp_OrderInfo=" + vnpOrderInfo
                            + "&vnp_BankCode=" + vnpBankCode
                            + "&vnp_TransactionStatus=" + vnpTransactionStatus;
                        
        
        //paymentResult.setSecureHash(params.getOrDefault("vnp_SecureHash", "")); --> Check secure hash
        PaymentTransactionDTO paymentTransaction = new PaymentTransactionDTO();
        paymentTransaction.setBankTransactionId(params.get("vnp_TransactionNo"));
        paymentTransaction.setContent(params.get("vnp_OrderInfo"));
        try {
            int rawAmount = Integer.parseInt(params.get("vnp_Amount"));
            paymentTransaction.setPaymentAmount(rawAmount / 100);
        } catch (Exception e) {
            paymentTransaction.setPaymentAmount(0);
        }
        paymentTransaction.setPaymentTime(java.time.LocalDateTime.now()); 
        paymentTransaction.setCardType(params.get("vnp_CardType")); 
        paymentTransaction.setCurrency("VND"); // khong nen fix cung
        paymentTransaction.setSystem("VNPay");
        paymentTransaction.setPaymentUrl(paymentUrl);
        paymentTransaction.setContent(params.get("vnp_OrderInfo"));
        
        String content = params.get("vnp_OrderInfo");
        int lastSpaceIndex = content.lastIndexOf(" ");
        String idStr = (lastSpaceIndex != -1) ? content.substring(lastSpaceIndex + 1).trim() : null;
        
        Long orderid = null;

        try {
            if (idStr != null) {
                orderid = Long.parseLong(idStr);
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("ID không hợp lệ: " + idStr);
        }
        
        System.out.println("OrderId = " + orderid);
        PlaceOrderRequestDTO placeOrderRequestDTO = new PlaceOrderRequestDTO();
        placeOrderRequestDTO.setPaymentTransaction(paymentTransaction);  
        placeOrderRequestDTO.setOrderId(orderid); 


        String placeOrderUrl = "http://localhost:8080/api/v1/place-order/handle-payment"; 
        
        
        ResponseEntity<String> restResponse = restTemplate.postForEntity(placeOrderUrl, placeOrderRequestDTO, String.class);

        // Redirect đến frontend với các query params
        String frontendUrl = "http://localhost:3000/vnpay-return" +
                           "?vnp_TxnRef=" + vnpTxnRef +
                           "&vnp_ResponseCode=" + vnpResponseCode +
                           "&vnp_TransactionNo=" + vnpTransactionNo +
                           "&vnp_Amount=" + vnpAmount +
                           "&vnp_PayDate=" + vnpPayDate +
                           "&vnp_OrderInfo=" + vnpOrderInfo +
                           "&orderId=" + orderid;
        
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
        boolean success = vnPayService.handleVnPayReturn(params);
        if (success) {
            return ResponseEntity.ok(new VnPayIpnResponseDTO("00", "Confirm Success"));
        } else {
            return ResponseEntity.ok(new VnPayIpnResponseDTO("97", "Invalid Checksum or Failed"));
        }
    }
} 
