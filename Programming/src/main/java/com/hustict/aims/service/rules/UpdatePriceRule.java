package com.hustict.aims.service.rules;

import com.hustict.aims.model.user.ActionType;
import com.hustict.aims.repository.ProductChecklogRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Map;

@Component
public class UpdatePriceRule implements ProductUpdateRule {
    private final ProductChecklogRepository repo;

    public UpdatePriceRule(ProductChecklogRepository repo) {
        this.repo = repo;
    }

    @Override
    public void validate(Long userId, Long productId, Map<String, Object> data) {
        if (data.containsKey("currentPrice")) {
            int count = repo.countProductActions(userId, productId, ActionType.UPDATE_PRICE, LocalDate.now());
            if (count >= 2) {
                throw new IllegalArgumentException("You can't update prices more than twice a day!");
            }
        }
    }
}
