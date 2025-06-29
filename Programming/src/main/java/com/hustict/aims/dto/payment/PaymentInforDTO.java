package com.hustict.aims.dto.payment;

import java.time.LocalDateTime;

public class PaymentInforDTO {
    private String bankTransactionId;
    private String content;
    private LocalDateTime paymentTime;
    private int paymentAmount;
    private String cardType;
    private String currency;
    private String system = "VNPay";
    private String paymentUrl;
}
