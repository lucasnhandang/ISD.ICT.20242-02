package com.hustict.aims.model.order;

import com.hustict.aims.model.invoice.Invoice;
import com.hustict.aims.model.shipping.DeliveryInfo;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
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

    public void addOrderItem(OrderItem item) {
        orderItems.add(item);
        item.setOrder(this);
    }

    public void removeOrderItem(OrderItem item) {
        orderItems.remove(item);
        item.setOrder(null);
    }
}
