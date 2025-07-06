package com.hustict.aims.utils.mapper.product;

import com.hustict.aims.dto.product.ProductDTO;
import com.hustict.aims.dto.product.ProductModifyRequest;
import com.hustict.aims.model.product.Product;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public abstract class BaseProductMapper {
    public ProductDTO toDTO(Product entity) {
        if (entity == null) return null;
        ProductDTO dto = createDTO();
        mapCommonEntityToDto(entity, dto);
        mapSpecificEntityToDto(entity, dto);
        return dto;
    }

    public Product toEntity(ProductDTO dto) {
        if (dto == null) return null;
        Product entity = createEntity();
        mapCommonDtoToEntity(dto, entity);
        mapSpecificDtoToEntity(dto, entity);
        return entity;
    }

    public ProductDTO fromRequest(ProductModifyRequest request) {
        ProductDTO dto = createDTO();
        if (request.getProduct() != null) {
            mapCommonDtoToDto(request.getProduct(), dto);
        }
        if (request.getSpecific() != null) {
            mapSpecificRequestToDto(request.getSpecific(), dto);
        }
        return dto;
    }

    public ProductDTO updateFromRequest(Product existing, ProductModifyRequest request) {
        ProductDTO dto = toDTO(existing);
        if (request.getProduct() != null) {
            mapCommonDtoToDto(request.getProduct(), dto);
        }
        if (request.getSpecific() != null) {
            mapSpecificRequestToDto(request.getSpecific(), dto);
        }
        return dto;
    }

    public void updateEntityFromDTO(ProductDTO dto, Product entity) {
        if (dto == null || entity == null) return;
        mapCommonDtoToEntity(dto, entity);
        mapSpecificDtoToEntity(dto, entity);
    }

    protected void mapCommonEntityToDto(Product entity, ProductDTO dto) {
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setValue(entity.getValue());
        dto.setCurrentPrice(entity.getCurrentPrice());
        dto.setBarcode(entity.getBarcode());
        dto.setDescription(entity.getDescription());
        dto.setQuantity(entity.getQuantity());
        dto.setEntryDate(entity.getEntryDate());
        dto.setDimension(entity.getDimension());
        dto.setWeight(entity.getWeight());
        dto.setRushOrderSupported(entity.getRushOrderSupported());
        dto.setImageUrl(entity.getImageUrl());
        dto.setCategory(entity.getCategory());
    }

    protected void mapCommonDtoToEntity(ProductDTO dto, Product entity) {
        entity.setTitle(dto.getTitle());
        entity.setValue(dto.getValue());
        entity.setCurrentPrice(dto.getCurrentPrice());
        entity.setBarcode(dto.getBarcode());
        entity.setDescription(dto.getDescription());
        entity.setQuantity(dto.getQuantity());
        entity.setEntryDate(dto.getEntryDate());
        entity.setDimension(dto.getDimension());
        entity.setWeight(dto.getWeight());
        entity.setRushOrderSupported(dto.getRushOrderSupported());
        entity.setImageUrl(dto.getImageUrl());
    }

    protected void mapCommonDtoToDto(ProductDTO source, ProductDTO target) {
        if (source.getTitle() != null) target.setTitle(source.getTitle());
        if (source.getValue() != 0) target.setValue(source.getValue());
        if (source.getCurrentPrice() != 0) target.setCurrentPrice(source.getCurrentPrice());
        if (source.getBarcode() != null) target.setBarcode(source.getBarcode());
        if (source.getDescription() != null) target.setDescription(source.getDescription());
        if (source.getQuantity() != 0) target.setQuantity(source.getQuantity());
        if (source.getEntryDate() != null) target.setEntryDate(source.getEntryDate());
        if (source.getDimension() != null) target.setDimension(source.getDimension());
        if (source.getWeight() != 0) target.setWeight(source.getWeight());
        if (source.getRushOrderSupported()!=null) target.setRushOrderSupported(source.getRushOrderSupported());
        if (source.getImageUrl() != null) target.setImageUrl(source.getImageUrl());
    }

    protected abstract ProductDTO createDTO();
    protected abstract Product createEntity();
    protected abstract void mapSpecificEntityToDto(Product entity, ProductDTO dto);
    protected abstract void mapSpecificDtoToEntity(ProductDTO dto, Product entity);
    protected abstract void mapSpecificRequestToDto(Map<String, Object> specific, ProductDTO dto);
}