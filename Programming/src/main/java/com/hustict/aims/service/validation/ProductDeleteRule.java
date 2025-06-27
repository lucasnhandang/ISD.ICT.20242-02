package com.hustict.aims.service.validation;

import java.util.List;
import java.util.Map;

public interface ProductDeleteRule {
    void validate(Long userId, List<Long> productId);
}
