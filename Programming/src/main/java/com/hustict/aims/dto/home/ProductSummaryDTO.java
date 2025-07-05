package com.hustict.aims.dto.home;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 DTO for product summary information displayed on home page and search results
 Follows Data Transfer Object pattern to reduce coupling between layers
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProductSummaryDTO {
    private Long id;
    private String title;
    private String category;
    private int currentPrice;
    private int quantity;
    private String imageUrl;
}