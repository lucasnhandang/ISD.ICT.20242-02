package com.hustict.aims.utils.mapper.product;

import com.hustict.aims.dto.product.BookDTO;
import com.hustict.aims.dto.product.ProductDTO;
import com.hustict.aims.model.product.Book;
import com.hustict.aims.model.product.Product;
import com.hustict.aims.utils.DateUtils;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Map;

@Component
public class BookMapper extends BaseProductMapper {
    @Override
    protected ProductDTO createDTO() {
        return new BookDTO();
    }
    
    @Override
    protected Product createEntity() {
        return new Book();
    }

    @Override
    protected void mapSpecificEntityToDto(Product entity, ProductDTO dto) {
        if (!(entity instanceof Book) || !(dto instanceof BookDTO)) {
            return; // Skip if types don't match
        }
        
        Book book = (Book) entity;
        BookDTO bookDto = (BookDTO) dto;
        
        bookDto.setAuthors(book.getAuthors());
        bookDto.setCoverType(book.getCoverType());
        bookDto.setPublisher(book.getPublisher());
        bookDto.setPublicationDate(book.getPublicationDate());
        bookDto.setPages(book.getPages());
        bookDto.setLanguage(book.getLanguage());
        bookDto.setGenre(book.getGenre());
    }

    @Override
    protected void mapSpecificDtoToEntity(ProductDTO dto, Product entity) {
        if (!(dto instanceof BookDTO) || !(entity instanceof Book)) {
            return; // Skip if types don't match
        }
        
        BookDTO bookDto = (BookDTO) dto;
        Book book = (Book) entity;
        
        book.setAuthors(bookDto.getAuthors());
        book.setCoverType(bookDto.getCoverType());
        book.setPublisher(bookDto.getPublisher());
        book.setPublicationDate(bookDto.getPublicationDate());
        book.setPages(bookDto.getPages());
        book.setLanguage(bookDto.getLanguage());
        book.setGenre(bookDto.getGenre());
    }

    @Override
    protected void mapSpecificRequestToDto(Map<String, Object> specific, ProductDTO dto) {
        if (specific == null || !(dto instanceof BookDTO)) return;
        
        BookDTO bookDto = (BookDTO) dto;
        bookDto.setAuthors((String) specific.get("authors"));
        bookDto.setCoverType((String) specific.get("coverType"));
        bookDto.setPublisher((String) specific.get("publisher"));
        bookDto.setPublicationDate(DateUtils.parseDateNullable(specific.get("publicationDate"), "publicationDate"));
        bookDto.setPages((Integer) specific.get("pages"));
        bookDto.setLanguage((String) specific.get("language"));
        bookDto.setGenre((String) specific.get("genre"));
    }

    // Convenience methods for type-safe usage
    public BookDTO toBookDTO(Book book) {
        return (BookDTO) toDTO(book);
    }
    
    public Book toBookEntity(BookDTO bookDto) {
        return (Book) toEntity(bookDto);
    }
    
    public void updateEntityFromDTO(BookDTO bookDto, Book book) {
        updateEntityFromDTO((ProductDTO) bookDto, (Product) book);
    }
}