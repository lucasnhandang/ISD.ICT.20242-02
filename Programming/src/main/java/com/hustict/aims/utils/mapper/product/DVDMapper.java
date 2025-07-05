package com.hustict.aims.utils.mapper.product;

import com.hustict.aims.dto.product.DVDDTO;
import com.hustict.aims.dto.product.ProductDTO;
import com.hustict.aims.model.product.DVD;
import com.hustict.aims.model.product.Product;
import com.hustict.aims.utils.DateUtils;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Map;

@Component
public class DVDMapper extends BaseProductMapper {
    @Override
    protected ProductDTO createDTO() {
        return new DVDDTO();
    }
    
    @Override
    protected Product createEntity() {
        return new DVD();
    }

    @Override
    protected void mapSpecificEntityToDto(Product entity, ProductDTO dto) {
        DVD dvd = (DVD) entity;
        DVDDTO dvdDto = (DVDDTO) dto;
        
        dvdDto.setDiscType(dvd.getDiscType());
        dvdDto.setDirector(dvd.getDirector());
        dvdDto.setRuntime(dvd.getRuntime());
        dvdDto.setStudio(dvd.getStudio());
        dvdDto.setLanguage(dvd.getLanguage());
        dvdDto.setSubtitles(dvd.getSubtitles());
        dvdDto.setGenre(dvd.getGenre());
        dvdDto.setReleaseDate(dvd.getReleaseDate());
    }

    @Override
    protected void mapSpecificDtoToEntity(ProductDTO dto, Product entity) {
        DVDDTO dvdDto = (DVDDTO) dto;
        DVD dvd = (DVD) entity;
        
        dvd.setDiscType(dvdDto.getDiscType());
        dvd.setDirector(dvdDto.getDirector());
        dvd.setRuntime(dvdDto.getRuntime());
        dvd.setStudio(dvdDto.getStudio());
        dvd.setLanguage(dvdDto.getLanguage());
        dvd.setSubtitles(dvdDto.getSubtitles());
        dvd.setGenre(dvdDto.getGenre());
        dvd.setReleaseDate(dvdDto.getReleaseDate());
    }

    @Override
    protected void mapSpecificRequestToDto(Map<String, Object> specific, ProductDTO dto) {
        DVDDTO dvdDto = (DVDDTO) dto;
        dvdDto.setDiscType((String) specific.get("discType"));
        dvdDto.setDirector((String) specific.get("director"));
        dvdDto.setRuntime(specific.get("runtime") != null ? (Integer) specific.get("runtime") : 0);
        dvdDto.setStudio((String) specific.get("studio"));
        dvdDto.setLanguage((String) specific.get("language"));
        dvdDto.setSubtitles((String) specific.get("subtitles"));
        dvdDto.setGenre((String) specific.get("genre"));
        dvdDto.setReleaseDate(DateUtils.parseDateNullable(specific.get("releaseDate"), "releaseDate"));
    }
}
