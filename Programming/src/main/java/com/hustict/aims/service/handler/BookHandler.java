package com.hustict.aims.service.handler;

import com.hustict.aims.dto.product.ProductDetailDTO;
import com.hustict.aims.model.product.Book;
import com.hustict.aims.model.product.Product;
import com.hustict.aims.repository.product.BookRepository;
import com.hustict.aims.utils.mapper.BookMapper;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class BookHandler implements ProductHandler {
    private final BookRepository repository;
    private final BookMapper mapper;

    public BookHandler(BookRepository repository, BookMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public boolean supports(String type) { return "Book".equalsIgnoreCase(type); }

    @Override
    public Product toEntity(Map<String, Object> data) { return mapper.fromMap(data); }

    @Override
    public Product updateEntity(Product existing, Map<String, Object> data) {
        return mapper.updateFromMap((Book) existing, data);
    }

    @Override
    public ProductDetailDTO saveAndReturnDTO(Product product) {
        Book saved = repository.save((Book) product);
        return mapper.toDetailDTO(saved);
    }
}
