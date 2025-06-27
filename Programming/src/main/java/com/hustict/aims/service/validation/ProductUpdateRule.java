package com.hustict.aims.service.validation;

import java.util.Map;

public interface ProductUpdateRule {
    void validate(Long userId, Long productId, Map<String, Object> data);
}
