package com.hustict.aims.model;

import java.time.LocalDate;

public class PaymentInfo {
    private String cardNumber;
    private LocalDate expiryDate;
    private String cvv;
    private String cardType;

    public PaymentInfo() {}

    public PaymentInfo(String cardNumber, LocalDate expiryDate, String cvv, String cardType) {
        this.cardNumber = cardNumber;
        this.expiryDate = expiryDate;
        this.cvv = cvv;
        this.cardType = cardType;
    }

    public String getCardNumber() { return cardNumber; }
    public void setCardNumber(String cardNumber) { this.cardNumber = cardNumber; }
    public LocalDate getExpiryDate() { return expiryDate; }
    public void setExpiryDate(LocalDate expiryDate) { this.expiryDate = expiryDate; }
    public String getCvv() { return cvv; }
    public void setCvv(String cvv) { this.cvv = cvv; }
    public String getCardType() { return cardType; }
    public void setCardType(String cardType) { this.cardType = cardType; }
} 