package com.hustict.aims.service.handler;

import com.hustict.aims.dto.product.ProductDetailDTO;
import com.hustict.aims.model.product.DVD;
import com.hustict.aims.model.product.Product;
import com.hustict.aims.repository.product.DVDRepository;
import com.hustict.aims.utils.mapper.product.DVDMapper;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class DVDHandler implements ProductHandler {
    private final DVDRepository repository;
    private final DVDMapper dvdMapper;

    public DVDHandler(DVDRepository dvdRepo, DVDMapper dvdMapper) {
        this.repository = dvdRepo;
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
    public Product save(Product product) {
        return repository.save((DVD) product);
    }

    @Override
    public ProductDetailDTO toDTO(Product product) {
        return dvdMapper.toDetailDTO((DVD) product);
    }
}