package com.hustict.aims.model.order;

import com.hustict.aims.model.product.Product;
import jakarta.persistence.*;

@Entity
@Table(name = "orderitem")
public class OrderItem {

    @EmbeddedId
    private OrderItemKey id = new OrderItemKey();

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("orderId") // ánh xạ theo field trong OrderItemKey
    @JoinColumn(name = "orderid")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("productId")
    @JoinColumn(name = "productid")
    private Product product;

    @Column(name = "quantity")
    private int quantity;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private OrderType type;

    public OrderItem() {}

    public OrderItem(Order order, Product product, int quantity, OrderType type) {
        this.order = order;
        this.product = product;
        this.quantity = quantity;
        this.type = type;
        this.id = new OrderItemKey(order.getId(), product.getId());
    }

    // Getters & Setters

    public OrderItemKey getId() { return id; }
    public void setId(OrderItemKey id) { this.id = id; }

    public Order getOrder() { return order; }
    public void setOrder(Order order) {
        this.order = order;
        this.id.setOrderId(order.getId());
    }

    public Product getProduct() { return product; }
    public void setProduct(Product product) {
        this.product = product;
        this.id.setProductId(product.getId());
    }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public OrderType getType() { return type; }
    public void setType(OrderType type) { this.type = type; }

}
