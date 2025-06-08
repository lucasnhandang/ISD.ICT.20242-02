package com.hustict.aims.dto.home;

/**
 * DTO for product search requests
 * Support search by name, sort A-Z by name, and sort by price
 */
public class ProductSearchRequestDTO {
    private String searchQuery; // Search by product name
    private String sortBy = "title"; // "title" for name, "price" for price
    private String sortDirection = "asc"; // "asc" for A-Z or low-high, "desc" otherwise
    private int page = 0;
    private int size = 20;

    public ProductSearchRequestDTO() {}

    public ProductSearchRequestDTO(String searchQuery, String sortBy, String sortDirection, 
                                    int page, int size) {
        this.searchQuery = searchQuery;
        this.sortBy = sortBy;
        this.sortDirection = sortDirection;
        this.page = page;
        this.size = size;
    }

    // Getters and Setters
    public String getSearchQuery() { return searchQuery; }
    public void setSearchQuery(String searchQuery) { this.searchQuery = searchQuery; }

    public String getSortBy() { return sortBy; }
    public void setSortBy(String sortBy) { 
        // Validate sort by options
        if ("title".equals(sortBy) || "price".equals(sortBy)) {
            this.sortBy = sortBy;
        } else {
            this.sortBy = "title"; 
        }
    }

    public String getSortDirection() { return sortDirection; }
    public void setSortDirection(String sortDirection) { 
        // Validate sort direction
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