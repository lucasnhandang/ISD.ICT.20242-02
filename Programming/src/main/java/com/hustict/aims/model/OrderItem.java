package com.hustict.aims.model;

import jakarta.persistence.*;

@Entity
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    private int quantity;
    private boolean isRushOrder;

    public OrderItem() {}

    public OrderItem(Order order, Product product, int quantity, boolean isRushOrder) {
        this.order = order;
        this.product = product;
        this.quantity = quantity;
        this.isRushOrder = isRushOrder;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Order getOrder() { return order; }
    public void setOrder(Order order) { this.order = order; }
    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public boolean isRushOrder() { return isRushOrder; }
    public void setIsRushOrder(boolean isRushOrder) { this.isRushOrder = isRushOrder; }
} 