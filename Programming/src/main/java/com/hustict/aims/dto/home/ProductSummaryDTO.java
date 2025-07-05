package com.hustict.aims.dto.home;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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