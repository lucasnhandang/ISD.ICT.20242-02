package com.hustict.aims.service.factory;

import com.hustict.aims.dto.product.LPDetailDTO;
import com.hustict.aims.model.product.LP;
import com.hustict.aims.service.handler.LPHandler;
import com.hustict.aims.service.validator.LPValidator;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class LPFactory implements ProductFactory<LP, LPDetailDTO> {
    private final LPHandler handler;
    private final LPValidator validator;
    
    public LPFactory(LPHandler handler, LPValidator validator) {
        this.handler = handler;
        this.validator = validator;
    }
    
    @Override
    public String getProductType() {
        return "LP";
    }
    
    @Override
    public boolean supports(String productType) {
        return "LP".equalsIgnoreCase(productType);
    }
    
    @Override
    public LPDetailDTO createProduct(Map<String, Object> data) {
        LP lp = (LP) handler.toEntity(data);
        validator.validate(lp);
        LP savedLP = (LP) handler.save(lp);
        return (LPDetailDTO) handler.toDTO(savedLP);
    }
    
    @Override
    public LPDetailDTO updateProduct(LP existing, Map<String, Object> data) {
        LP updatedLP = (LP) handler.updateEntity(existing, data);
        validator.validate(updatedLP);
        LP savedLP = (LP) handler.save(updatedLP);
        return (LPDetailDTO) handler.toDTO(savedLP);
    }
    
    @Override
    public LPDetailDTO viewProduct(LP product) {
        return (LPDetailDTO) handler.toDTO(product);
    }
} 