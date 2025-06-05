package com.hustict.aims.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "Book")
public class Book extends Product {
    private List<String> authors;
    private String coverType;
    private String publisher;
    private LocalDate publicationDate;
    private Integer pages;
    private String language;
    private String genre;

    public Book() {}

    public Book(Product product, List<String> authors, String coverType, String publisher, Integer pages, String language, String genre, LocalDate publicationDate) {
        super(product);
        this.authors = authors;
        this.coverType = coverType;
        this.publisher = publisher;
        this.pages = pages;
        this.language = language;
        this.genre = genre;
        this.publicationDate = publicationDate;
    }

    public void setAuthors(List<String> authors) { this.authors = authors; }
    public List<String> getAuthors() { return authors; }
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

    @Override
    public String getCategory() { return "Book"; }
} 