/*
 * Cohesion Analysis:
 * - Functional Cohesion: High cohesion as it has a single responsibility of validating CD-specific fields
 * - SRP Analysis: No violation as it only handles CD validation
 */
package com.hustict.aims.service.validator;

import com.hustict.aims.exception.InvalidProductException;
import com.hustict.aims.model.CD;
import com.hustict.aims.model.Product;

public class CDValidator implements ProductValidator{
    @Override
    public void validate(Product product) throws InvalidProductException {
        CD cd = (CD) product;
        if (cd.getArtists() == null || cd.getArtists().isEmpty()
                || cd.getTrackList() == null || cd.getTrackList().isEmpty()
                || cd.getGenre() == null || cd.getGenre().isEmpty()) {
            throw new InvalidProductException("Missing required CD-specific fields.");
        }
    }
}
