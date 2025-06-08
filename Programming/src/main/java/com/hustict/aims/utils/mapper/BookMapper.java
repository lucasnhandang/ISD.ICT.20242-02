package com.hustict.aims.utils.mapper;

import com.hustict.aims.dto.product.BookDetailDTO;
import com.hustict.aims.model.product.Book;
import com.hustict.aims.utils.DateUtils;
import org.springframework.stereotype.Component;
import java.util.Map;

@Component
public class BookMapper extends AbstractProductMapper<Book, BookDetailDTO> {
    @Override
    protected Book createProduct() {
        return new Book();
    }

    @Override
    protected void mapSpecFields(Book book, Map<String, Object> data) {
        book.setAuthors((String) data.get("authors"));
        book.setCoverType((String) data.get("coverType"));
        book.setPublisher((String) data.get("publisher"));
        book.setPublicationDate(DateUtils.parseDate(data.get("publicationDate"), "publicationDate"));
        book.setPages((Integer) data.get("pages"));
        book.setLanguage((String) data.get("language"));
        book.setGenre((String) data.get("genre"));
    }

    @Override
    protected void updateSpecFields(Book book, Map<String, Object> data) {
        if (data.containsKey("authors")) book.setAuthors((String) data.get("authors"));
        if (data.containsKey("coverType")) book.setCoverType((String) data.get("coverType"));
        if (data.containsKey("publisher")) book.setPublisher((String) data.get("publisher"));
        if (data.containsKey("publicationDate")) book.setPublicationDate(DateUtils.parseDate(data.get("publicationDate"), "publicationDate"));
        if (data.containsKey("pages")) book.setPages((Integer) data.get("pages"));
        if (data.containsKey("language")) book.setLanguage((String) data.get("language"));
        if (data.containsKey("genre")) book.setGenre((String) data.get("genre"));
    }

    @Override
    protected BookDetailDTO createDetailDTO() {
        return new BookDetailDTO();
    }
}