package com.hustict.aims.model;

import java.time.LocalDate;
import java.util.List;

public class Book extends Product {
    private List<String> authors;
    private String coverType;
    private String cancelOrderRepo;
    private LocalDate publicationDate;
    private int pages;
    private String language;
    private String genre;

    public Book() {}

    public Book(Long id, String productDimension, int totalQuantity, String description, Boolean rushOrderSupported, String imageUrl, String barcode, String title, double weight, int price,
                List<String> authors, String coverType, String cancelOrderRepo, LocalDate publicationDate, int pages, String language, String genre) {
        super(id, productDimension, totalQuantity, description, rushOrderSupported, imageUrl, barcode, title, weight, price);
        this.authors = authors;
        this.coverType = coverType;
        this.cancelOrderRepo = cancelOrderRepo;
        this.publicationDate = publicationDate;
        this.pages = pages;
        this.language = language;
        this.genre = genre;
    }

    public List<String> getAuthors() { return authors; }
    public void setAuthors(List<String> authors) { this.authors = authors; }
    public String getCoverType() { return coverType; }
    public void setCoverType(String coverType) { this.coverType = coverType; }
    public String getCancelOrderRepo() { return cancelOrderRepo; }
    public void setCancelOrderRepo(String cancelOrderRepo) { this.cancelOrderRepo = cancelOrderRepo; }
    public LocalDate getPublicationDate() { return publicationDate; }
    public void setPublicationDate(LocalDate publicationDate) { this.publicationDate = publicationDate; }
    public int getPages() { return pages; }
    public void setPages(int pages) { this.pages = pages; }
    public String getLanguage() { return language; }
    public void setLanguage(String language) { this.language = language; }
    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }
} 