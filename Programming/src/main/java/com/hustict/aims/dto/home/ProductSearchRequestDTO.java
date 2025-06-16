package com.hustict.aims.dto.home;

/**
 * DTO for product search requests
 * Support search by name, sort A-Z by name, and sort by price
 */
public class ProductSearchRequestDTO {
    private String searchQuery;
    private String category;
    private String sortBy = "title";
    private String sortDirection = "asc";
    private int page = 0;
    private int size = 20;

    public ProductSearchRequestDTO() {}

    public ProductSearchRequestDTO(String searchQuery, String category, String sortBy, String sortDirection, int page, int size) {
        this.searchQuery = searchQuery;
        this.category = category;
        this.sortBy = sortBy;
        this.sortDirection = sortDirection;
        this.page = page;
        this.size = size;
    }

    public String getSearchQuery() { return searchQuery; }
    public void setSearchQuery(String searchQuery) { this.searchQuery = searchQuery; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getSortBy() { return sortBy; }
    public void setSortBy(String sortBy) {
        if ("title".equals(sortBy) || "price".equals(sortBy)) {
            this.sortBy = sortBy;
        } else {
            this.sortBy = "title";
        }
    }

    public String getSortDirection() { return sortDirection; }
    public void setSortDirection(String sortDirection) {
        if ("desc".equalsIgnoreCase(sortDirection)) {
            this.sortDirection = "desc";
        } else {
            this.sortDirection = "asc";
        }
    }

    public int getPage() { return page; }
    public void setPage(int page) { this.page = page; }

    public int getSize() { return size; }
    public void setSize(int size) { this.size = size; }
} 