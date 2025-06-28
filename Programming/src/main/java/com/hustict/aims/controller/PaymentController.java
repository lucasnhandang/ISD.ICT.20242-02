package com.hustict.aims.controller;

import com.hustict.aims.dto.payment.VnPayCreateRequestDTO;
import com.hustict.aims.dto.payment.VnPayIpnResponseDTO;
import com.hustict.aims.dto.payment.VnPayReturnResponseDTO;
import com.hustict.aims.service.placeOrder.VnPayService;
import com.hustict.aims.utils.VnPayConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {
    @Autowired
    private VnPayService vnPayService;
    @Autowired
    private VnPayConfig vnPayConfig;

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
    public ResponseEntity<VnPayReturnResponseDTO> vnPayReturn(HttpServletRequest request) {
        Map<String, String> params = new HashMap<>();
        request.getParameterMap().forEach((k, v) -> params.put(k, v[0]));
        
        boolean success = vnPayService.handleVnPayReturn(params);
        
        VnPayReturnResponseDTO response = new VnPayReturnResponseDTO();
        response.setVnp_TxnRef(params.get("vnp_TxnRef"));
        response.setVnp_ResponseCode(params.get("vnp_ResponseCode"));
        response.setVnp_TransactionNo(params.get("vnp_TransactionNo"));
        response.setVnp_Amount(params.get("vnp_Amount"));
        response.setVnp_OrderInfo(params.get("vnp_OrderInfo"));
        response.setVnp_PayDate(params.get("vnp_PayDate"));
        response.setVnp_BankCode(params.get("vnp_BankCode"));
        response.setVnp_CardType(params.get("vnp_CardType"));
        response.setVnp_SecureHash(params.get("vnp_SecureHash"));
        response.setSuccess(success);
        
        if (success) {
            response.setMessage("Thanh toán thành công!");
        } else {
            response.setMessage("Thanh toán thất bại!");
        }
        
        return ResponseEntity.ok(response);
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
