package com.hustict.aims.utils.mapper.product;

import com.hustict.aims.dto.product.CDDTO;
import com.hustict.aims.dto.product.ProductDTO;
import com.hustict.aims.model.product.CD;
import com.hustict.aims.model.product.Product;
import com.hustict.aims.utils.DateUtils;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Map;

@Component
public class CDMapper extends BaseProductMapper {
    @Override
    protected ProductDTO createDTO() {
        return new CDDTO();
    }
    
    @Override
    protected Product createEntity() {
        return new CD();
    }

    @Override
    protected void mapSpecificEntityToDto(Product entity, ProductDTO dto) {
        if (!(entity instanceof CD) || !(dto instanceof CDDTO)) {
            return; // Skip if types don't match
        }
        
        CD cd = (CD) entity;
        CDDTO cdDto = (CDDTO) dto;
        
        cdDto.setArtists(cd.getArtists());
        cdDto.setRecordLabel(cd.getRecordLabel());
        cdDto.setTrackList(cd.getTrackList());
        cdDto.setGenre(cd.getGenre());
        cdDto.setReleaseDate(cd.getReleaseDate());
    }

    @Override
    protected void mapSpecificDtoToEntity(ProductDTO dto, Product entity) {
        if (!(dto instanceof CDDTO) || !(entity instanceof CD)) {
            return; // Skip if types don't match
        }
        
        CDDTO cdDto = (CDDTO) dto;
        CD cd = (CD) entity;
        
        cd.setArtists(cdDto.getArtists());
        cd.setRecordLabel(cdDto.getRecordLabel());
        cd.setTrackList(cdDto.getTrackList());
        cd.setGenre(cdDto.getGenre());
        cd.setReleaseDate(cdDto.getReleaseDate());
    }

    @Override
    protected void mapSpecificRequestToDto(Map<String, Object> specific, ProductDTO dto) {
        if (specific == null || !(dto instanceof CDDTO)) return;
        
        CDDTO cdDto = (CDDTO) dto;
        cdDto.setArtists((String) specific.get("artists"));
        cdDto.setRecordLabel((String) specific.get("recordLabel"));
        cdDto.setTrackList((String) specific.get("trackList"));
        cdDto.setGenre((String) specific.get("genre"));
        cdDto.setReleaseDate(DateUtils.parseDateNullable(specific.get("releaseDate"), "releaseDate"));
    }

    // Convenience methods for type-safe usage
    public CDDTO toCDDTO(CD cd) {
        return (CDDTO) toDTO(cd);
    }
    
    public CD toCDEntity(CDDTO cdDto) {
        return (CD) toEntity(cdDto);
    }
    
    public void updateEntityFromDTO(CDDTO cdDto, CD cd) {
        updateEntityFromDTO((ProductDTO) cdDto, (Product) cd);
    }
}
