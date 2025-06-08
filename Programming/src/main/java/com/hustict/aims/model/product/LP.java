package com.hustict.aims.model.product;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
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

    public LP() {}

    public LP(Product product, String artists, String recordLabel, String trackList, String genre, LocalDate releaseDate) {
        super(product);
        this.artists = artists;
        this.recordLabel = recordLabel;
        this.trackList = trackList;
        this.genre = genre;
        this.releaseDate = releaseDate;
    }

    public String getArtists() { return artists; }
    public void setArtists(String artists) { this.artists = artists; }
    public String getRecordLabel() { return recordLabel; }
    public void setRecordLabel(String recordLabel) { this.recordLabel = recordLabel; }
    public String getTrackList() { return trackList; }
    public void setTrackList(String trackList) { this.trackList = trackList; }
    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }
    public LocalDate getReleaseDate() { return releaseDate; }
    public void setReleaseDate(LocalDate releaseDate) { this.releaseDate = releaseDate; }
} 