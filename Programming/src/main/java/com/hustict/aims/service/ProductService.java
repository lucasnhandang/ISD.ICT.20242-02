package com.hustict.aims.service;
import com.hustict.aims.exception.*;

import com.hustict.aims.model.Product;
/*
All methods and fields in the class focus on managing a single product's availability
Its only reason to change would be if the logic for restocking or checking availability changes. 
 */
public class ProductService {

    public void restock(Product product, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Restock quantity must be positive!");
        }
        product.setTotalQuantity(product.getTotalQuantity() + quantity);
    }

    public boolean checkAvailability(Product product, int quantity) {
        if (product.getTotalQuantity() <= 0) {
            throw new OutOfStockException(product.getTitle() + " is out of stock.");
        } else if (product.getTotalQuantity() < quantity) {
            throw new InsufficientStockException("Not enough stock for product " + product.getTitle() + ".");
        } else {
            return true;
        }
    }
}
