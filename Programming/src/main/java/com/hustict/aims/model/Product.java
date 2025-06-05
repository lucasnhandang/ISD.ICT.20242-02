package com.hustict.aims.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "product_type")
public abstract class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private int value;
    private int currentPrice;
    private String barcode;
    private String description;
    private int quantity;
    private LocalDate entryDate;
    private String productDimension;
    private double weight;
    private Boolean rushOrderSupported;
    private String imageUrl;

    public Product() {}

    public Product(String title, int value, int currentPrice, String barcode, String description, int quantity, LocalDate entryDate, String productDimension, double weight, Boolean rushOrderSupported, String imageUrl) {
        this.title = title;
        this.value = value;
        this.currentPrice = currentPrice;
        this.barcode = barcode;
        this.description = description;
        this.quantity = quantity;
        this.entryDate = entryDate;
        this.productDimension = productDimension;
        this.weight = weight;
        this.rushOrderSupported = rushOrderSupported;
        this.imageUrl = imageUrl;
    }

    public Product(Product otherProduct) {
        this.title = otherProduct.getTitle();
        this.value = otherProduct.getValue();
        this.currentPrice = otherProduct.getCurrentPrice();
        this.barcode = otherProduct.getBarcode();
        this.description = otherProduct.getDescription();
        this.quantity = otherProduct.getQuantity();
        this.entryDate = otherProduct.getEntryDate();
        this.productDimension = otherProduct.getProductDimension();
        this.weight = otherProduct.getWeight();
        this.rushOrderSupported = otherProduct.getRushOrderSupported();
        this.imageUrl = otherProduct.getImageUrl();
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

    public String getProductDimension() { return productDimension; }
    public void setProductDimension(String productDimension) { this.productDimension = productDimension; }

    public double getWeight() { return weight; }
    public void setWeight(double weight) { this.weight = weight; }

    public Boolean getRushOrderSupported() { return rushOrderSupported; }
    public void setRushOrderSupported(Boolean rushOrderSupported) { this.rushOrderSupported = rushOrderSupported; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public abstract String getCategory();
} 