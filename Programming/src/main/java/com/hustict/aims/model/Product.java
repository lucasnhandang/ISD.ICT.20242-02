package com.hustict.aims.model;

// The Product class has functional cohesion. 
// It holds the responsibility of managing the descriptive information of a product in the system, and it should only change when the product description is added, removed, or modified.
public class Product {
    private int id;
    private String title;
    private int price;
    private int totalQuantity;
    private double weight;
    private boolean rushOrderSupported;
    private String imageUrl;
    private String barcode;
    private String description;
    private String productDimension;

    // Constructor
     public Product(int id, String title, int price, int totalQuantity, double weight,
                   boolean rushOrderSupported, String imageUrl, String barcode,
                   String description, String productDimension) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.totalQuantity = totalQuantity;
        this.weight = weight;
        this.rushOrderSupported = rushOrderSupported;
        this.imageUrl = imageUrl;
        this.barcode = barcode;
        this.description = description;
        this.productDimension = productDimension;
    }


    // Getters
    public String getTitle() { return title; }
    public int getPrice() { return price; }
    public int getTotalQuantity() { return totalQuantity; }
    public double getWeight() { return weight; }
    public boolean getRushOrderSupported() { return rushOrderSupported; }
    public String getImageUrl() { return imageUrl; }
    public String getBarcode() { return barcode; }
    public String getDescription() { return description; }
    public String getProductDimension() { return productDimension; }
    public int getId() {return id;}

    //Setter
    public void setTotalQuantity(int quantity) {
        this.totalQuantity = quantity;
    }
}
