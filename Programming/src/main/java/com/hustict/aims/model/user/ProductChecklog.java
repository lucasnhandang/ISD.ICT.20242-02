package com.hustict.aims.model.user;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "productchecklog")
public class ProductChecklog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "checklogid")
    private Long id;

    @Column(name = "userid")
    private Long userId;

    @Column(name = "productid")
    private Long productId;

    @Enumerated(EnumType.STRING)
    @Column(name = "actiontype")
    private ActionType actionType;

    @Column(name = "checktime")
    private LocalDateTime date;

    public ProductChecklog() {}

    public ProductChecklog(Long userId, Long productId, ActionType actionType, LocalDateTime date) {
        this.userId = userId;
        this.productId = productId;
        this.actionType = actionType;
        this.date = date;
    }

    public Long getId() { return id; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }
    public ActionType getActionType() { return actionType; }
    public void setActionType(ActionType actionType) { this.actionType = actionType; }
    public LocalDateTime getDate() { return date; }
    public void setDate(LocalDateTime date) { this.date = date; }
}