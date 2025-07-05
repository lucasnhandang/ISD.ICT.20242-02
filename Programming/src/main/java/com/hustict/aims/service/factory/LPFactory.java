package com.hustict.aims.service.factory;

import com.hustict.aims.dto.product.LPDTO;
import com.hustict.aims.dto.product.ProductDTO;
import com.hustict.aims.dto.product.ProductModifyRequest;
import com.hustict.aims.model.product.Product;
import com.hustict.aims.model.product.LP;
import com.hustict.aims.repository.product.LPRepository;
import com.hustict.aims.service.validator.LPValidator;
import com.hustict.aims.utils.mapper.product.LPMapper;
import org.springframework.stereotype.Component;

@Component
public class LPFactory implements ProductFactory {
    private final LPRepository repository;
    private final LPValidator validator;
    private final LPMapper mapper;
    
    public LPFactory(LPRepository repository, LPValidator validator, LPMapper mapper) {
        this.repository = repository;
        this.validator = validator;
        this.mapper = mapper;
    }
    
    @Override
    public String getCategory() {
        return "LP";
    }
    
    @Override
    public ProductDTO createProduct(ProductModifyRequest request) {
        LPDTO lpDTO = (LPDTO) mapper.fromRequest(request);
        
        LP lp = (LP) mapper.toEntity(lpDTO);
        
        validator.validate(lp);
        
        LP savedLP = repository.save(lp);
        
        return mapper.toDTO(savedLP);
    }
    
    @Override
    public ProductDTO updateProduct(Product existing, ProductModifyRequest request) {
        if (!(existing instanceof LP)) {
            throw new IllegalArgumentException("Existing product is not a LP");
        }
        
        LP existingLP = (LP) existing;
        
        LPDTO lpDTO = (LPDTO) mapper.updateFromRequest(existing, request);
        
        mapper.updateEntityFromDTO(lpDTO, existingLP);
        
        validator.validate(existingLP);
        
        LP savedLP = repository.save(existingLP);
        
        return mapper.toDTO(savedLP);
    }
    
    @Override
    public ProductDTO viewProduct(Product product) {
        if (!(product instanceof LP)) {
            throw new IllegalArgumentException("Product is not a LP");
        }
        
        return mapper.toDTO(product);
    }
} 