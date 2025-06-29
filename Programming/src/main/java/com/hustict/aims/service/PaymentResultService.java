package com.hustict.aims.service;

import com.hustict.aims.dto.payment.PaymentResultDTO;
import com.hustict.aims.service.placeOrder.VnPayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

import jakarta.servlet.http.HttpSession;

@Service
public class PaymentResultService {
    
    private final VnPayService vnPayService;
    
    @Autowired
    public PaymentResultService(VnPayService vnPayService) {
        this.vnPayService = vnPayService;
    }
    
    public PaymentResultDTO processPaymentReturn(Map<String, String> params) {
        boolean success = vnPayService.handleVnPayReturn(params);
        
        PaymentResultDTO result = new PaymentResultDTO();
        result.setTxnRef(params.get("vnp_TxnRef"));
        result.setResponseCode(params.get("vnp_ResponseCode"));
        result.setTransactionNo(params.get("vnp_TransactionNo"));
        result.setAmount(params.get("vnp_Amount"));
        result.setOrderInfo(params.get("vnp_OrderInfo"));
        result.setPayDate(params.get("vnp_PayDate"));
        result.setBankCode(params.get("vnp_BankCode"));
        result.setCardType(params.get("vnp_CardType"));
        result.setSuccess(success);
        result.setTimestamp(LocalDateTime.now());
        
        if (success) {
            result.setMessage("Thanh toán thành công!");
        } else {
            result.setMessage("Thanh toán thất bại!");
        }
        
        return result;
    }
    
    public PaymentResultDTO createPaymentResult(String txnRef, String responseCode) {
        PaymentResultDTO result = new PaymentResultDTO();
        
        if (txnRef != null && responseCode != null) {
            boolean success = "00".equals(responseCode);
            result.setSuccess(success);
            result.setTxnRef(txnRef);
            result.setResponseCode(responseCode);
            result.setMessage(success ? "Thanh toán thành công!" : "Thanh toán thất bại!");
            result.setTimestamp(LocalDateTime.now());
        } else {
            result.setSuccess(false);
            result.setMessage("Thiếu thông tin giao dịch");
            result.setTimestamp(LocalDateTime.now());
        }
        
        return result;
    }
} 
