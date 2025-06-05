package com.hustict.aims.model;

import java.time.LocalDateTime;
import java.util.Map;

public class Order {
    private Long id;
    private DeliveryInfo deliveryInfo;
    private Map<Product, Long> productList;
    private OrderStatus status;
    private boolean isRushOrder = false;
    private LocalDateTime orderTime;
    private PaymentTransaction transaction;
    private int totalItem;
    private int totalPrice;
    private String currency = "VND";

    public Order() {}

    public Order(Map<Product, Long> productList, DeliveryInfo deliveryInfo, OrderStatus status, boolean isRushOrder, PaymentTransaction transaction) {
        this.productList = productList;
        this.deliveryInfo = deliveryInfo;
        this.status = status;
        this.isRushOrder = isRushOrder;
        this.transaction = transaction;
        this.orderTime = LocalDateTime.now();
        this.totalItem = calculateTotalItem(productList);
        this.totalPrice = calculateTotalPrice(productList);
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public DeliveryInfo getDeliveryInfo() { return deliveryInfo; }
    public void setDeliveryInfo(DeliveryInfo deliveryInfo) { this.deliveryInfo = deliveryInfo; }
    public Map<Product, Long> getProductList() { return productList; }
    public void setProductList(Map<Product, Long> productList) { this.productList = productList; }
    public OrderStatus getStatus() { return status; }
    public void setStatus(OrderStatus status) { this.status = status; }
    public boolean isRushOrder() { return isRushOrder; }
    public void setRushOrder(boolean rushOrder) { isRushOrder = rushOrder; }
    public LocalDateTime getOrderTime() { return orderTime; }
    public void setOrderTime(LocalDateTime orderTime) { this.orderTime = orderTime; }
    public PaymentTransaction getTransaction() { return transaction; }
    public void setTransaction(PaymentTransaction transaction) { this.transaction = transaction; }
    public int getTotalItem() { return totalItem; }
    public void setTotalItem(int totalItem) { this.totalItem = totalItem; }
    public int getTotalPrice() { return totalPrice; }
    public void setTotalPrice(int totalPrice) { this.totalPrice = totalPrice; }
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public int calculateTotalItem(Map<Product, Long> productList) {
        int total = 0;
        for (Long quantity : productList.values()) {
            total += quantity;
        }
        return total;
    }

    public int calculateTotalPrice(Map<Product, Long> productList) {
        int total = 0;
        for (Map.Entry<Product, Long> entry : productList.entrySet()) {
            total += entry.getKey().getPrice() * entry.getValue();
        }
        return total;
    }
} 