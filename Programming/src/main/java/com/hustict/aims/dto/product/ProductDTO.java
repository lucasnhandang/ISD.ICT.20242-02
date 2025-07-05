package com.hustict.aims.dto.product;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
    private Long id;
    private String title;
    private int value;
    private int currentPrice;
    private String barcode;
    private String description;
    private int quantity;
    private LocalDate entryDate;
    private String dimension;
    private double weight;
    private Boolean rushOrderSupported;
    private String imageUrl;
    private String category;
}