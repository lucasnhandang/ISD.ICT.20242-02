package com.hustict.aims.service.factory;

import com.hustict.aims.dto.product.BookDTO;
import com.hustict.aims.dto.product.ProductDTO;
import com.hustict.aims.dto.product.ProductModifyRequest;
import com.hustict.aims.model.product.Product;
import com.hustict.aims.model.product.Book;
import com.hustict.aims.repository.product.BookRepository;
import com.hustict.aims.service.validator.BookValidator;
import com.hustict.aims.utils.mapper.product.BookMapper;
import org.springframework.stereotype.Component;

@Component
public class BookFactory implements ProductFactory {
    private final BookRepository repository;
    private final BookValidator validator;
    private final BookMapper mapper;
    
    public BookFactory(BookRepository repository, BookValidator validator, BookMapper mapper) {
        this.repository = repository;
        this.validator = validator;
        this.mapper = mapper;
    }
    
    @Override
    public String getCategory() {
        return "Book";
    }
    
    @Override
    public ProductDTO createProduct(ProductModifyRequest request) {
        BookDTO bookDTO = (BookDTO) mapper.fromRequest(request);
        
        Book book = mapper.toBookEntity(bookDTO);
        
        validator.validate(book);
        
        Book savedBook = repository.save(book);
        
        return mapper.toBookDTO(savedBook);
    }
    
    @Override
    public ProductDTO updateProduct(Product existing, ProductModifyRequest request) {
        if (!(existing instanceof Book)) {
            throw new IllegalArgumentException("Existing product is not a Book");
        }
        
        Book existingBook = (Book) existing;
        
        BookDTO bookDTO = (BookDTO) mapper.updateFromRequest(existing, request);
        
        mapper.updateEntityFromDTO(bookDTO, existingBook);
        
        validator.validate(existingBook);
        
        Book savedBook = repository.save(existingBook);
        
        return mapper.toBookDTO(savedBook);
    }
    
    @Override
    public ProductDTO viewProduct(Product product) {
        if (!(product instanceof Book)) {
            throw new IllegalArgumentException("Product is not a Book");
        }
        
        return mapper.toBookDTO((Book) product);
    }
}