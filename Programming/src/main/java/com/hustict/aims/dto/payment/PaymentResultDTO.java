package com.hustict.aims.dto.payment;

import java.time.LocalDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResultDTO {
    private boolean success;
    private String message;
    private String txnRef;
    private String responseCode;
    private String transactionNo;
    private String amount;
    private String orderInfo;
    private String payDate;
    private String bankCode;
    private String cardType;
    private LocalDateTime timestamp;
    private String transactionStatus;
}
