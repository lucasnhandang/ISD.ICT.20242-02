package com.hustict.aims.service.handler;

import com.hustict.aims.dto.product.ProductDetailDTO;
import com.hustict.aims.model.product.CD;
import com.hustict.aims.model.product.Product;
import com.hustict.aims.repository.product.CDRepository;
import com.hustict.aims.utils.mapper.CDMapper;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class CDHandler implements ProductHandler {
    private final CDRepository cdRepo;
    private final CDMapper cdMapper;

    public CDHandler(CDRepository cdRepo, CDMapper cdMapper) {
        this.cdRepo = cdRepo;
        this.cdMapper = cdMapper;
    }

    @Override
    public String getType() { return "CD"; }

    @Override
    public boolean supports(String type) { return "CD".equalsIgnoreCase(type); }

    @Override
    public Product toEntity(Map<String, Object> data) { return cdMapper.fromMap(data); }

    @Override
    public Product updateEntity(Product existing, Map<String, Object> data) {
        return cdMapper.updateFromMap((CD) existing, data);
    }

    @Override
    public ProductDetailDTO saveAndReturnDTO(Product product) {
        CD saved = cdRepo.save((CD) product);
        return cdMapper.toDetailDTO(saved);
    }
}
