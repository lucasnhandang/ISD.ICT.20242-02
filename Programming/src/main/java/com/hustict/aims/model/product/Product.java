package com.hustict.aims.model.product;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "product")
@Inheritance(strategy = InheritanceType.JOINED)
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "value", nullable = false)
    private int value;

    @Column(name = "currentprice", nullable = false)
    private int currentPrice;

    @Column(name = "barcode", nullable = false)
    private String barcode;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @Column(name = "entrydate", nullable = false)
    private LocalDate entryDate;

    @Column(name = "dimension", nullable = false)
    private String dimension;

    @Column(name = "weight", columnDefinition = "NUMERIC", nullable = false)
    private double weight;

    @Column(name = "rushordersupported")
    private Boolean rushOrderSupported;

    @Column(name = "imageurl")
    private String imageUrl;

    @Column(name = "category", nullable = false, updatable = false)
    private String category;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted;
    public Product() {
        assignCategory();
    }

    public Product(String title, int value, int currentPrice, String barcode, String description,
                int quantity, LocalDate entryDate, String dimension, double weight,
                Boolean rushOrderSupported, String imageUrl) {
        this.title = title;
        this.value = value;
        this.currentPrice = currentPrice;
        this.barcode = barcode;
        this.description = description;
        this.quantity = quantity;
        this.entryDate = entryDate;
        this.dimension = dimension;
        this.weight = weight;
        this.rushOrderSupported = rushOrderSupported;
        this.imageUrl = imageUrl;

        assignCategory();
    }

    public Product(Product otherProduct) {
        this(otherProduct.getTitle(), otherProduct.getValue(), otherProduct.getCurrentPrice(),
            otherProduct.getBarcode(), otherProduct.getDescription(), otherProduct.getQuantity(),
            otherProduct.getEntryDate(), otherProduct.getDimension(), otherProduct.getWeight(),
            otherProduct.getRushOrderSupported(), otherProduct.getImageUrl());
    }

    @PrePersist
    @PreUpdate
    protected void assignCategory() {
        this.category = this.getClass().getSimpleName(); 
    }

    public Long getId() { return id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public int getValue() { return value; }
    public void setValue(int value) { this.value = value; }

    public int getCurrentPrice() { return currentPrice; }
    public void setCurrentPrice(int currentPrice) { this.currentPrice = currentPrice; }

    public String getBarcode() { return barcode; }
    public void setBarcode(String barcode) { this.barcode = barcode; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public LocalDate getEntryDate() { return entryDate; }
    public void setEntryDate(LocalDate entryDate) { this.entryDate = entryDate; }

    public String getDimension() { return dimension; }
    public void setDimension(String dimension) { this.dimension = dimension; }

    public double getWeight() { return weight; }
    public void setWeight(double weight) { this.weight = weight; }

    public Boolean getRushOrderSupported() { return rushOrderSupported; }
    public void setRushOrderSupported(Boolean rushOrderSupported) { this.rushOrderSupported = rushOrderSupported; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public String getCategory() { return category; }

    protected void setCategory(String category) {
        this.category = category;
    }
    public boolean isDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }
}
