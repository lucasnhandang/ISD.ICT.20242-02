package com.hustict.aims.model.payment;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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
} 