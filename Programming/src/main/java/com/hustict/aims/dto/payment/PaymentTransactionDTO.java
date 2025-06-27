package com.hustict.aims.dto.payment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentTransactionDTO {
    private String bankTransactionId;
    private String content;
    private LocalDateTime paymentTime;
    private int paymentAmount;
    private String cardType;
    private String currency;
}
