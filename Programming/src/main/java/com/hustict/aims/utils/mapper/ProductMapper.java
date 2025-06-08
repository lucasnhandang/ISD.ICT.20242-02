package com.hustict.aims.utils.mapper;

import com.hustict.aims.dto.product.ProductDetailDTO;
import com.hustict.aims.model.product.*;

import java.util.Map;

public interface ProductMapper<T extends Product, D extends ProductDetailDTO> {
    T fromMap(Map<String, Object> data);
    T updateFromMap(T existing, Map<String, Object> data);
    D toDetailDTO(T product);
}