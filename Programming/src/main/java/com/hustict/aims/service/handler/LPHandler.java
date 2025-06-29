package com.hustict.aims.service.handler;

import com.hustict.aims.dto.product.ProductDetailDTO;
import com.hustict.aims.model.product.Book;
import com.hustict.aims.model.product.LP;
import com.hustict.aims.model.product.Product;
import com.hustict.aims.repository.product.LPRepository;
import com.hustict.aims.utils.mapper.product.LPMapper;

import java.util.Map;

public class LPHandler implements ProductHandler {
    private final LPRepository repository;
    private final LPMapper lpMapper;

    public LPHandler(LPRepository lpRepo, LPMapper lpMapper) {
        this.repository = lpRepo;
        this.lpMapper = lpMapper;
    }

    @Override
    public String getType() { return "LP"; }

    @Override
    public boolean supports(String type) { return "LP".equalsIgnoreCase(type); }

    @Override
    public Product toEntity(Map<String, Object> data) { return lpMapper.fromMap(data); }

    @Override
    public Product updateEntity(Product existing, Map<String, Object> data) {
        return lpMapper.updateFromMap((LP) existing, data);
    }

    @Override
    public Product save(Product product) {
        return repository.save((LP) product);
    }

    @Override
    public ProductDetailDTO toDTO(Product product) {
        return lpMapper.toDetailDTO((LP) product);
    }
}