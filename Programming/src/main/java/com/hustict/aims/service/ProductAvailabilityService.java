package com.hustict.aims.service;
import com.hustict.aims.exception.*;

import com.hustict.aims.model.cart;
/* 
The ProductAvailabilityService has Funtional cohesion. 
All methods and fields in the class contribute to a single task: checking the availability of products in the cart using ProductService
It has only one reason to change: if the structure of order items in Cart change.
 */

public class ProductAvailabilityService {
    private final InventoryService inventoryService;

    public ProductAvailabilityService(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    public void checkCartAvailability(Cart cart) {
        for (CartItem item : cart.getItems()) {
            inventoryService.checkAvailability(item.getProduct(), item.getQuantity());
        }
    }
}
