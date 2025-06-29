package com.hustict.aims.service.factory;

import com.hustict.aims.dto.product.DVDDetailDTO;
import com.hustict.aims.model.product.DVD;
import com.hustict.aims.service.handler.DVDHandler;
import com.hustict.aims.service.validator.DVDValidator;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class DVDFactory implements ProductFactory<DVD, DVDDetailDTO> {
    private final DVDHandler handler;
    private final DVDValidator validator;
    
    public DVDFactory(DVDHandler handler, DVDValidator validator) {
        this.handler = handler;
        this.validator = validator;
    }
    
    @Override
    public String getProductType() {
        return "DVD";
    }
    
    @Override
    public boolean supports(String productType) {
        return "DVD".equalsIgnoreCase(productType);
    }
    
    @Override
    public DVDDetailDTO createProduct(Map<String, Object> data) {
        DVD dvd = (DVD) handler.toEntity(data);
        validator.validate(dvd);
        DVD savedDVD = (DVD) handler.save(dvd);
        return (DVDDetailDTO) handler.toDTO(savedDVD);
    }
    
    @Override
    public DVDDetailDTO updateProduct(DVD existing, Map<String, Object> data) {
        DVD updatedDVD = (DVD) handler.updateEntity(existing, data);
        validator.validate(updatedDVD);
        DVD savedDVD = (DVD) handler.save(updatedDVD);
        return (DVDDetailDTO) handler.toDTO(savedDVD);
    }
    
    @Override
    public DVDDetailDTO viewProduct(DVD product) {
        return (DVDDetailDTO) handler.toDTO(product);
    }
} 