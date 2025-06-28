package com.hustict.aims.model.payment;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "paymenttransaction")
public class PaymentTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transactionid")
    private Long id;
    
    @Column(name = "banktransactionid")
    private String bankTransactionId;
    
    @Column(name = "content")
    private String content;
    
    @Column(name = "paymenttime")
    private LocalDateTime paymentTime;
    
    @Column(name = "paymentamount")
    private int paymentAmount;
    
    @Column(name = "cardtype")
    private String cardType;
    
    @Column(name = "currency")
    private String currency = "VND";

    @Column(name = "systems") 
    private String systems;

    @Column(name = "payment_url", length = 800)
    private String paymentUrl;


    public PaymentTransaction() {}

    public PaymentTransaction(String bankTransactionId, String content, 
                             LocalDateTime paymentTime, int paymentAmount, 
                             String cardType, String currency) {
        this.bankTransactionId = bankTransactionId;
        this.content = content;
        this.paymentTime = paymentTime;
        this.paymentAmount = paymentAmount;
        this.cardType = cardType;
        this.currency = currency;
    }

    public Long getId() { return id; }
    public String getBankTransactionId() { return bankTransactionId; }
    public void setBankTransactionId(String bankTransactionId) { this.bankTransactionId = bankTransactionId; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public LocalDateTime getPaymentTime() { return paymentTime; }
    public void setPaymentTime(LocalDateTime paymentTime) { this.paymentTime = paymentTime; }
    public int getPaymentAmount() { return paymentAmount; }
    public void setPaymentAmount(int paymentAmount) { this.paymentAmount = paymentAmount; }
    public String getCardType() { return cardType; }
    public void setCardType(String cardType) { this.cardType = cardType; }
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