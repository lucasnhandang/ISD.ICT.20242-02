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

    @Column(name = "payment_url", length = 800)
    private String paymentUrl;
} 