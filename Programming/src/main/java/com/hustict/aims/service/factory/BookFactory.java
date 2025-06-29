package com.hustict.aims.service.factory;

import com.hustict.aims.dto.product.BookDetailDTO;
import com.hustict.aims.model.product.Book;
import com.hustict.aims.repository.product.BookRepository;
import com.hustict.aims.service.handler.BookHandler;
import com.hustict.aims.service.validation.BookValidator;
import com.hustict.aims.utils.mapper.product.BookMapper;
import org.springframework.stereotype.Component;

@Component
public class BookFactory implements ProductFactory<Book, BookDetailDTO> {
    
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    
    public BookFactory(BookRepository bookRepository, BookMapper bookMapper) {
        this.bookRepository = bookRepository;
        this.bookMapper = bookMapper;
    }
    
    @Override
    public String getProductType() {
        return "Book";
    }
    
    @Override
    public boolean supports(String productType) {
        return "Book".equalsIgnoreCase(productType);
    }
    
    @Override
    public ProductBundle<Book, BookDetailDTO> createBundle() {
        BookHandler handler = new BookHandler(bookRepository, bookMapper);
        BookValidator validator = new BookValidator();
        return new ProductBundle<>(handler, validator, bookMapper, getProductType());
    }
}