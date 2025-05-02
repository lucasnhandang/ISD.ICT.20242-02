package com.hustict.aims.service.validator;

import com.hustict.aims.exception.InvalidProductException;
import com.hustict.aims.model.Product;
import com.hustict.aims.model.Book;

public class BookValidator implements ProductValidator{
    @Override
    public void validate(Product product) throws InvalidProductException {
        Book b = (Book) product;
        if (b.getAuthors() == null || b.getAuthors().isEmpty()
                || b.getCoverType() == null || b.getCoverType().isEmpty()
                || b.getPublisher() == null || b.getPublisher().isEmpty()
                || b.getPublicationDate() == null || b.getPublicationDate().isEmpty()) {
            throw new InvalidProductException("Missing required Book-specific fields.");
        }
    }
}
