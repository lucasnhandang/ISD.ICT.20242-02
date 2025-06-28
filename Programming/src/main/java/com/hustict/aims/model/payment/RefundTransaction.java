package com.hustict.aims.model.payment;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "refundtransaction")
public class RefundTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "refundid")
    private Long id;
    
    @Column(name = "banktransactionid")
    private String bankTransactionId;
    
    @Column(name = "refundtime")
    private LocalDateTime refundTime;
    
    @Column(name = "orderamount")
    private int orderAmount;
    
    @Column(name = "currency")
    private String currency = "VND";

    @Column(name = "systems") 
    private String systems;

    @Column(name = "payment_url", length = 255)
    private String paymentUrl;


    
    public RefundTransaction() {}

    public RefundTransaction(String bankTransactionId, LocalDateTime refundTime, 
                            int orderAmount, String currency) {
        this.bankTransactionId = bankTransactionId;
        this.refundTime = refundTime;
        this.orderAmount = orderAmount;
        this.currency = currency;
    }

    public Long getId() { return id; }
    public String getBankTransactionId() { return bankTransactionId; }
    public void setBankTransactionId(String bankTransactionId) { this.bankTransactionId = bankTransactionId; }
    public LocalDateTime getRefundTime() { return refundTime; }
    public void setRefundTime(LocalDateTime refundTime) { this.refundTime = refundTime; }
    public int getOrderAmount() { return orderAmount; }
    public void setOrderAmount(int orderAmount) { this.orderAmount = orderAmount; }
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
    public String getSystems() {
        return systems;
    }

    public void setSystems(String systems) {
        this.systems = systems;
    }
    public String getPaymentUrl() {
        return paymentUrl;
    }

    public void setPaymentUrl(String paymentUrl) {
        this.paymentUrl = paymentUrl;
    }
} 