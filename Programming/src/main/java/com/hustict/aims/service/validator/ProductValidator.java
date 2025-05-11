/*
 * Cohesion Analysis:
 * - Functional Cohesion: As it defines a single responsibility for product validation
 * - SRP Analysis: No violation as it has a single responsibility of defining validation contract
 */
package com.hustict.aims.service.validator;

import com.hustict.aims.exception.InvalidProductException;
import com.hustict.aims.model.Product;

public interface ProductValidator {
    void validate(Product product) throws InvalidProductException;
}
