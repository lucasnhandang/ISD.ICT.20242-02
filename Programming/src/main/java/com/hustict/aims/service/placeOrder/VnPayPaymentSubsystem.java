package com.hustict.aims.service.placeOrder;

import com.hustict.aims.dto.payment.PaymentTransactionDTO;
import com.hustict.aims.dto.payment.VnPayCreateRequestDTO;
import com.hustict.aims.dto.payment.PlaceOrderRequestDTO;
import com.hustict.aims.model.order.Order;
import com.hustict.aims.model.payment.PaymentTransaction;
import com.hustict.aims.repository.OrderRepository;
import com.hustict.aims.repository.PaymentTransactionRepository;
import com.hustict.aims.utils.VnPayConfig;
import com.hustict.aims.utils.VnPayUtils;

import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.*;
import java.io.IOException;

@Service("vnPayPaymentSubsystem")
public class VnPayPaymentSubsystem implements PaymentSubsystem {
    @Autowired
    private VnPayConfig vnPayConfig;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private PaymentTransactionRepository paymentTransactionRepository;
    @Autowired
    private RestTemplate restTemplate;

    @Override
    public String createPaymentUrl(VnPayCreateRequestDTO req, String clientIp, String returnUrl, String txnRef) {
        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", "2.1.0");
        vnp_Params.put("vnp_Command", "pay");
        vnp_Params.put("vnp_TmnCode", vnPayConfig.getTmnCode());
        vnp_Params.put("vnp_Amount", String.valueOf(req.getAmount() * 100));
        vnp_Params.put("vnp_CurrCode", "VND");
        if (req.getBankCode() != null && !req.getBankCode().isEmpty()) {
            vnp_Params.put("vnp_BankCode", req.getBankCode());
        }
        vnp_Params.put("vnp_TxnRef", txnRef);
        vnp_Params.put("vnp_OrderInfo", req.getOrderInfo());
        vnp_Params.put("vnp_OrderType", req.getOrderType());
        vnp_Params.put("vnp_Locale", req.getLanguage() != null ? req.getLanguage() : "vn");
        vnp_Params.put("vnp_ReturnUrl", returnUrl);
        vnp_Params.put("vnp_IpAddr", clientIp);
        String vnp_CreateDate = java.time.format.DateTimeFormatter.ofPattern("yyyyMMddHHmmss").format(LocalDateTime.now());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);
        String vnp_ExpireDate = java.time.format.DateTimeFormatter.ofPattern("yyyyMMddHHmmss").format(LocalDateTime.now().plusMinutes(15));
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);
        // Billing & Invoice (nếu có)
        if (req.getBillingMobile() != null) vnp_Params.put("vnp_Bill_Mobile", req.getBillingMobile());
        if (req.getBillingEmail() != null) vnp_Params.put("vnp_Bill_Email", req.getBillingEmail());
        if (req.getBillingFullName() != null && !req.getBillingFullName().isEmpty()) {
            String[] names = req.getBillingFullName().trim().split(" ");
            if (names.length > 1) {
                vnp_Params.put("vnp_Bill_FirstName", names[0]);
                vnp_Params.put("vnp_Bill_LastName", names[names.length - 1]);
            }
        }
        if (req.getBillingAddress() != null) vnp_Params.put("vnp_Bill_Address", req.getBillingAddress());
        if (req.getBillingCity() != null) vnp_Params.put("vnp_Bill_City", req.getBillingCity());
        if (req.getBillingCountry() != null) vnp_Params.put("vnp_Bill_Country", req.getBillingCountry());
        if (req.getBillingState() != null) vnp_Params.put("vnp_Bill_State", req.getBillingState());
        if (req.getInvMobile() != null) vnp_Params.put("vnp_Inv_Phone", req.getInvMobile());
        if (req.getInvEmail() != null) vnp_Params.put("vnp_Inv_Email", req.getInvEmail());
        if (req.getInvCustomer() != null) vnp_Params.put("vnp_Inv_Customer", req.getInvCustomer());
        if (req.getInvAddress() != null) vnp_Params.put("vnp_Inv_Address", req.getInvAddress());
        if (req.getInvCompany() != null) vnp_Params.put("vnp_Inv_Company", req.getInvCompany());
        if (req.getInvTaxcode() != null) vnp_Params.put("vnp_Inv_Taxcode", req.getInvTaxcode());
        if (req.getInvType() != null) vnp_Params.put("vnp_Inv_Type", req.getInvType());
        // Build hash & query
        String hashData = VnPayUtils.buildHashData(vnp_Params);
        String vnp_SecureHash = VnPayUtils.hmacSHA512(vnPayConfig.getHashSecret(), hashData);
        vnp_Params.put("vnp_SecureHash", vnp_SecureHash);
        String queryUrl = VnPayUtils.buildQueryString(vnp_Params);
        return vnPayConfig.getPayUrl() + "?" + queryUrl;
    }

    @Override
    public boolean handlePaymentReturn(Map<String, String> params) {
        // Validate checksum
        String vnp_SecureHash = params.get("vnp_SecureHash");
        params.remove("vnp_SecureHash");
        params.remove("vnp_SecureHashType");
        String hashData = VnPayUtils.buildHashData(params);
        String calculatedHash = VnPayUtils.hmacSHA512(vnPayConfig.getHashSecret(), hashData);
        System.out.println("[VNPAY] hashData: " + hashData);
        System.out.println("[VNPAY] calculatedHash: " + calculatedHash);
        System.out.println("[VNPAY] vnp_SecureHash: " + vnp_SecureHash);
        boolean valid = calculatedHash.equalsIgnoreCase(vnp_SecureHash);
        if (!valid) return false;

        // Kiểm tra mã giao dịch, số tiền, trạng thái...
        String txnRef = params.get("vnp_TxnRef");
        String responseCode = params.get("vnp_ResponseCode");
        String amountStr = params.get("vnp_Amount");
        String bankTxnId = params.get("vnp_TransactionNo");
        String payDate = params.get("vnp_PayDate");
        String cardType = params.get("vnp_CardType");

        if ("00".equals(responseCode)) {
            // Tạo PaymentTransactionDTO và lưu vào session
            PaymentTransactionDTO txnDTO = new PaymentTransactionDTO();
            txnDTO.setBankTransactionId(bankTxnId);
            txnDTO.setContent("VNPAY Payment for order " + txnRef);
            txnDTO.setPaymentTime(LocalDateTime.now());
            txnDTO.setPaymentAmount(Integer.parseInt(amountStr) / 100); // Lưu ý chia cho 100 để chuyển sang tiền thực
            txnDTO.setCardType(cardType);
            txnDTO.setCurrency("VND");

            // Lưu Payment URL nếu có
            String returnUrl = vnPayConfig.getReturnUrl() + "?" + params.entrySet().stream()
                .map(e -> e.getKey() + "=" + e.getValue())
                .reduce("", (a, b) -> a + (a.isEmpty() ? "" : "&") + b);
            txnDTO.setPaymentUrl(returnUrl);

            return true;
        }
        return false;
    }

    @Override
    public String handleReturnAndBuildRedirect(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Map<String, String> params = new HashMap<>();
        request.getParameterMap().forEach((k, v) -> params.put(k, v[0]));

        // Xử lý logic như trong PaymentController
        String vnpTxnRef = params.getOrDefault("vnp_TxnRef", "");
        String vnpResponseCode = params.getOrDefault("vnp_ResponseCode", "");
        String vnpTransactionNo = params.getOrDefault("vnp_TransactionNo", "");
        String vnpAmount = params.getOrDefault("vnp_Amount", "");
        String vnpPayDate = params.getOrDefault("vnp_PayDate", "");
        String vnpCardType = params.getOrDefault("vnp_CardType", "");
        String vnpOrderInfo = params.getOrDefault("vnp_OrderInfo", "");
        String vnpBankCode = params.getOrDefault("vnp_BankCode", "");
        String vnpTransactionStatus = params.getOrDefault("vnp_TransactionStatus", "");
        boolean isSuccess = "00".equals(vnpTransactionStatus);
        String paymentUrl = "vnp_TxnRef=" + vnpTxnRef
                + "&vnp_ResponseCode=" + vnpResponseCode
                + "&vnp_TransactionNo=" + vnpTransactionNo
                + "&vnp_Amount=" + vnpAmount
                + "&vnp_PayDate=" + vnpPayDate
                + "&vnp_CardType=" + vnpCardType
                + "&vnp_OrderInfo=" + vnpOrderInfo
                + "&vnp_BankCode=" + vnpBankCode
                + "&vnp_TransactionStatus=" + vnpTransactionStatus;

        PaymentTransactionDTO paymentTransaction = new PaymentTransactionDTO();
        paymentTransaction.setBankTransactionId(params.get("vnp_TransactionNo"));
        paymentTransaction.setContent(params.get("vnp_OrderInfo"));
        try {
            int rawAmount = Integer.parseInt(params.get("vnp_Amount"));
            int normalizedAmount = rawAmount / 100;
            paymentTransaction.setPaymentAmount(normalizedAmount);
        } catch (Exception e) {
            paymentTransaction.setPaymentAmount(0);
        }
        paymentTransaction.setPaymentTime(java.time.LocalDateTime.now());
        paymentTransaction.setCardType(params.get("vnp_CardType"));
        paymentTransaction.setCurrency("VND");
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

        PlaceOrderRequestDTO placeOrderRequestDTO = new PlaceOrderRequestDTO();
        placeOrderRequestDTO.setPaymentTransaction(paymentTransaction);
        placeOrderRequestDTO.setOrderId(orderid);

        
        String placeOrderUrl = "http://localhost:8080/api/v1/place-order/handle-payment";
        restTemplate.postForEntity(placeOrderUrl, placeOrderRequestDTO, String.class);
        // Ở đây chỉ trả về URL frontend để controller redirect
        String frontendUrl = "http://localhost:3000/vnpay-return" +
                "?vnp_TxnRef=" + vnpTxnRef +
                "&vnp_ResponseCode=" + vnpResponseCode +
                "&vnp_TransactionNo=" + vnpTransactionNo +
                "&vnp_Amount=" + vnpAmount +
                "&vnp_PayDate=" + vnpPayDate +
                "&vnp_OrderInfo=" + vnpOrderInfo +
                "&orderId=" + orderid + "&paymentSuccess=" + isSuccess;
        return frontendUrl;
    }
}

