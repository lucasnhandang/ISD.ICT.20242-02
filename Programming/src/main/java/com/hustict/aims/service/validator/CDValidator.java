package com.hustict.aims.service.validator;

import com.hustict.aims.exception.InvalidProductException;
import com.hustict.aims.model.Product;
import com.hustict.aims.model.CD;

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
