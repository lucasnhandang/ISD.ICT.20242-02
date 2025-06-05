package com.hustict.aims.model;

import java.time.LocalDateTime;

public class RefundTransaction {
    private Long id;
    private LocalDateTime refundDate;
    private int orderAmount;
    private String bankTranID;
    private String currency;
    private PaymentTransaction paymentTransaction;

    public RefundTransaction() {}

    public RefundTransaction(Long id, LocalDateTime refundDate, int orderAmount, String bankTranID, String currency, PaymentTransaction paymentTransaction) {
        this.id = id;
        this.refundDate = refundDate;
        this.orderAmount = orderAmount;
        this.bankTranID = bankTranID;
        this.currency = currency;
        this.paymentTransaction = paymentTransaction;
    }

    public Long getId() { return id; }
    public LocalDateTime getRefundDate() { return refundDate; }
    public void setRefundDate(LocalDateTime refundDate) { this.refundDate = refundDate; }
    public int getOrderAmount() { return orderAmount; }
    public void setOrderAmount(int orderAmount) { this.orderAmount = orderAmount; }
    public String getBankTranID() { return bankTranID; }
    public void setBankTranID(String bankTranID) { this.bankTranID = bankTranID; }
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
    public PaymentTransaction getPaymentTransaction() { return paymentTransaction; }
    public void setPaymentTransaction(PaymentTransaction paymentTransaction) { this.paymentTransaction = paymentTransaction; }

    public RefundTransaction(LocalDateTime refundDate, int orderAmount, String bankTranID, String currency, PaymentTransaction paymentTransaction) {
        this.refundDate = refundDate;
        this.orderAmount = orderAmount;
        this.bankTranID = bankTranID;
        this.currency = currency;
        this.paymentTransaction = paymentTransaction;
    }
} 