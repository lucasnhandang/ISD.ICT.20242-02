package com.hustict.aims.service.rules;

import com.hustict.aims.dto.product.ProductModifyRequest;
import com.hustict.aims.model.product.Product;
import com.hustict.aims.model.user.ActionType;
import org.springframework.stereotype.Component;

@Component
public class PriceUpdateActionStrategy implements ActionTypeStrategy {
    
    @Override
    public ActionType determine(Product existing, ProductModifyRequest request) {
        int oldPrice = existing.getCurrentPrice();
        int newPrice = request.getProduct().getCurrentPrice();
        boolean isPriceUpdate = oldPrice != newPrice;
        return isPriceUpdate ? ActionType.UPDATE_PRICE : ActionType.UPDATE;
    }
} 