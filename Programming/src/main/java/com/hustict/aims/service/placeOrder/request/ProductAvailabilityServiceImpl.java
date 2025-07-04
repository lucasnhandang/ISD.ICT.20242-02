package com.hustict.aims.service.placeOrder.request;

import com.hustict.aims.dto.cart.CartRequestDTO;
import com.hustict.aims.exception.CartAvailabilityException;
import com.hustict.aims.service.product.InventoryService;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ProductAvailabilityServiceImpl implements ProductAvailabilityService {

    private final InventoryService productService;

    public ProductAvailabilityServiceImpl(InventoryService productService) {
        this.productService = productService;
    }

    @Override
    public void checkCartAvailability(CartRequestDTO cart) {
        List<Map.Entry<Long, Integer>> items = cart.getProductQuantity();
        List<String> unmetProducts = new ArrayList<>();

        for (Map.Entry<Long, Integer> entry : items) {
            Long productId = entry.getKey();
            int quantity = entry.getValue();

            // reuseable 
            boolean available = productService.isAvailable(productId, quantity);

            if (!available) {
                unmetProducts.add("Product ID " + productId + " does not have enough quantity: " + quantity);
            }
        }

        if (!unmetProducts.isEmpty()) {
            throw new CartAvailabilityException(unmetProducts);
        }
    }
}
