package com.hustict.aims.model;

import java.util.List;

public class Book extends Product {
    private List<String> authors;
    private String coverType;
    private String publisher;
    private String publicationDate;
    private int pages;
    private String language;
    private String genre;

    public Book(String title, int price, int totalQuantity, double weight,
                boolean rushOrderSupported, String imageUrl, String barcode,
                String description, String productDimension,
                List<String> authors, String coverType, String publisher,
                String publicationDate, int pages, String language, String genre) {
        super(title, price, totalQuantity, weight, rushOrderSupported, imageUrl, barcode, description, productDimension);
        this.authors = authors;
        this.coverType = coverType;
        this.publisher = publisher;
        this.publicationDate = publicationDate;
        this.pages = pages;
        this.language = language;
        this.genre = genre;
    }

    public List<String> getAuthors() { return authors; }
    public String getCoverType() { return coverType; }
    public String getPublisher() { return publisher; }
    public String getPublicationDate() { return publicationDate; }
    public int getPages() { return pages; }
    public String getLanguage() { return language; }
    public String getGenre() { return genre; }
}
