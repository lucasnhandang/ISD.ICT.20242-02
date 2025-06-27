package com.hustict.aims.dto.cart;
import lombok.Data;


@Data
public class CartItemRequestDTO {
    private Long productID;
    private String productName;
    private int quantity;
    private int price;
    private double weight;
    
    public Long getProductID() {
        return productID;
    }

    public int getQuantity() {
        return quantity;
    }

}