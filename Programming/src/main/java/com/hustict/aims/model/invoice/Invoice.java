package com.hustict.aims.model;

import jakarta.persistence.*;

@Entity
@Table(name = "invoice")
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "invoiceid")
    private Long id;

    @Column(name = "productpriceexvat")
    private int productPriceExVAT;
    
    @Column(name = "productpriceincvat")
    private int productPriceIncVAT;
    
    @Column(name = "shippingfee")
    private int shippingFee;
    
    @Column(name = "totalamount")
    private int totalAmount;
    
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "transactionid", referencedColumnName = "transactionid")
    private PaymentTransaction paymentTransaction;
    
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "refundid", referencedColumnName = "refundid")
    private RefundTransaction refundTransaction;

    public Invoice() {}

    public Invoice(int productPriceExVAT, int productPriceIncVAT, int shippingFee, 
                   int totalAmount, PaymentTransaction paymentTransaction, 
                   RefundTransaction refundTransaction) {
        this.productPriceExVAT = productPriceExVAT;
        this.productPriceIncVAT = productPriceIncVAT;
        this.shippingFee = shippingFee;
        this.totalAmount = totalAmount;
        this.paymentTransaction = paymentTransaction;
        this.refundTransaction = refundTransaction;
    }

    public Long getId() { return id; }
    public int getProductPriceExVAT() { return productPriceExVAT; }
    public void setProductPriceExVAT(int productPriceExVAT) { this.productPriceExVAT = productPriceExVAT; }
    public int getProductPriceIncVAT() { return productPriceIncVAT; }
    public void setProductPriceIncVAT(int productPriceIncVAT) { this.productPriceIncVAT = productPriceIncVAT; }
    public int getShippingFee() { return shippingFee; }
    public void setShippingFee(int shippingFee) { this.shippingFee = shippingFee; }
    public int getTotalAmount() { return totalAmount; }
    public void setTotalAmount(int totalAmount) { this.totalAmount = totalAmount; }
    public PaymentTransaction getPaymentTransaction() { return paymentTransaction; }
    public void setPaymentTransaction(PaymentTransaction paymentTransaction) { this.paymentTransaction = paymentTransaction; }
    public RefundTransaction getRefundTransaction() { return refundTransaction; }
    public void setRefundTransaction(RefundTransaction refundTransaction) { this.refundTransaction = refundTransaction; }
} 