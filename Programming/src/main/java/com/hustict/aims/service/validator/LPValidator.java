package com.hustict.aims.service.validator;

import com.hustict.aims.exception.InvalidProductException;
import com.hustict.aims.model.Product;
import com.hustict.aims.model.LP;

public class LPValidator implements ProductValidator {
    @Override
    public void validate(Product product) throws InvalidProductException {
        LP lp = (LP) product;
        if (lp.getArtists() == null || lp.getArtists().isEmpty()
                || lp.getTrackList() == null || lp.getTrackList().isEmpty()
                || lp.getGenre() == null || lp.getGenre().isEmpty()) {
            throw new InvalidProductException("Missing required LP-specific fields.");
        }
    }
}
