package com.hustict.aims.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "productchecklog")
public class ProductChecklog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "checklogid")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "userid", referencedColumnName = "userid")
    private User user;

    @ManyToOne
    @JoinColumn(name = "productid", referencedColumnName = "id")
    private Product product;

    @Column(name = "actiontype")
    private ActionType actionType;

    @Column(name = "checktime")
    private LocalDateTime date;

    public ProductChecklog() {}

    public ProductChecklog(User user, Product product, ActionType actionType, LocalDateTime date) {
        this.user = user;
        this.product = product;
        this.actionType = actionType;
        this.date = date;
    }

    public Long getId() { return id; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }
    public ActionType getActionType() { return actionType; }
    public void setActionType(ActionType actionType) { this.actionType = actionType; }
    public LocalDateTime getDate() { return date; }
    public void setDate(LocalDateTime date) { this.date = date; }
}