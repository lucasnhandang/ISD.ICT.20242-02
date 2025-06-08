package com.hustict.aims.model;

import jakarta.persistence.*;
import java.time.LocalDate;

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

    public Book() {}

    public Book(Product product, String authors, String coverType, String publisher, Integer pages, String language, String genre, LocalDate publicationDate) {
        super(product);
        this.authors = authors;
        this.coverType = coverType;
        this.publisher = publisher;
        this.pages = pages;
        this.language = language;
        this.genre = genre;
        this.publicationDate = publicationDate;
    }

    public void setAuthors(String authors) { this.authors = authors; }
    public String getAuthors() { return authors; }
    public void setCoverType(String coverType) { this.coverType = coverType; }
    public String getCoverType() { return coverType; }
    public void setPublisher(String publisher) { this.publisher = publisher; }
    public String getPublisher() { return publisher; }
    public void setPages(Integer pages) { this.pages = pages; }
    public Integer getPages() { return pages; }
    public void setLanguage(String language) { this.language = language; }
    public String getLanguage() { return language; }
    public void setGenre(String genre) { this.genre = genre; }
    public String getGenre() { return genre; }
    public void setPublicationDate(LocalDate publicationDate) { this.publicationDate = publicationDate; }
    public LocalDate getPublicationDate() { return publicationDate; }
} 