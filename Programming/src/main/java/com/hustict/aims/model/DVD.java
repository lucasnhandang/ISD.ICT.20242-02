package com.hustict.aims.model;

import java.time.LocalDate;

public class DVD extends Product {
    private String discType;
    private String director;
    private int runtime;
    private String studio;
    private String language;
    private String subtitles;
    private String genre;
    private LocalDate releaseDate;

    public DVD() {}

    public DVD(Long id, String productDimension, int totalQuantity, String description, Boolean rushOrderSupported, String imageUrl, String barcode, String title, double weight, int price,
               String discType, String director, int runtime, String studio, String language, String subtitles, String genre, LocalDate releaseDate) {
        super(id, productDimension, totalQuantity, description, rushOrderSupported, imageUrl, barcode, title, weight, price);
        this.discType = discType;
        this.director = director;
        this.runtime = runtime;
        this.studio = studio;
        this.language = language;
        this.subtitles = subtitles;
        this.genre = genre;
        this.releaseDate = releaseDate;
    }

    public String getDiscType() { return discType; }
    public void setDiscType(String discType) { this.discType = discType; }
    public String getDirector() { return director; }
    public void setDirector(String director) { this.director = director; }
    public int getRuntime() { return runtime; }
    public void setRuntime(int runtime) { this.runtime = runtime; }
    public String getStudio() { return studio; }
    public void setStudio(String studio) { this.studio = studio; }
    public String getLanguage() { return language; }
    public void setLanguage(String language) { this.language = language; }
    public String getSubtitles() { return subtitles; }
    public void setSubtitles(String subtitles) { this.subtitles = subtitles; }
    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }
    public LocalDate getReleaseDate() { return releaseDate; }
    public void setReleaseDate(LocalDate releaseDate) { this.releaseDate = releaseDate; }
}