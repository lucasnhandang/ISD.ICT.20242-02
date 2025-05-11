/*
 * Cohesion Analysis:
 * - Coincidental Cohesion: Low cohesion since it's just a wrapper around Product
 * - SRP Analysis: The class violates SRP as...
 *   ...it doesn't add significant value beyond wrapping Product
 * Improvement Suggestions:
 * 1. Consider removing this class entirely and using Product directly
 * 2. If additional product metadata is needed, add meaningful attributes
 * 3. Consider merging with Product class if no additional functionality is needed
 */
package com.hustict.aims.model;

public class ProductInfo {
    private Product product;
    public ProductInfo(Product product) {
        this.product = product;
    }
    public Product getProduct() {
        return product;
    }
}
