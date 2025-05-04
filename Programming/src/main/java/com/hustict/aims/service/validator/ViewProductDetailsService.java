package com.hustict.aims.service.validator;

import com.hustict.aims.model.Product;
import com.hustict.aims.model.ProductInfo;

import java.util.HashMap;
import java.util.Map;

public class ViewProductDetailsService {

    // Simulated product repository
    private final Map<Integer, Product> productDatabase = new HashMap<>();
    private final Map<Integer, Boolean> productActiveStatus = new HashMap<>();

    public ViewProductDetailsService() {
        // Simulated data
        Product p1 = new Product("Effective Java", 100000, 20, 0.5, true,
                "img.jpg", "111", "Clean and new", "20x30x2cm");
        productDatabase.put(42, p1);
        productActiveStatus.put(42, true);

        productDatabase.put(87, p1);
        productActiveStatus.put(87, false); // inactive
    }

    public ProductInfo getProductDetails(int productId) {
        if (productId <= 0) {
            throw new IllegalArgumentException("product_id must be a positive integer");
        }

        if (!productDatabase.containsKey(productId)) {
            throw new RuntimeException("Product " + productId + " not found");
        }

        if (!Boolean.TRUE.equals(productActiveStatus.get(productId))) {
            throw new IllegalStateException("Product " + productId + " is inactive");
        }

        return new ProductInfo(productDatabase.get(productId));
    }
}
