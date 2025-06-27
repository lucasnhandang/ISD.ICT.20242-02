package com.hustict.aims.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class VNPayService {
    @Value("${vnpay.tmnCode}")
    private String vnpTmnCode;

    @Value("${vnpay.hashSecret}")
    private String vnpHashSecret;

    @Value("${vnpay.payUrl}")
    private String vnpPayUrl;

    @Value("${vnpay.returnUrl}")
    private String vnpReturnUrl;

    @Value("${vnpay.version}")
    private String vnpVersion;

    public String createPaymentUrl(long amount, String orderInfo, String txnRef, String ipAddr) {
        // Chuyển IPv6 localhost thành IPv4 nếu cần
        if ("0:0:0:0:0:0:0:1".equals(ipAddr) || "::1".equals(ipAddr)) {
            ipAddr = "127.0.0.1";
        }
        Map<String, String> vnpParams = new HashMap<>();
        vnpParams.put("vnp_Version", vnpVersion);
        vnpParams.put("vnp_Command", "pay");
        vnpParams.put("vnp_TmnCode", vnpTmnCode);
        vnpParams.put("vnp_Amount", String.valueOf(amount * 100));
        vnpParams.put("vnp_CurrCode", "VND");
        vnpParams.put("vnp_TxnRef", txnRef);
        vnpParams.put("vnp_OrderInfo", orderInfo);
        vnpParams.put("vnp_OrderType", "other");
        vnpParams.put("vnp_Locale", "vn");
        vnpParams.put("vnp_ReturnUrl", vnpReturnUrl);
        vnpParams.put("vnp_IpAddr", ipAddr);
        vnpParams.put("vnp_CreateDate", new java.text.SimpleDateFormat("yyyyMMddHHmmss").format(new java.util.Date()));

        // Build hashData (KHÔNG encode, BỎ QUA value rỗng/null)
        List<String> fieldNames = new ArrayList<>(vnpParams.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        for (String fieldName : fieldNames) {
            String value = vnpParams.get(fieldName);
            if (value == null || value.trim().isEmpty()) continue; // Bỏ qua value rỗng/null
            if (hashData.length() > 0) hashData.append('&');
            hashData.append(fieldName).append('=').append(value);

            if (query.length() > 0) query.append('&');
            String encodedKey = java.net.URLEncoder.encode(fieldName, java.nio.charset.StandardCharsets.UTF_8).replaceAll("%20", "+");
            String encodedValue = java.net.URLEncoder.encode(value, java.nio.charset.StandardCharsets.UTF_8).replaceAll("%20", "+");
            query.append(encodedKey).append('=').append(encodedValue);
        }
        // Log để debug
        System.out.println("VNPAY PARAMS: " + vnpParams);
        System.out.println("VNPAY HASH DATA: " + hashData.toString());

        String secureHash = hmacSHA512(vnpHashSecret, hashData.toString());
        System.out.println("VNPAY SECURE HASH: " + secureHash);

        // Thêm secure hash vào query string
        query.append("&vnp_SecureHash=").append(secureHash);

        return vnpPayUrl + "?" + query.toString();
    }

    public boolean validateVnpayResponse(Map<String, String> vnpParams) {
        String receivedHash = vnpParams.remove("vnp_SecureHash");
        vnpParams.remove("vnp_SecureHashType");
        List<String> fieldNames = new ArrayList<>(vnpParams.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        for (String fieldName : fieldNames) {
            String value = vnpParams.get(fieldName);
            if (hashData.length() > 0) hashData.append('&');
            hashData.append(fieldName).append('=').append(value);
        }
        String calculatedHash = hmacSHA512(vnpHashSecret, hashData.toString());
        return calculatedHash.equals(receivedHash);
    }

    private String hmacSHA512(String key, String data) {
        try {
            Mac hmac512 = Mac.getInstance("HmacSHA512");
            SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA512");
            hmac512.init(secretKey);
            byte[] bytes = hmac512.doFinal(data.getBytes(StandardCharsets.UTF_8));
            StringBuilder hash = new StringBuilder();
            for (byte b : bytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hash.append('0');
                hash.append(hex);
            }
            return hash.toString();
        } catch (Exception e) {
            throw new RuntimeException("Error while calculating HMAC SHA512", e);
        }
    }
} 