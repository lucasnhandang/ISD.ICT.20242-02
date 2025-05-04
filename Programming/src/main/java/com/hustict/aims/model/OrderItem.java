package com.hustict.aims.model;

public class OrderItem {
    private int orderID;
    private int productID;
    private int quantity;
    private int price;

    // Constructor
    public OrderItem(int orderID, int productID, int quantity, int price) {
        this.orderID = orderID;
        this.productID = productID;
        this.quantity = quantity;
        this.price = price;
    }

    // Getters
    public int getOrderID() { return orderID; }
    public int getProductID() { return productID; }
    public int getQuantity() { return quantity; }
    public int getPrice() { return price; }
}
