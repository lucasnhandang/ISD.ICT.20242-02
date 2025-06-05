package com.hustict.aims.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "DVD")
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

    public DVD(Product product, String discType, String director, int runtime, String studio, String language, String subtitles, String genre, LocalDate releaseDate) {
        super(product);
        this.discType = discType;
        this.director = director;
        this.runtime = runtime;
        this.studio = studio;
        this.language = language;
        this.subtitles = subtitles;
        this.genre = genre;
        this.releaseDate = releaseDate;
    }

    public void setDiscType(String discType) { this.discType = discType; }
    public String getDiscType() { return discType; }
    public void setDirector(String director) { this.director = director; }
    public String getDirector() { return director; }
    public void setRuntime(int runtime) { this.runtime = runtime; }
    public int getRuntime() { return runtime; }
    public void setStudio(String studio) { this.studio = studio; }
    public String getStudio() { return studio; }
    public void setLanguage(String language) { this.language = language; }
    public String getLanguage() { return language; }
    public void setSubtitles(String subtitles) { this.subtitles = subtitles; }
    public String getSubtitles() { return subtitles; }
    public void setGenre(String genre) { this.genre = genre; }
    public String getGenre() { return genre; }
    public void setReleaseDate(LocalDate releaseDate) { this.releaseDate = releaseDate; }
    public LocalDate getReleaseDate() { return releaseDate; }

    @Override
    public String getCategory() { return "DVD"; }
}