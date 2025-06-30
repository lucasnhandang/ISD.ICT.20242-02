package com.hustict.aims.service.rules;

import com.hustict.aims.model.user.ActionType;
import com.hustict.aims.repository.ProductChecklogRepository;
import com.hustict.aims.repository.product.ProductRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Map;

@Component
public class UpdatePriceRule implements ProductUpdateRule {
    private final ProductChecklogRepository repo;
    private final ProductRepository productRepo;

    public UpdatePriceRule(ProductChecklogRepository repo, ProductRepository productRepo) {
        this.repo = repo;
        this.productRepo = productRepo;
    }

    @Override
    public void validate(Long userId, Long productId, Map<String, Object> data) {
        if (data.containsKey("currentPrice")) {
            Integer current = productRepo.getCurrentPrice(productId);
            Integer incoming = Integer.valueOf(data.get("currentPrice").toString());
            if (!incoming.equals(current)) {
                int count = repo.countProductActions(userId, productId, ActionType.UPDATE_PRICE, LocalDate.now());
                if (count >= 2) {
                    throw new IllegalArgumentException("You can't update prices more than twice a day!");
                }
            }
        }
    }
}
