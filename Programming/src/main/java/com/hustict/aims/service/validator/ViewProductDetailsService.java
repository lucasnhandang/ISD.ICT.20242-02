package com.hustict.aims.service.validator;

import com.hustict.aims.model.Product;
import com.hustict.aims.model.ProductInfo;

import java.util.HashMap;
import java.util.Map;

/**
 * Cohesion Level: procedural cohesion.
 * It performs multiple loosely related tasks in one place:
    * It initializes and simulates a product database.
    * It contains logic to validate product IDs and check active status.
    * It constructs and returns a ProductInfo instance.
 * These tasks are only sequentially connected, not functionally cohesive (they do not all contribute to a single, well-defined responsibility).
 * SRP Violation: violates as it has multiple responsibilities:
    * Data Initialization: using a hardcoded map.
    * Business Logic: validating product ID, checking activity.
    * Data Transformation: returning "ProductInfo".
 * Solution for Improvement: distribute responsibilities into other classes
 */

public class ViewProductDetailsService {

    // Simulated product repository
    private final Map<Integer, Product> productDatabase = new HashMap<>();
    private final Map<Integer, Boolean> productActiveStatus = new HashMap<>();

    public ViewProductDetailsService() {
        // Simulated data
        Product p1 = new Product(1, "Effective Java", 100000, 20, 0.5, true,
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
