package com.hustict.aims.utils.mapper;

import com.hustict.aims.dto.product.DVDDetailDTO;
import com.hustict.aims.model.product.DVD;
import com.hustict.aims.utils.DateUtils;
import org.springframework.stereotype.Component;
import java.util.Map;

@Component
public class DVDMapper extends AbstractProductMapper<DVD, DVDDetailDTO> {

    @Override
    protected DVD createProduct() {
        return new DVD();
    }

    @Override
    protected void mapSpecFields(DVD dvd, Map<String, Object> data) {
        dvd.setDiscType((String) data.get("discType"));
        dvd.setDirector((String) data.get("director"));
        dvd.setRuntime((int) data.get("runtime"));
        dvd.setStudio((String) data.get("studio"));
        dvd.setLanguage((String) data.get("language"));
        dvd.setSubtitles((String) data.get("subtitles"));
        dvd.setGenre((String) data.get("genre"));
        if (data.get("releaseDate") != null) {
            dvd.setReleaseDate(DateUtils.parseDateNullable(data.get("releaseDate"), "releaseDate"));
        }
    }

    @Override
    protected void updateSpecFields(DVD dvd, Map<String, Object> data) {
        if (data.containsKey("discType")) dvd.setDiscType((String) data.get("discType"));
        if (data.containsKey("director")) dvd.setDirector((String) data.get("director"));
        if (data.containsKey("runtime")) dvd.setRuntime((int) data.get("runtime"));
        if (data.containsKey("studio")) dvd.setStudio((String) data.get("studio"));
        if (data.containsKey("language")) dvd.setLanguage((String) data.get("language"));
        if (data.containsKey("subtitles")) dvd.setSubtitles((String) data.get("subtitles"));
        if (data.containsKey("genre")) dvd.setGenre((String) data.get("genre"));
        if (data.containsKey("releaseDate")) dvd.setReleaseDate(DateUtils.parseDateNullable(data.get("releaseDate"), "releaseDate"));
    }

    @Override
    protected DVDDetailDTO createDetailDTO() {
        return new DVDDetailDTO();
    }
}