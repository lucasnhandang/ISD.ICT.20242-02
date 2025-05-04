package com.hustict.aims.model;

import java.time.LocalDate;
import java.util.List;

public class Order {
    private int id;
    private int totalItem;
    private int totalPrice;
    private String currency;
    private Integer invoiceID; // nullable
    private int deliveryInfoID;
    private LocalDate orderDate;
    private String orderStatus;
    private boolean isRushOrder;
    private List<OrderItem> orderItems; // Optional, nếu bạn muốn ánh xạ 2 chiều
    private String rejectionReason = null;

    // Constructor
    public Order(int id, int totalItem, int totalPrgetOrderStatusice, String currency, Integer invoiceID,
                 int deliveryInfoID, LocalDate orderDate, String orderStatus, boolean isRushOrder) {
        this.id = id;
        this.totalItem = totalItem;
        this.totalPrice = totalPrice;
        this.currency = currency;
        this.invoiceID = invoiceID;
        this.deliveryInfoID = deliveryInfoID;
        this.orderDate = orderDate;
        this.orderStatus = orderStatus;
        this.isRushOrder = isRushOrder;
    }

    // Getters
    public int getOrderID() { return id; }
    public int getTotalItem() { return totalItem; }
    public int getTotalPrice() { return totalPrice; }
    public String getCurrency() { return currency; }
    public Integer getInvoiceID() { return invoiceID; }
    public int getDeliveryInfoID() { return deliveryInfoID; }
    public LocalDate getOrderDate() { return orderDate; }
    public String getOrderStatus() { return orderStatus; }
    public boolean isRushOrder() { return isRushOrder; }
    public String getRejectionReason() {
        return rejectionReason;
    }

    //setter
    public void setOrderStatus(String status) {
        this.orderStatus = status;
    }

    public void setRejectionReason(String reason) {
        this.rejectionReason = reason;
    }

}
