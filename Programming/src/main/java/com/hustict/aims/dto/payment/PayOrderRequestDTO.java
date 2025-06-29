package com.hustict.aims.dto.payment;

public class PayOrderRequestDTO {
    private long amount;
    private String orderInfo;
    private String txnRef;

    public PayOrderRequestDTO() {}

    public long getAmount() { return amount; }
    public void setAmount(long amount) { this.amount = amount; }

    public String getOrderInfo() { return orderInfo; }
    public void setOrderInfo(String orderInfo) { this.orderInfo = orderInfo; }

    public String getTxnRef() { return txnRef; }
    public void setTxnRef(String txnRef) { this.txnRef = txnRef; }
} 