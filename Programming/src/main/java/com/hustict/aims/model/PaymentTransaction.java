package com.hustict.aims.model;

import java.time.LocalDateTime;

public class PaymentTransaction {
    private Long id;
    private String customerName;
    private String cardType;
    private String transactionContent;
    private int totalAmount;
    private String currency;
    private LocalDateTime date;
    private String bankCode;
    private int bankTranNo;

    public PaymentTransaction() {}

    public PaymentTransaction(Long id, String customerName, String cardType, String transactionContent, int totalAmount, String currency, LocalDateTime date, String bankCode, int bankTranNo) {
        this.id = id;
        this.customerName = customerName;
        this.cardType = cardType;
        this.transactionContent = transactionContent;
        this.totalAmount = totalAmount;
        this.currency = currency;
        this.date = date;
        this.bankCode = bankCode;
        this.bankTranNo = bankTranNo;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    public String getCardType() { return cardType; }
    public void setCardType(String cardType) { this.cardType = cardType; }
    public String getTransactionContent() { return transactionContent; }
    public void setTransactionContent(String transactionContent) { this.transactionContent = transactionContent; }
    public int getTotalAmount() { return totalAmount; }
    public void setTotalAmount(int totalAmount) { this.totalAmount = totalAmount; }
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
    public LocalDateTime getDate() { return date; }
    public void setDate(LocalDateTime date) { this.date = date; }
    public String getBankCode() { return bankCode; }
    public void setBankCode(String bankCode) { this.bankCode = bankCode; }
    public int getBankTranNo() { return bankTranNo; }
    public void setBankTranNo(int bankTranNo) { this.bankTranNo = bankTranNo; }
} 