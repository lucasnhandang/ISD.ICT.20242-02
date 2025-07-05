package com.hustict.aims.dto.product;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProductModifyRequest {
    private ProductDTO product;
    private Map<String, Object> specific;
}