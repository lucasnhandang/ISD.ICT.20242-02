package com.hustict.aims.service.factory;

import com.hustict.aims.dto.product.CDDetailDTO;
import com.hustict.aims.model.product.CD;
import com.hustict.aims.repository.product.CDRepository;
import com.hustict.aims.service.handler.CDHandler;
import com.hustict.aims.service.validation.CDValidator;
import com.hustict.aims.utils.mapper.product.CDMapper;
import org.springframework.stereotype.Component;

@Component
public class CDFactory implements ProductFactory<CD, CDDetailDTO> {
    
    private final CDRepository cdRepository;
    private final CDMapper cdMapper;
    
    public CDFactory(CDRepository cdRepository, CDMapper cdMapper) {
        this.cdRepository = cdRepository;
        this.cdMapper = cdMapper;
    }
    
    @Override
    public String getProductType() {
        return "CD";
    }
    
    @Override
    public boolean supports(String productType) {
        return "CD".equalsIgnoreCase(productType);
    }
    
    @Override
    public ProductBundle<CD, CDDetailDTO> createBundle() {
        CDHandler handler = new CDHandler(cdRepository, cdMapper);
        CDValidator validator = new CDValidator();
        return new ProductBundle<>(handler, validator, cdMapper, getProductType());
    }
}