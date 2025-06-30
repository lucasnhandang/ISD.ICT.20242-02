package com.hustict.aims.service.factory;

import com.hustict.aims.dto.product.CDDetailDTO;
import com.hustict.aims.model.product.CD;
import com.hustict.aims.service.handler.CDHandler;
import com.hustict.aims.service.validator.CDValidator;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class CDFactory implements ProductFactory<CD, CDDetailDTO> {
    private final CDHandler handler;
    private final CDValidator validator;
    
    public CDFactory(CDHandler handler, CDValidator validator) {
        this.handler = handler;
        this.validator = validator;
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
    public CDDetailDTO createProduct(Map<String, Object> data) {
        CD cd = (CD) handler.toEntity(data);
        validator.validate(cd);
        CD savedCD = (CD) handler.save(cd);
        return (CDDetailDTO) handler.toDTO(savedCD);
    }
    
    @Override
    public CDDetailDTO updateProduct(CD existing, Map<String, Object> data) {
        CD updatedCD = (CD) handler.updateEntity(existing, data);
        validator.validate(updatedCD);
        CD savedCD = (CD) handler.save(updatedCD);
        return (CDDetailDTO) handler.toDTO(savedCD);
    }
    
    @Override
    public CDDetailDTO viewProduct(CD product) {
        return (CDDetailDTO) handler.toDTO(product);
    }
}