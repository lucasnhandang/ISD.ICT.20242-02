package com.hustict.aims.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "product")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "product_type")
public abstract class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    @Column(name = "title")
    private String title;
    
    @Column(name = "value")
    private int value;
    
    @Column(name = "currentprice")
    private int currentPrice;
    
    @Column(name = "barcode")
    private String barcode;
    
    @Column(name = "description")
    private String description;
    
    @Column(name = "quantity")
    private int quantity;
    
    @Column(name = "entrydate")
    private LocalDate entryDate;
    
    @Column(name = "dimension")
    private String dimension;
    
    @Column(name = "weight")
    private double weight;
    
    @Column(name = "rushordersupported")
    private Boolean rushOrderSupported;
    
    @Column(name = "imageurl")
    private String imageUrl;

    public Product() {}

    public Product(String title, int value, int currentPrice, String barcode, String description, int quantity, LocalDate entryDate, String dimension, double weight, Boolean rushOrderSupported, String imageUrl) {
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
    }

    public Product(Product otherProduct) {
        this.title = otherProduct.getTitle();
        this.value = otherProduct.getValue();
        this.currentPrice = otherProduct.getCurrentPrice();
        this.barcode = otherProduct.getBarcode();
        this.description = otherProduct.getDescription();
        this.quantity = otherProduct.getQuantity();
        this.entryDate = otherProduct.getEntryDate();
        this.dimension = otherProduct.getDimension();
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

    public String getDimension() { return dimension; }
    public void setDimension(String dimension) { this.dimension = dimension; }

    public double getWeight() { return weight; }
    public void setWeight(double weight) { this.weight = weight; }

    public Boolean getRushOrderSupported() { return rushOrderSupported; }
    public void setRushOrderSupported(Boolean rushOrderSupported) { this.rushOrderSupported = rushOrderSupported; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public abstract String getCategory();
} 