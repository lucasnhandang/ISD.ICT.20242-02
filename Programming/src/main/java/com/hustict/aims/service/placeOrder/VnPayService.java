package com.hustict.aims.service.placeOrder;

import com.hustict.aims.dto.payment.VnPayCreateRequestDTO;
import com.hustict.aims.model.order.Order;
import com.hustict.aims.model.payment.PaymentTransaction;
import com.hustict.aims.repository.OrderRepository;
import com.hustict.aims.repository.PaymentTransactionRepository;
import com.hustict.aims.utils.VnPayConfig;
import com.hustict.aims.utils.VnPayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class VnPayService {
    @Autowired
    private VnPayConfig vnPayConfig;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private PaymentTransactionRepository paymentTransactionRepository;

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

    public boolean handleVnPayReturn(Map<String, String> params) {
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
            // Giao dịch thành công, cập nhật DB
            Optional<Order> orderOpt = orderRepository.findById(Long.valueOf(txnRef));
            if (orderOpt.isPresent()) {
                Order order = orderOpt.get();
                // Cập nhật trạng thái đơn hàng nếu cần
                // ...
                // Lưu thông tin giao dịch
                PaymentTransaction txn = new PaymentTransaction();
                txn.setBankTransactionId(bankTxnId);
                txn.setContent("VNPAY Payment for order " + txnRef);
                txn.setPaymentTime(LocalDateTime.now());
                txn.setPaymentAmount(Integer.parseInt(amountStr) / 100);
                txn.setCardType(cardType);
                paymentTransactionRepository.save(txn);
                // Gắn transaction vào order nếu cần
                // ...
                orderRepository.save(order);
            }
            return true;
        }
        return false;
    }
} 