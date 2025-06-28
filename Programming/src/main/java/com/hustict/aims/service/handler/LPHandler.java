package com.hustict.aims.service.handler;

import com.hustict.aims.dto.product.ProductDetailDTO;
import com.hustict.aims.model.product.LP;
import com.hustict.aims.model.product.Product;
import com.hustict.aims.repository.product.LPRepository;
import com.hustict.aims.utils.mapper.LPMapper;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class LPHandler implements ProductHandler {
    private final LPRepository lpRepo;
    private final LPMapper lpMapper;

    public LPHandler(LPRepository lpRepo, LPMapper lpMapper) {
        this.lpRepo = lpRepo;
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
    public ProductDetailDTO saveAndReturnDTO(Product product) {
        LP saved = lpRepo.save((LP) product);
        return lpMapper.toDetailDTO(saved);
    }
}