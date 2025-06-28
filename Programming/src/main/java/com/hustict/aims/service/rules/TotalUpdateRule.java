package com.hustict.aims.service.rules;

import com.hustict.aims.model.user.ActionType;
import com.hustict.aims.repository.ProductChecklogRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Component
public class TotalUpdateRule implements ProductUpdateRule {
    private final ProductChecklogRepository repo;

    public TotalUpdateRule(ProductChecklogRepository repo) {
        this.repo = repo;
    }

    @Override
    public void validate(Long userId, Long productId, Map<String, Object> data) {
        List<ActionType> updateActions = List.of(ActionType.UPDATE, ActionType.UPDATE_PRICE);
        int count = repo.countUserActions(userId, updateActions, LocalDate.now());
        if (count > 30) {
            throw new IllegalArgumentException("You can't update more than 30 products per day!");
        }
    }
}
