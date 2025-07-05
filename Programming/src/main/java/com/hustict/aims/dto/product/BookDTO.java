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
public class BookDTO extends ProductDTO {
    private String authors;
    private String coverType;
    private String publisher;
    private LocalDate publicationDate;
    private Integer pages;
    private String language;
    private String genre;
}
