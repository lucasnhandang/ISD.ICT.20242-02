package com.hustict.aims.service.factory;

import com.hustict.aims.dto.product.CDDTO;
import com.hustict.aims.dto.product.ProductDTO;
import com.hustict.aims.dto.product.ProductModifyRequest;
import com.hustict.aims.model.product.Product;
import com.hustict.aims.model.product.CD;
import com.hustict.aims.repository.product.CDRepository;
import com.hustict.aims.service.validator.CDValidator;
import com.hustict.aims.utils.mapper.product.CDMapper;
import org.springframework.stereotype.Component;

@Component
public class CDFactory implements ProductFactory {
    private final CDRepository repository;
    private final CDValidator validator;
    private final CDMapper mapper;
    
    public CDFactory(CDRepository repository, CDValidator validator, CDMapper mapper) {
        this.repository = repository;
        this.validator = validator;
        this.mapper = mapper;
    }
    
    @Override
    public String getCategory() {
        return "CD";
    }
    
    @Override
    public ProductDTO createProduct(ProductModifyRequest request) {
        CDDTO cdDTO = (CDDTO) mapper.fromRequest(request);
        
        CD cd = (CD) mapper.toEntity(cdDTO);
        
        validator.validate(cd);
        
        CD savedCD = repository.save(cd);
        
        return mapper.toDTO(savedCD);
    }
    
    @Override
    public ProductDTO updateProduct(Product existing, ProductModifyRequest request) {
        if (!(existing instanceof CD)) {
            throw new IllegalArgumentException("Existing product is not a CD");
        }
        
        CD existingCD = (CD) existing;
        
        CDDTO cdDTO = (CDDTO) mapper.updateFromRequest(existing, request);
        
        mapper.updateEntityFromDTO(cdDTO, existingCD);
        
        validator.validate(existingCD);
        
        CD savedCD = repository.save(existingCD);
        
        return mapper.toDTO(savedCD);
    }
    
    @Override
    public ProductDTO viewProduct(Product product) {
        if (!(product instanceof CD)) {
            throw new IllegalArgumentException("Product is not a CD");
        }
        
        return mapper.toDTO(product);
    }
}