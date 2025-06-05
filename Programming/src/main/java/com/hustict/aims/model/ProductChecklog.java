package com.hustict.aims.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class ProductChecklog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private Product product;

    private String actionType; // UPDATE, UPDATE_PRICE, ADD
    private LocalDate date;

    public ProductChecklog() {}

    public ProductChecklog(User user, Product product, String actionType, LocalDate date) {
        this.user = user;
        this.product = product;
        this.actionType = actionType;
        this.date = date;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }
    public String getActionType() { return actionType; }
    public void setActionType(String actionType) { this.actionType = actionType; }
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
} 