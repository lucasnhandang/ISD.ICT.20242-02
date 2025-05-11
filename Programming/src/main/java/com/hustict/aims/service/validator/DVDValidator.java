/*
 * Cohesion Analysis:
 * - Functional Cohesion: As it has a single responsibility of validating DVD-specific fields
 * - SRP Analysis: No violation as it only handles DVD validation
 */
package com.hustict.aims.service.validator;

import com.hustict.aims.exception.InvalidProductException;
import com.hustict.aims.model.DVD;
import com.hustict.aims.model.Product;

public class DVDValidator implements ProductValidator {
    @Override
    public void validate(Product product) throws InvalidProductException {
        DVD dvd = (DVD) product;
        if (dvd.getDiscType() == null || dvd.getDiscType().isEmpty()
                || dvd.getDirector() == null || dvd.getDirector().isEmpty()
                || dvd.getStudio() == null || dvd.getStudio().isEmpty()
                || dvd.getLanguage() == null || dvd.getLanguage().isEmpty()
                || dvd.getSubtitles() == null || dvd.getSubtitles().isEmpty()) {
            throw new InvalidProductException("Missing required DVD-specific fields.");
        }
    }
}
