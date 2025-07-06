package com.hustict.aims.dto.home;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProductSearchRequestDTO {
    private String searchQuery;
    private String category;
    private String sortBy = "title";
    private String sortDirection = "asc";
    private int page = 0;
    private int size = 20;

    public ProductSearchRequestDTO(String searchQuery, String category, String sortBy, String sortDirection, int page, int size) {
        this.searchQuery = searchQuery;
        this.category = category;
        this.sortBy = sortBy != null ? sortBy : "title";
        this.sortDirection = sortDirection != null ? sortDirection : "asc";
        this.page = page;
        this.size = size;
    }
}