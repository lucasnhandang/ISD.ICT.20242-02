package com.hustict.aims.service.factory;

import com.hustict.aims.dto.product.DVDDetailDTO;
import com.hustict.aims.model.product.DVD;
import com.hustict.aims.repository.product.DVDRepository;
import com.hustict.aims.service.handler.DVDHandler;
import com.hustict.aims.service.validation.DVDValidator;
import com.hustict.aims.utils.mapper.product.DVDMapper;
import org.springframework.stereotype.Component;

@Component
public class DVDFactory implements ProductFactory<DVD, DVDDetailDTO> {
    
    private final DVDRepository dvdRepository;
    private final DVDMapper dvdMapper;
    
    public DVDFactory(DVDRepository dvdRepository, DVDMapper dvdMapper) {
        this.dvdRepository = dvdRepository;
        this.dvdMapper = dvdMapper;
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
    public ProductBundle<DVD, DVDDetailDTO> createBundle() {
        DVDHandler handler = new DVDHandler(dvdRepository, dvdMapper);
        DVDValidator validator = new DVDValidator();
        return new ProductBundle<>(handler, validator, dvdMapper, getProductType());
    }
} 