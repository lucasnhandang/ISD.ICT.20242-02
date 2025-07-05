package com.hustict.aims.model.invoice;

import com.hustict.aims.model.payment.PaymentTransaction;
import com.hustict.aims.model.payment.RefundTransaction;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
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
} 