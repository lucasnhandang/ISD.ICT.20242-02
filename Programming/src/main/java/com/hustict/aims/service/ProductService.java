package com.hustict.aims.service;
import com.hustict.aims.exception.*;

import com.hustict.aims.model.Product;
/*
The ProductService has Logical cohesion.
Its methods perform different actions (restocking, availability checking) that are logically related to product inventory.
It only changes if any of those specific inventory-related logics change.
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
