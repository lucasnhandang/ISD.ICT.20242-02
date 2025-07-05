package com.hustict.aims.service.rules;

import com.hustict.aims.dto.product.ProductModifyRequest;
import com.hustict.aims.model.product.Product;
import com.hustict.aims.model.user.ActionType;

public interface ActionTypeStrategy {
    ActionType determine(Product existing, ProductModifyRequest request);
} 