package com.hustict.aims.dto.home;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 DTO for product search requests
 Support search/sort A-Z by name, and sort by price
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductSearchRequestDTO {
    private String searchQuery;
    private String category;
    private String sortBy = "title";
    private String sortDirection = "asc";
    private int page = 0;
    private int size = 20;
}