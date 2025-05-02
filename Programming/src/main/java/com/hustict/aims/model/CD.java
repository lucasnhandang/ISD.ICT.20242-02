package com.hustict.aims.model;

import java.util.List;

public class CD extends Product {
    private List<String> artists;
    private String recordLabel;
    private List<String> trackList;
    private String genre;
    private String releaseDate;

    public CD(String title, int price, int totalQuantity, double weight,
              boolean rushOrderSupported, String imageUrl, String barcode,
              String description, String productDimension,
              List<String> artists, String recordLabel, List<String> trackList,
              String genre, String releaseDate) {
        super(title, price, totalQuantity, weight, rushOrderSupported, imageUrl, barcode, description, productDimension);
        this.artists = artists;
        this.recordLabel = recordLabel;
        this.trackList = trackList;
        this.genre = genre;
        this.releaseDate = releaseDate;
    }

    public List<String> getArtists() { return artists; }
    public String getRecordLabel() { return recordLabel; }
    public List<String> getTrackList() { return trackList; }
    public String getGenre() { return genre; }
    public String getReleaseDate() { return releaseDate; }
}
