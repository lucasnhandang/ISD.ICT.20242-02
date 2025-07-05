package com.hustict.aims.service.rules;

import com.hustict.aims.dto.product.ProductModifyRequest;

public interface ProductUpdateRule {
    void validate(Long userId, Long productId, ProductModifyRequest request);
}
