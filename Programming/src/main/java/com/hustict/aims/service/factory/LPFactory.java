package com.hustict.aims.service.factory;

import com.hustict.aims.dto.product.LPDetailDTO;
import com.hustict.aims.model.product.LP;
import com.hustict.aims.repository.product.LPRepository;
import com.hustict.aims.service.handler.LPHandler;
import com.hustict.aims.service.validation.LPValidator;
import com.hustict.aims.utils.mapper.product.LPMapper;
import org.springframework.stereotype.Component;

@Component
public class LPFactory implements ProductFactory<LP, LPDetailDTO> {
    
    private final LPRepository lpRepository;
    private final LPMapper lpMapper;
    
    public LPFactory(LPRepository lpRepository, LPMapper lpMapper) {
        this.lpRepository = lpRepository;
        this.lpMapper = lpMapper;
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
    public ProductBundle<LP, LPDetailDTO> createBundle() {
        LPHandler handler = new LPHandler(lpRepository, lpMapper);
        LPValidator validator = new LPValidator();
        return new ProductBundle<>(handler, validator, lpMapper, getProductType());
    }
} 