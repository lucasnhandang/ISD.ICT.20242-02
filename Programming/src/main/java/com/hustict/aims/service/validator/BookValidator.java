package com.hustict.aims.service.validator;

import com.hustict.aims.model.product.Book;
import com.hustict.aims.model.product.Product;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class BookValidator extends ProductValidator {
    @Override
    protected void validateSpecific(Product product, List<String> errors) {
        if (!(product instanceof Book)) {
            errors.add("Invalid product type for Book validator");
            return;
        }
        
        Book book = (Book) product;
        rejectIfBlank(book.getAuthors(), "Book authors", 100, errors);
        rejectIfBlank(book.getCoverType(), "Book cover type", 50, errors);
        rejectIfBlank(book.getPublisher(), "Book publisher", 100, errors);
        rejectIfNull(book.getPublicationDate(), "Book publication date", errors);
        
        if (book.getPages() != null && book.getPages() <= 0) {
            errors.add("Number of pages must be positive!");
        }
    }
}
