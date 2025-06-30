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
public class LPDTO extends ProductDTO {
    private String artists;
    private String recordLabel;
    private String trackList;
    private String genre;
    private LocalDate releaseDate;
}
