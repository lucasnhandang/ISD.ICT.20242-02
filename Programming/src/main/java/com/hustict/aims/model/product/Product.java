package com.hustict.aims.model.product;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Entity
@Table(name = "product")
@Inheritance(strategy = InheritanceType.JOINED)
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Setter
    @Column(name = "title", nullable = false)
    private String title;

    @Setter
    @Column(name = "value", nullable = false)
    private int value;

    @Setter
    @Column(name = "currentprice", nullable = false)
    private int currentPrice;

    @Setter
    @Column(name = "barcode", nullable = false)
    private String barcode;

    @Setter
    @Column(name = "description", nullable = false)
    private String description;

    @Setter
    @Column(name = "quantity", nullable = false)
    private int quantity;

    @Setter
    @Column(name = "entrydate", nullable = false)
    private LocalDate entryDate;

    @Setter
    @Column(name = "dimension", nullable = false)
    private String dimension;

    @Setter
    @Column(name = "weight", columnDefinition = "NUMERIC", nullable = false)
    private double weight;

    @Setter
    @Column(name = "rushordersupported")
    private Boolean rushOrderSupported;

    @Setter
    @Column(name = "imageurl")
    private String imageUrl;

    @Setter(AccessLevel.PROTECTED)
    @Column(name = "category", nullable = false, updatable = false)
    private String category;

    @Setter
    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted;

    public Product() {
        setCategory();
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
        setCategory();
    }

    public Product(Product otherProduct) {
        this(otherProduct.getTitle(), otherProduct.getValue(), otherProduct.getCurrentPrice(),
            otherProduct.getBarcode(), otherProduct.getDescription(), otherProduct.getQuantity(),
            otherProduct.getEntryDate(), otherProduct.getDimension(), otherProduct.getWeight(),
            otherProduct.getRushOrderSupported(), otherProduct.getImageUrl());
    }

    protected void setCategory() {
        this.category = "Product";
    }
}
