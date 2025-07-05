package com.hustict.aims.utils.mapper.product;

import com.hustict.aims.dto.product.LPDTO;
import com.hustict.aims.dto.product.ProductDTO;
import com.hustict.aims.model.product.LP;
import com.hustict.aims.model.product.Product;
import com.hustict.aims.utils.DateUtils;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Map;

@Component
public class LPMapper extends BaseProductMapper {
    @Override
    protected ProductDTO createDTO() {
        return new LPDTO();
    }
    
    @Override
    protected Product createEntity() {
        return new LP();
    }

    @Override
    protected void mapSpecificEntityToDto(Product entity, ProductDTO dto) {
        if (!(entity instanceof LP) || !(dto instanceof LPDTO)) {
            return; // Skip if types don't match
        }
        
        LP lp = (LP) entity;
        LPDTO lpDto = (LPDTO) dto;
        
        lpDto.setArtists(lp.getArtists());
        lpDto.setRecordLabel(lp.getRecordLabel());
        lpDto.setTrackList(lp.getTrackList());
        lpDto.setGenre(lp.getGenre());
        lpDto.setReleaseDate(lp.getReleaseDate());
    }

    @Override
    protected void mapSpecificDtoToEntity(ProductDTO dto, Product entity) {
        if (!(dto instanceof LPDTO) || !(entity instanceof LP)) {
            return; // Skip if types don't match
        }
        
        LPDTO lpDto = (LPDTO) dto;
        LP lp = (LP) entity;
        
        lp.setArtists(lpDto.getArtists());
        lp.setRecordLabel(lpDto.getRecordLabel());
        lp.setTrackList(lpDto.getTrackList());
        lp.setGenre(lpDto.getGenre());
        lp.setReleaseDate(lpDto.getReleaseDate());
    }

    @Override
    protected void mapSpecificRequestToDto(Map<String, Object> specific, ProductDTO dto) {
        if (specific == null || !(dto instanceof LPDTO)) return;
        
        LPDTO lpDto = (LPDTO) dto;
        lpDto.setArtists((String) specific.get("artists"));
        lpDto.setRecordLabel((String) specific.get("recordLabel"));
        lpDto.setTrackList((String) specific.get("trackList"));
        lpDto.setGenre((String) specific.get("genre"));
        lpDto.setReleaseDate(DateUtils.parseDateNullable(specific.get("releaseDate"), "releaseDate"));
    }

    // Convenience methods for type-safe usage
    public LPDTO toLPDTO(LP lp) {
        return (LPDTO) toDTO(lp);
    }
    
    public LP toLPEntity(LPDTO lpDto) {
        return (LP) toEntity(lpDto);
    }
    
    public void updateEntityFromDTO(LPDTO lpDto, LP lp) {
        updateEntityFromDTO((ProductDTO) lpDto, (Product) lp);
    }
}
