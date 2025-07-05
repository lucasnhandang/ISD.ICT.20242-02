package com.hustict.aims.service.rules;

import com.hustict.aims.dto.product.ProductModifyRequest;
import com.hustict.aims.model.user.ActionType;
import com.hustict.aims.repository.ProductChecklogRepository;
import com.hustict.aims.repository.product.ProductRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class UpdatePriceRule implements ProductUpdateRule {
    private final ProductChecklogRepository repo;
    private final ProductRepository productRepo;

    public UpdatePriceRule(ProductChecklogRepository repo, ProductRepository productRepo) {
        this.repo = repo;
        this.productRepo = productRepo;
    }

    @Override
    public void validate(Long userId, Long productId, ProductModifyRequest request) {
        if (request.getProduct() != null) {
            int current = productRepo.getCurrentPrice(productId);
            int incoming = request.getProduct().getCurrentPrice();
            
            boolean isPriceChanged = current != incoming;
            
            if (isPriceChanged) {
                int count = repo.countProductActions(userId, productId, ActionType.UPDATE_PRICE, LocalDate.now());
                if (count >= 2) {
                    throw new IllegalArgumentException("You can't update prices more than twice a day!");
                }
            }
        }
    }
}
