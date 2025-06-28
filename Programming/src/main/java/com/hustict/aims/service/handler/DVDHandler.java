package com.hustict.aims.service.handler;

import com.hustict.aims.dto.product.ProductDetailDTO;
import com.hustict.aims.model.product.DVD;
import com.hustict.aims.model.product.Product;
import com.hustict.aims.repository.product.DVDRepository;
import com.hustict.aims.utils.mapper.DVDMapper;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class DVDHandler implements ProductHandler {
    private final DVDRepository dvdRepo;
    private final DVDMapper dvdMapper;

    public DVDHandler(DVDRepository dvdRepo, DVDMapper dvdMapper) {
        this.dvdRepo = dvdRepo;
        this.dvdMapper = dvdMapper;
    }

    @Override
    public String getType() { return "DVD"; }

    @Override
    public boolean supports(String type) { return "DVD".equalsIgnoreCase(type); }

    @Override
    public Product toEntity(Map<String, Object> data) { return dvdMapper.fromMap(data); }

    @Override
    public Product updateEntity(Product existing, Map<String, Object> data) {
        return dvdMapper.updateFromMap((DVD) existing, data);
    }

    @Override
    public ProductDetailDTO saveAndReturnDTO(Product product) {
        DVD saved = dvdRepo.save((DVD) product);
        return dvdMapper.toDetailDTO(saved);
    }
}