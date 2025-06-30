package com.hustict.aims.service.factory;

import com.hustict.aims.dto.product.BookDetailDTO;
import com.hustict.aims.model.product.Book;
import com.hustict.aims.service.handler.BookHandler;
import com.hustict.aims.service.validator.BookValidator;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class BookFactory implements ProductFactory<Book, BookDetailDTO> {
    private final BookHandler handler;
    private final BookValidator validator;
    
    public BookFactory(BookHandler handler, BookValidator validator) {
        this.handler = handler;
        this.validator = validator;
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
    public BookDetailDTO createProduct(Map<String, Object> data) {
        Book book = (Book) handler.toEntity(data);
        validator.validate(book);
        Book savedBook = (Book) handler.save(book);
        return (BookDetailDTO) handler.toDTO(savedBook);
    }

    @Override
    public BookDetailDTO updateProduct(Book existing, Map<String, Object> data) {
        Book updatedBook = (Book) handler.updateEntity(existing, data);
        validator.validate(updatedBook);
        Book savedBook = (Book) handler.save(updatedBook);
        return (BookDetailDTO) handler.toDTO(savedBook);
    }
    
    @Override
    public BookDetailDTO viewProduct(Book product) {
        return (BookDetailDTO) handler.toDTO(product);
    }
}