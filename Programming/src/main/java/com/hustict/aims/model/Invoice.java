package com.hustict.aims.model;

public class Invoice {
    private Long id;
    private Order order;
    private int shippingFee;
    private int totalPriceIncludeVAT;
    private int totalPriceExcludeVAT;
    private int totalAmount;
    private PaymentTransaction transaction;

    public Invoice() {}

    public Invoice(int shippingFee, Order order) {
        this.shippingFee = shippingFee;
        this.order = order;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Order getOrder() { return order; }
    public void setOrder(Order order) { this.order = order; }
    public int getShippingFee() { return shippingFee; }
    public void setShippingFee(int shippingFee) { this.shippingFee = shippingFee; }
    public int getTotalPriceIncludeVAT() { return totalPriceIncludeVAT; }
    public void setTotalPriceIncludeVAT(int totalPriceIncludeVAT) { this.totalPriceIncludeVAT = totalPriceIncludeVAT; }
    public int getTotalPriceExcludeVAT() { return totalPriceExcludeVAT; }
    public void setTotalPriceExcludeVAT(int totalPriceExcludeVAT) { this.totalPriceExcludeVAT = totalPriceExcludeVAT; }
    public int getTotalAmount() { return totalAmount; }
    public void setTotalAmount(int totalAmount) { this.totalAmount = totalAmount; }
    public PaymentTransaction getTransaction() { return transaction; }
    public void setTransaction(PaymentTransaction transaction) { this.transaction = transaction; }
} 