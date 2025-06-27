package com.hustict.aims.utils.mapper;

import com.hustict.aims.dto.product.CDDetailDTO;
import com.hustict.aims.model.product.CD;
import com.hustict.aims.utils.DateUtils;

import org.springframework.stereotype.Component;
import java.util.Map;

@Component
public class CDMapper extends AbstractProductMapper<CD, CDDetailDTO> {

    @Override
    protected CD createProduct() {
        return new CD();
    }

    @Override
    protected void mapSpecFields(CD cd, Map<String, Object> data) {
        cd.setArtists((String) data.get("artists"));
        cd.setRecordLabel((String) data.get("recordLabel"));
        cd.setTrackList((String) data.get("trackList"));
        cd.setGenre((String) data.get("genre"));
        if (data.get("releaseDate") != null) {
            cd.setReleaseDate(DateUtils.parseDateNullable(data.get("releaseDate"), "releaseDate"));
        }
    }

    @Override
    protected void updateSpecFields(CD cd, Map<String, Object> data) {
        if (data.containsKey("artists")) cd.setArtists((String) data.get("artists"));
        if (data.containsKey("recordLabel")) cd.setRecordLabel((String) data.get("recordLabel"));
        if (data.containsKey("trackList")) cd.setTrackList((String) data.get("trackList"));
        if (data.containsKey("genre")) cd.setGenre((String) data.get("genre"));
        if (data.containsKey("releaseDate")) cd.setReleaseDate(DateUtils.parseDateNullable(data.get("releaseDate"), "releaseDate"));
    }

    @Override
    protected CDDetailDTO createDetailDTO() {
        return new CDDetailDTO();
    }
}
