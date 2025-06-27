package com.hustict.aims.service.placeOrder;
import com.hustict.aims.dto.cart.CartRequestDTO;
import com.hustict.aims.exception.CartAvailabilityException;

import com.hustict.aims.service.ProductService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ProductAvailabilityService {
    // reuseable
    private final ProductService productService;

    public ProductAvailabilityService(ProductService productService) {
        this.productService = productService;
    }

    public void checkCartAvailability(CartRequestDTO cart) {
        List<Map.Entry<Long, Integer>> items = cart.getProductQuantity();
        List<String> unmetProducts = new ArrayList<>();

        for (Map.Entry<Long, Integer> entry : items) {
            Long productId = entry.getKey().longValue(); 
            int quantity = entry.getValue();
            
            boolean available = productService.isProductAvailable(productId, quantity);
            
            
            if (!available) {
                unmetProducts.add("Product ID " + productId + " does not have enough quantity: " + quantity);
            }
        }

        if (!unmetProducts.isEmpty()) {
            throw new CartAvailabilityException(unmetProducts);
        }
    }
}
