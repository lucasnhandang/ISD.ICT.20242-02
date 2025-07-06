package com.hustict.aims.service.product;

import com.hustict.aims.dto.product.ProductModifyRequest;
import com.hustict.aims.model.user.ActionType;
import com.hustict.aims.model.user.ProductChecklog;
import com.hustict.aims.repository.ProductChecklogRepository;
import com.hustict.aims.service.rules.ProductDeleteRule;
import com.hustict.aims.service.rules.ProductUpdateRule;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProductActionService {
    private final List<ProductUpdateRule> updateRules;
    private final List<ProductDeleteRule> deleteRules;
    private final ProductChecklogRepository repo;

    public ProductActionService(List<ProductUpdateRule> updateRules,
                                List<ProductDeleteRule> deleteRules,
                                ProductChecklogRepository repo) {
        this.updateRules = updateRules;
        this.deleteRules = deleteRules;
        this.repo = repo;
    }

    public void validateUpdate(Long userId, Long productId, ProductModifyRequest request) {
        for (ProductUpdateRule rule : updateRules) {
            rule.validate(userId, productId, request);
        }
    }

    public void validateDelete(Long userId, List<Long> productIds) {
        for (ProductDeleteRule rule : deleteRules) {
            rule.validate(userId, productIds);
        }
    }

    public void logProductAction(Long userId, Long productId, ActionType actionType) {
        ProductChecklog log = new ProductChecklog(userId, productId, actionType, LocalDateTime.now());
        repo.save(log);
    }
}