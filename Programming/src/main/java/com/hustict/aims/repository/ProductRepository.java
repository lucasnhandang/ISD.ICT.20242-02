/*
 * Cohesion Analysis:
 * - Functional Cohesion: As it defines a single responsibility for product persistence
 * - SRP Analysis: No violation as it has a single responsibility of defining data access contract
 */
package com.hustict.aims.repository;

import com.hustict.aims.exception.DatabaseFailConnectionException;
import com.hustict.aims.model.Product;

public interface ProductRepository {
    boolean isProductExists(String barcode);
    void saveProduct(Product product) throws DatabaseFailConnectionException;
}