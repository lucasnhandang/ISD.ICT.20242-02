package com.hustict.aims.dto.home;

/**
 * DTO for product summary information displayed on home page and search results
 * Follows Data Transfer Object pattern to reduce coupling between layers
 */
public class ProductSummaryDTO {
    private Long id;
    private String title;
    private String category;
    private int currentPrice;
    private int quantity;
    private String imageUrl;

    public ProductSummaryDTO() {}

    public ProductSummaryDTO(Long id, String title, String category, int currentPrice, 
                            int quantity, String imageUrl) {
        this.id = id;
        this.title = title;
        this.category = category;
        this.currentPrice = currentPrice;
        this.quantity = quantity;
        this.imageUrl = imageUrl;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public int getCurrentPrice() { return currentPrice; }
    public void setCurrentPrice(int currentPrice) { this.currentPrice = currentPrice; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
}