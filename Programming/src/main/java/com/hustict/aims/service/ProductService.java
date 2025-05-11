/*
 * Cohesion Analysis:
 * - Functional Cohesion: It handles multiple product-related operations
 * - SRP Analysis: Has violation since it handles both stock management and availability checking
 * 
 * Improvement Suggestions:
 * 1. Split into separate services:
 *    - StockManagementService: Handle restock operations
 *    - InventoryService: Handle availability checking
 * 2. Consider adding more stock-related operations:
 *    - Deduct stock
 *    - Get stock level
 *    - Set minimum stock threshold
 */
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
