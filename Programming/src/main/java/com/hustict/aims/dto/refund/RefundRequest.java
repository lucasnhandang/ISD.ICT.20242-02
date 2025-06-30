package com.hustict.aims.dto.refund;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Getter
@Setter
@AllArgsConstructor  
@NoArgsConstructor   
public class RefundRequest {
    private String requestId;
    private String version;
    private String command;
    private String tmnCode;
    private String transactionType;
    private String txnRef;
    private int amount;
    private String orderInfo;
    private String transactionNo;
    private String transactionDate;
    private String createBy;
    private String createDate;
    private String ipAddr;
    private String secureHash;
}
