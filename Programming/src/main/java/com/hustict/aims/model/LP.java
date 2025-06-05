package com.hustict.aims.model;

import java.time.LocalDate;
import java.util.List;

public class LP extends Product {
    private List<String> artists;
    private String recordLabel;
    private List<String> trackList;
    private String genre;
    private LocalDate releaseDate;

    public LP() {}

    public LP(Long id, String productDimension, int totalQuantity, String description, Boolean rushOrderSupported, String imageUrl, String barcode, String title, double weight, int price,
              List<String> artists, String recordLabel, List<String> trackList, String genre, LocalDate releaseDate) {
        super(id, productDimension, totalQuantity, description, rushOrderSupported, imageUrl, barcode, title, weight, price);
        this.artists = artists;
        this.recordLabel = recordLabel;
        this.trackList = trackList;
        this.genre = genre;
        this.releaseDate = releaseDate;
    }

    public List<String> getArtists() { return artists; }
    public void setArtists(List<String> artists) { this.artists = artists; }
    public String getRecordLabel() { return recordLabel; }
    public void setRecordLabel(String recordLabel) { this.recordLabel = recordLabel; }
    public List<String> getTrackList() { return trackList; }
    public void setTrackList(List<String> trackList) { this.trackList = trackList; }
    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }
    public LocalDate getReleaseDate() { return releaseDate; }
    public void setReleaseDate(LocalDate releaseDate) { this.releaseDate = releaseDate; }
} 