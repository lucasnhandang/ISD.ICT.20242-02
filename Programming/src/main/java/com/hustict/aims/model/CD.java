package com.hustict.aims.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "CD")
public class CD extends Product {
    private List<String> artists;
    private String recordLabel;
    private List<String> trackList;
    private String genre;
    private LocalDate releaseDate;

    public CD() {}

    public CD(Product product, List<String> artists, String recordLabel, List<String> trackList, String genre, LocalDate releaseDate) {
        super(product);
        this.artists = artists;
        this.recordLabel = recordLabel;
        this.trackList = trackList;
        this.genre = genre;
        this.releaseDate = releaseDate;
    }

    public void setArtists(List<String> artists) { this.artists = artists; }
    public List<String> getArtists() { return artists; }
    public void setRecordLabel(String recordLabel) { this.recordLabel = recordLabel; }
    public String getRecordLabel() { return recordLabel; }
    public void setTrackList(List<String> trackList) { this.trackList = trackList; }
    public List<String> getTrackList() { return trackList; }
    public void setGenre(String genre) { this.genre = genre; }
    public String getGenre() { return genre; }
    public void setReleaseDate(LocalDate releaseDate) { this.releaseDate = releaseDate; }
    public LocalDate getReleaseDate() { return releaseDate; }

    @Override
    public String getCategory() { return "CD"; }
} 