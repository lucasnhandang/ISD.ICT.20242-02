package com.hustict.aims.service.placeOrder;

import com.hustict.aims.dto.payment.VnPayCreateRequestDTO;
import java.util.Map;

public interface PaymentSubsystem {
    String createPaymentUrl(VnPayCreateRequestDTO req, String clientIp, String returnUrl, String txnRef);
    boolean handlePaymentReturn(Map<String, String> params);
    // Có thể mở rộng thêm các phương thức khác như refund, checkStatus...
} 