/*
 * Cohesion Analysis:
 * - Functional Cohesion: As it has a single responsibility of creating appropriate validators
 * - SRP Analysis: No violation as it follows Factory pattern with single responsibility
 */
package com.hustict.aims.service.validator;

import com.hustict.aims.model.Book;
import com.hustict.aims.model.CD;
import com.hustict.aims.model.DVD;
import com.hustict.aims.model.LP;
import com.hustict.aims.model.Product;

public class ProductValidatorFactory {
    public static ProductValidator getValidator(Product product) {
        if (product instanceof Book) return new BookValidator();
        if (product instanceof CD) return new CDValidator();
        if (product instanceof DVD) return new DVDValidator();
        if (product instanceof LP) return new LPValidator();
        throw new IllegalArgumentException("No validator found for product type: " + product.getClass().getSimpleName());
    }
}
