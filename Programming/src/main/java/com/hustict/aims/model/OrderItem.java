package com.hustict.aims.model;

import jakarta.persistence.*;

@Entity
@Table(name = "orderitem")
@IdClass(OrderItemId.class)
public class OrderItem {
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orderid", referencedColumnName = "orderid")
    private Order order;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "productid", referencedColumnName = "id")
    private Product product;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "type")
    private OrderType type;

    public OrderItem() {}

    public OrderItem(Product product, int quantity, OrderType type) {
        this.product = product;
        this.quantity = quantity;
        this.type = type;
    }

    public OrderItem(Order order, Product product, int quantity, OrderType type) {
        this.order = order;
        this.product = product;
        this.quantity = quantity;
        this.type = type;
    }

    public Order getOrder() { return order; }
    public void setOrder(Order order) { this.order = order; }

    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }
    
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public OrderType getType() { return type; } 
    public void setType(OrderType type) { this.type = type; } 
}