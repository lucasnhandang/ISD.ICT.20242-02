package com.hustict.aims.model.product;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name = "lp")
public class LP extends Product {
    @Column(name = "artists")
    private String artists;
    
    @Column(name = "recordlabel")
    private String recordLabel;
    
    @Column(name = "tracklist")
    private String trackList;
    
    @Column(name = "genre")
    private String genre;
    
    @Column(name = "releasedate")
    private LocalDate releaseDate;

    public LP() { super(); }

    public LP(Product product, String artists, String recordLabel, String trackList, String genre, LocalDate releaseDate) {
        super(product);
        this.artists = artists;
        this.recordLabel = recordLabel;
        this.trackList = trackList;
        this.genre = genre;
        this.releaseDate = releaseDate;
    }

    @Override
    protected void setCategory() {
        this.setCategory("LP");
    }
} 