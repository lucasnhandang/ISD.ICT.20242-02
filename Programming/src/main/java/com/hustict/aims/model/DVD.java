/*
 * Cohesion Analysis:
 * - Informational Cohesion: As it groups related data and behavior for a DVD
 * - SRP Analysis: No violation as it only represents a DVD entity
 * 
 */
package com.hustict.aims.model;

public class DVD extends Product {
    private String discType;
    private String director;
    private int runtime;
    private String studio;
    private String language;
    private String subtitles;
    private String genre;
    private String releaseDate;

    public DVD(String title, int price, int totalQuantity, double weight,
               boolean rushOrderSupported, String imageUrl, String barcode,
               String description, String productDimension,
               String discType, String director, int runtime, String studio,
               String language, String subtitles, String genre, String releaseDate) {
        super(title, price, totalQuantity, weight, rushOrderSupported, imageUrl, barcode, description, productDimension);
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
    public String getDirector() { return director; }
    public int getRuntime() { return runtime; }
    public String getStudio() { return studio; }
    public String getLanguage() { return language; }
    public String getSubtitles() { return subtitles; }
    public String getGenre() { return genre; }
    public String getReleaseDate() { return releaseDate; }
}
