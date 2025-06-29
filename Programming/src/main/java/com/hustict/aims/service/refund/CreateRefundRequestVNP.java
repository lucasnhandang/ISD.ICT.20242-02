package com.hustict.aims.service.refund;

import com.hustict.aims.dto.refund.RefundRequest;
import com.hustict.aims.model.invoice.Invoice;
import com.hustict.aims.model.order.Order;
import com.hustict.aims.model.payment.PaymentTransaction;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;


import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.Formatter;

@Service
public class CreateRefundRequestVNP {
    @Value("${vnpay.tmnCode}")
    private String vnpTmnCode; 

    @Value("${vnpay.hashSecret}")
    private String secretKey;  // Lấy từ file cấu hình

    public RefundRequest CreateRefundRequest(Order order, String ipAddr, String createBy) {
        RefundRequest refundRequest = new RefundRequest();

        Invoice invoice = order.getInvoice();
        PaymentTransaction paymentTransaction = invoice.getPaymentTransaction();

        String paymentUrl = paymentTransaction.getPaymentUrl();
        Map<String, String> queryParams = extractQueryParams(paymentUrl);

        String txnRef = queryParams.get("vnp_TxnRef");  
        String transactionNo = queryParams.get("vnp_TransactionNo");  
        int amount = Integer.parseInt(queryParams.get("vnp_Amount"));  

        String orderInfo = "Hoàn tiền cho đơn hàng " + order.getId();

        refundRequest.setRequestId(generateUniqueRequestId());  // Tạo requestId duy nhất
        refundRequest.setVersion("2.1.0");
        refundRequest.setCommand("refund");
        refundRequest.setTmnCode(vnpTmnCode);  
        refundRequest.setTransactionType("02");  
        refundRequest.setTxnRef(txnRef); 
        refundRequest.setAmount(amount);  
        refundRequest.setOrderInfo(orderInfo);  
        refundRequest.setTransactionNo(transactionNo);
        refundRequest.setCreateDate(java.time.LocalDateTime.now().toString());
        refundRequest.setIpAddr(ipAddr);  // Địa chỉ IP của máy chủ
        refundRequest.setCreateBy(createBy);
        String secureHash = generateSecureHash(secretKey, refundRequest);
        refundRequest.setSecureHash(secureHash);

        return refundRequest;
    }

    private Map<String, String> extractQueryParams(String paymentUrl) {
        Map<String, String> params = new HashMap<>();
        try {
            URL url = new URL(paymentUrl);
            String query = url.getQuery();
            if (query != null) {
                String[] pairs = query.split("&");
                for (String pair : pairs) {
                    String[] keyValue = pair.split("=");
                    if (keyValue.length == 2) {
                        params.put(keyValue[0], keyValue[1]);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error parsing URL or extracting parameters: " + e.getMessage());
        }
        return params;
    }

    public static String generateSecureHash(String secretKey, RefundRequest refundRequest) {
        try {
            // Tạo dữ liệu đầu vào (data) theo thứ tự các tham số như mô tả
            String data = refundRequest.getRequestId() + "|" + 
                          refundRequest.getVersion() + "|" + 
                          refundRequest.getCommand() + "|" + 
                          refundRequest.getTmnCode() + "|" + 
                          refundRequest.getTransactionType() + "|" + 
                          refundRequest.getTxnRef() + "|" + 
                          refundRequest.getAmount() + "|" + 
                          refundRequest.getTransactionNo() + "|" + 
                          refundRequest.getTransactionDate() + "|" + 
                          refundRequest.getCreateBy() + "|" + 
                          refundRequest.getCreateDate() + "|" + 
                          refundRequest.getIpAddr() + "|" + 
                          refundRequest.getOrderInfo();
            
            // Tạo đối tượng SecretKeySpec với secretKey của bạn
            SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "HmacSHA512");
            
            // Tạo đối tượng Mac và sử dụng thuật toán HmacSHA512
            Mac mac = Mac.getInstance("HmacSHA512");
            mac.init(secretKeySpec);

            // Tính toán hash
            byte[] hashBytes = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
            
            // Chuyển đổi byte[] thành chuỗi hex
            return bytesToHex(hashBytes);
            
        } catch (Exception e) {
            throw new RuntimeException("Error generating secure hash", e);
        }
    }

    // Hàm chuyển đổi byte[] thành chuỗi hex
    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        try (Formatter formatter = new Formatter(sb)) {
            for (byte b : bytes) {
                formatter.format("%02x", b);
            }
        }
        return sb.toString();
    }

    private String generateUniqueRequestId() {
        return String.valueOf(System.currentTimeMillis());  
    }
}   
