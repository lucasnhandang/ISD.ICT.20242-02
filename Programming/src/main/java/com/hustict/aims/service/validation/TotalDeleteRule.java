package com.hustict.aims.service.validation;

import com.hustict.aims.model.user.ActionType;
import com.hustict.aims.repository.ProductChecklogRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class TotalDeleteRule implements ProductDeleteRule {
    private final ProductChecklogRepository repo;

    TotalDeleteRule(ProductChecklogRepository repo) {
        this.repo = repo;
    }

    @Override
    public void validate (Long userId, List<Long> productIds) {
        int countDelete = repo.countUserActions(userId, List.of(ActionType.DELETE), LocalDate.now());

        int total = countDelete + productIds.size();

        if (total > 30) {
            throw new IllegalArgumentException("You can't delete more than 30 products per day!");
        }
    }
}
