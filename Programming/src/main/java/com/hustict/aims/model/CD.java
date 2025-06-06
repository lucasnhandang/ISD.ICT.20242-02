package com.hustict.aims.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "cd")
public class CD extends Product {
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

    public CD() {}

    public CD(Product product, String artists, String recordLabel, String trackList, String genre, LocalDate releaseDate) {
        super(product);
        this.artists = artists;
        this.recordLabel = recordLabel;
        this.trackList = trackList;
        this.genre = genre;
        this.releaseDate = releaseDate;
    }

    public void setArtists(String artists) { this.artists = artists; }
    public String getArtists() { return artists; }
    public void setRecordLabel(String recordLabel) { this.recordLabel = recordLabel; }
    public String getRecordLabel() { return recordLabel; }
    public void setTrackList(String trackList) { this.trackList = trackList; }
    public String getTrackList() { return trackList; }
    public void setGenre(String genre) { this.genre = genre; }
    public String getGenre() { return genre; }
    public void setReleaseDate(LocalDate releaseDate) { this.releaseDate = releaseDate; }
    public LocalDate getReleaseDate() { return releaseDate; }

    @Override
    public String getCategory() { return "CD"; }
} 