package com.hustict.aims.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "orderid")
    private Long id;

    @Column(name = "orderdate")
    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "orderstatus")
    private OrderStatus orderStatus = OrderStatus.PENDING;

    @Column(name = "isrushorder")
    private Boolean isRushOrder;

    @Column(name = "rushdeliverytime")
    private LocalDateTime rushDeliveryTime;

    @Column(name = "rushinstruction")
    private String rushInstruction;

    @Column(name = "currency")
    private String currency = "VND";

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "deliveryinfoid", referencedColumnName = "deliveryinfoid")
    private DeliveryInfo deliveryInfo;

    @OneToOne
    @JoinColumn(name = "invoiceid", referencedColumnName = "invoiceid")
    private Invoice invoice;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();

    public Order() {}

    public Order(LocalDateTime orderDate, OrderStatus orderStatus, Boolean isRushOrder,
                LocalDateTime rushDeliveryTime, String rushInstruction, String currency,
                DeliveryInfo deliveryInfo) {
        this.orderDate = orderDate;
        this.orderStatus = orderStatus;
        this.isRushOrder = isRushOrder;
        this.rushDeliveryTime = rushDeliveryTime;
        this.rushInstruction = rushInstruction;
        this.currency = currency;
        this.deliveryInfo = deliveryInfo;
    }

    public Long getId() { return id; }

    public LocalDateTime getOrderDate() { return orderDate; }
    public void setOrderDate(LocalDateTime orderDate) { this.orderDate = orderDate; }

    public OrderStatus getOrderStatus() { return orderStatus; }
    public void setOrderStatus(OrderStatus orderStatus) { this.orderStatus = orderStatus; }

    public Boolean getIsRushOrder() { return isRushOrder; }
    public void setIsRushOrder(Boolean isRushOrder) { this.isRushOrder = isRushOrder; }

    public LocalDateTime getRushDeliveryTime() { return rushDeliveryTime; }
    public void setRushDeliveryTime(LocalDateTime rushDeliveryTime) { this.rushDeliveryTime = rushDeliveryTime; }

    public String getRushInstruction() { return rushInstruction; }
    public void setRushInstruction(String rushInstruction) { this.rushInstruction = rushInstruction; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public DeliveryInfo getDeliveryInfo() { return deliveryInfo; }
    public void setDeliveryInfo(DeliveryInfo deliveryInfo) { this.deliveryInfo = deliveryInfo; }

    public Invoice getInvoice() { return invoice; }
    public void setInvoice(Invoice invoice) { this.invoice = invoice; }

    public List<OrderItem> getOrderItems() { return orderItems; }
    public void setOrderItems(List<OrderItem> orderItems) { this.orderItems = orderItems; }

    public void addOrderItem(OrderItem item) {
        orderItems.add(item);
        item.setOrder(this);
    }

    public void removeOrderItem(OrderItem item) {
        orderItems.remove(item);
        item.setOrder(null);
    }
}
