package com.hustict.aims.utils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class VnPayUtils {
    public static String hmacSHA512(String key, String data) {
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
            throw new RuntimeException("Error while hashing data", e);
        }
    }

    public static String buildQueryString(Map<String, String> params) {
        StringBuilder query = new StringBuilder();
        try {
            List<String> keys = new ArrayList<>(params.keySet());
            Collections.sort(keys);
            for (int i = 0; i < keys.size(); i++) {
                String key = keys.get(i);
                String value = params.get(key);
                if (value != null && !value.isEmpty()) {
                    query.append(URLEncoder.encode(key, "UTF-8"));
                    query.append("=");
                    query.append(URLEncoder.encode(value, "UTF-8"));
                    if (i < keys.size() - 1) {
                        query.append("&");
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error building query string", e);
        }
        return query.toString();
    }

    public static String buildHashData(Map<String, String> params) {
        StringBuilder hashData = new StringBuilder();
        try {
            List<String> keys = new ArrayList<>(params.keySet());
            Collections.sort(keys);
            for (int i = 0; i < keys.size(); i++) {
                String key = keys.get(i);
                String value = params.get(key);
                if (value != null && !value.isEmpty()) {
                    hashData.append(key);
                    hashData.append("=");
                    hashData.append(URLEncoder.encode(value, "UTF-8"));
                    if (i < keys.size() - 1) {
                        hashData.append("&");
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error building hash data", e);
        }
        return hashData.toString();
    }

    public static boolean validateChecksum(Map<String, String> params, String hashSecret, String vnp_SecureHash) {
        // 1. Loại bỏ 2 trường không được mang vào data để hash
        params.remove("vnp_SecureHash");
        params.remove("vnp_SecureHashType");
        
        // 2. Sinh lại chuỗi data và tính HMAC
        String hashData = buildHashData(params);
        String calculatedHash = hmacSHA512(hashSecret, hashData);
        return calculatedHash.equalsIgnoreCase(vnp_SecureHash);
    }
    
} 