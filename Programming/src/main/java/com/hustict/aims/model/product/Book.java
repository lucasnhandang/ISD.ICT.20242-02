package com.hustict.aims.model.product;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@Entity
@Table(name = "book")
public class Book extends Product {
    @Column(name = "authors")
    private String authors;
    
    @Column(name = "covertype")
    private String coverType;
    
    @Column(name = "publisher")
    private String publisher;
    
    @Column(name = "publicationdate")
    private LocalDate publicationDate;
    
    @Column(name = "numpages")
    private Integer pages;
    
    @Column(name = "language")
    private String language;
    
    @Column(name = "genre")
    private String genre;

    public Book() { super(); }

    public Book(Product product, String authors, String coverType, String publisher, Integer pages,
                String language, String genre, LocalDate publicationDate) {
        super(product);
        this.authors = authors;
        this.coverType = coverType;
        this.publisher = publisher;
        this.pages = pages;
        this.language = language;
        this.genre = genre;
        this.publicationDate = publicationDate;
    }

    @Override
    protected void setCategory() {
        this.setCategory("Book");
    }
}