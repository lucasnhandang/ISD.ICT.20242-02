package com.hustict.aims.service.refund;
import org.springframework.stereotype.Component;


@Component
public class PaymentSystem {

    private final VNPayRefundService vnPayPaymentService;
    private final PaypalRefundService paypalPaymentService;

    public PaymentSystem(VNPayRefundService vnPayPaymentService, PaypalRefundService paypalPaymentService) {
        this.vnPayPaymentService = vnPayPaymentService;
        this.paypalPaymentService = paypalPaymentService;
    }

    public void processRefund(Long orderId, String system) {
        switch (system) {
            case "VNPay":
                vnPayPaymentService.processRefund(orderId);
                break;
            case "Paypal":
                paypalPaymentService.processRefund(orderId);
                break;
            default:
                throw new IllegalArgumentException("Unsupported payment system: " + system);
        }
    }

}
