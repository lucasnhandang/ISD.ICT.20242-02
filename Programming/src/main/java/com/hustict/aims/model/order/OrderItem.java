package com.hustict.aims.model.order;

import com.hustict.aims.model.product.Product;
import jakarta.persistence.*;
import lombok.Setter;

@Entity
@Table(name = "orderitem")
public class OrderItem {

    @Setter
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

    @Setter
    @Column(name = "quantity")
    private int quantity;

    @Setter
    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private OrderType type;


    @Setter
    @Column(name = "price", nullable = false)
    private int price;

    public OrderItem() {}

    public OrderItem(Order order, Product product, int quantity, OrderType type, int price) {
        this.order = order;
        this.product = product;
        this.quantity = quantity;
        this.type = type;
        this.price = price;
        this.id = new OrderItemKey(order.getId(), product.getId());
    }

    public OrderItemKey getId() { return id; }

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

    public OrderType getType() { return type; }

    public int getPrice() {
        return price;
    }
}
