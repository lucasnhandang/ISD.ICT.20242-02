package com.hustict.aims.service.factory;

import com.hustict.aims.dto.product.DVDDTO;
import com.hustict.aims.dto.product.ProductDTO;
import com.hustict.aims.dto.product.ProductModifyRequest;
import com.hustict.aims.model.product.Product;
import com.hustict.aims.model.product.DVD;
import com.hustict.aims.repository.product.DVDRepository;
import com.hustict.aims.service.validator.DVDValidator;
import com.hustict.aims.utils.mapper.product.DVDMapper;
import org.springframework.stereotype.Component;

@Component
public class DVDFactory implements ProductFactory {
    private final DVDRepository repository;
    private final DVDValidator validator;
    private final DVDMapper mapper;
    
    public DVDFactory(DVDRepository repository, DVDValidator validator, DVDMapper mapper) {
        this.repository = repository;
        this.validator = validator;
        this.mapper = mapper;
    }
    
    @Override
    public String getCategory() {
        return "DVD";
    }
    
    @Override
    public ProductDTO createProduct(ProductModifyRequest request) {
        DVDDTO dvdDTO = (DVDDTO) mapper.fromRequest(request);
        
        DVD dvd = (DVD) mapper.toEntity(dvdDTO);
        
        validator.validate(dvd);
        
        DVD savedDVD = repository.save(dvd);
        
        return mapper.toDTO(savedDVD);
    }
    
    @Override
    public ProductDTO updateProduct(Product existing, ProductModifyRequest request) {
        if (!(existing instanceof DVD)) {
            throw new IllegalArgumentException("Existing product is not a DVD");
        }
        
        DVD existingDVD = (DVD) existing;
        
        DVDDTO dvdDTO = (DVDDTO) mapper.updateFromRequest(existing, request);
        
        mapper.updateEntityFromDTO(dvdDTO, existingDVD);

        validator.validate(existingDVD);
        
        DVD savedDVD = repository.save(existingDVD);
        
        return mapper.toDTO(savedDVD);
    }
    
    @Override
    public ProductDTO viewProduct(Product product) {
        if (!(product instanceof DVD)) {
            throw new IllegalArgumentException("Product is not a DVD");
        }
        
        return mapper.toDTO(product);
    }
} 