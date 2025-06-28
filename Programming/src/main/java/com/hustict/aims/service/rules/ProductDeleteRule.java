package com.hustict.aims.service.rules;

import java.util.List;

public interface ProductDeleteRule {
    void validate(Long userId, List<Long> productId);
}
