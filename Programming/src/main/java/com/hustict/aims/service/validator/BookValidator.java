package com.hustict.aims.service.validator;

import com.hustict.aims.model.product.Book;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class BookValidator extends ProductValidator<Book> {
    @Override
    public String getType() { return "Book"; }

    @Override
    protected Class<Book> getSupportedClass() { return Book.class; }

    @Override
    protected void validateSpecific(Book b, List<String> errs) {
        rejectIfBlank(b.getAuthors(), "Book authors", 100, errs);
        rejectIfBlank(b.getCoverType(), "Book cover type", 50, errs);
        rejectIfBlank(b.getPublisher(), "Book publisher", 100, errs);
        rejectIfNull(b.getPublicationDate(), "Book publication date", errs);
        
        if (b.getPages() != null && b.getPages() <= 0) errs.add("Number of pages must be positive!");
    }
}
