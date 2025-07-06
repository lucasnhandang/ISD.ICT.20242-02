package com.hustict.aims.service.placeOrder;

import com.hustict.aims.dto.payment.VnPayCreateRequestDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

public interface PaymentSubsystem {
    String createPaymentUrl(VnPayCreateRequestDTO req, String clientIp, String returnUrl, String txnRef);
    boolean handlePaymentReturn(Map<String, String> params);
    String handleReturnAndBuildRedirect(HttpServletRequest request, HttpServletResponse response) throws IOException;
} 