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
public class DVDDTO extends ProductDTO {
    private String discType;
    private String director;
    private int runtime;
    private String studio;
    private String language;
    private String subtitles;
    private String genre;
    private LocalDate releaseDate;
}
