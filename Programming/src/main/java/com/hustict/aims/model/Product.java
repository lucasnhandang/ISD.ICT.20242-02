package com.hustict.aims.model;

public abstract class Product {
    private Long id;
    private String productDimension;
    private int totalQuantity;
    private String description;
    private Boolean rushOrderSupported;
    private String imageUrl;
    private String barcode;
    private String title;
    private double weight;
    private int price;

    public Product() {}

    public Product(Long id, String productDimension, int totalQuantity, String description, Boolean rushOrderSupported, String imageUrl, String barcode, String title, double weight, int price) {
        this.id = id;
        this.productDimension = productDimension;
        this.totalQuantity = totalQuantity;
        this.description = description;
        this.rushOrderSupported = rushOrderSupported;
        this.imageUrl = imageUrl;
        this.barcode = barcode;
        this.title = title;
        this.weight = weight;
        this.price = price;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getProductDimension() { return productDimension; }
    public void setProductDimension(String productDimension) { this.productDimension = productDimension; }
    public int getTotalQuantity() { return totalQuantity; }
    public void setTotalQuantity(int totalQuantity) { this.totalQuantity = totalQuantity; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Boolean getRushOrderSupported() { return rushOrderSupported; }
    public void setRushOrderSupported(Boolean rushOrderSupported) { this.rushOrderSupported = rushOrderSupported; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public String getBarcode() { return barcode; }
    public void setBarcode(String barcode) { this.barcode = barcode; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public double getWeight() { return weight; }
    public void setWeight(double weight) { this.weight = weight; }
    public int getPrice() { return price; }
    public void setPrice(int price) { this.price = price; }
} 