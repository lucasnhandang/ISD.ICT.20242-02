package com.hustict.aims.dto.payment;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentTransactionDTO {
    private String bankTransactionId;
    private String content;
    private LocalDateTime paymentTime;
    private int paymentAmount;
    private String cardType;
    private String currency;
    private String system = "VNPay";
    private String paymentUrl;
}
