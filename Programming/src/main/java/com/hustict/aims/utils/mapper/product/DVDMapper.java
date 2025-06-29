package com.hustict.aims.utils.mapper.product;

import com.hustict.aims.dto.product.DVDDetailDTO;
import com.hustict.aims.model.product.DVD;
import com.hustict.aims.utils.DateUtils;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class DVDMapper extends ProductMapper<DVD, DVDDetailDTO> {

    @Override
    protected DVD createProduct() {
        return new DVD();
    }

    @Override
    protected DVDDetailDTO createDetailDTO() {
        return new DVDDetailDTO();
    }

    @Override
    protected void mapSpecFields(DVD dvd, Map<String, Object> data) {
        dvd.setDiscType(getString(data, "discType"));
        dvd.setDirector(getString(data, "director"));
        dvd.setRuntime(parseInt(data.get("runtime")));
        dvd.setStudio(getString(data, "studio"));
        dvd.setLanguage(getString(data, "language"));
        dvd.setSubtitles(getString(data, "subtitles"));
        dvd.setGenre(getString(data, "genre"));
        dvd.setReleaseDate(DateUtils.parseDateNullable(data.get("releaseDate"), "releaseDate"));
    }

    @Override
    protected void updateSpecFields(DVD dvd, Map<String, Object> data) {
        if (data.containsKey("discType"))
            dvd.setDiscType(getString(data, "discType"));
        if (data.containsKey("director"))
            dvd.setDirector(getString(data, "director"));
        if (data.containsKey("runtime"))
            dvd.setRuntime(parseInt(data.get("runtime")));
        if (data.containsKey("studio"))
            dvd.setStudio(getString(data, "studio"));
        if (data.containsKey("language"))
            dvd.setLanguage(getString(data, "language"));
        if (data.containsKey("subtitles"))
            dvd.setSubtitles(getString(data, "subtitles"));
        if (data.containsKey("genre"))
            dvd.setGenre(getString(data, "genre"));
        if (data.containsKey("releaseDate"))
            dvd.setReleaseDate(DateUtils.parseDateNullable(data.get("releaseDate"), "releaseDate"));
    }
}
