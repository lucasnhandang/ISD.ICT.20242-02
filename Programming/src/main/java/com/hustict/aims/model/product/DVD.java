package com.hustict.aims.model.product;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name = "dvd")
public class DVD extends Product {
    @Column(name = "disctype")
    private String discType;
    
    @Column(name = "director")
    private String director;
    
    @Column(name = "runtime")
    private int runtime;
    
    @Column(name = "studio")
    private String studio;
    
    @Column(name = "language")
    private String language;
    
    @Column(name = "subtitles")
    private String subtitles;
    
    @Column(name = "genre")
    private String genre;
    
    @Column(name = "releasedate")
    private LocalDate releaseDate;

    public DVD() { super(); }

    public DVD(Product product, String discType, String director, int runtime, String studio,
               String language, String subtitles, String genre, LocalDate releaseDate) {
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

    @Override
    protected void setCategory() {
        this.setCategory("DVD");
    }
}