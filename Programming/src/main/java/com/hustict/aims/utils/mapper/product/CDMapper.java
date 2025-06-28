package com.hustict.aims.utils.mapper.product;

import com.hustict.aims.dto.product.CDDetailDTO;
import com.hustict.aims.model.product.CD;
import com.hustict.aims.utils.DateUtils;

import org.springframework.stereotype.Component;
import java.util.Map;

@Component
public class CDMapper extends ProductMapper<CD, CDDetailDTO> {

    @Override
    protected CD createProduct() {
        return new CD();
    }

    @Override
    protected CDDetailDTO createDetailDTO() {
        return new CDDetailDTO();
    }

    @Override
    protected void mapSpecFields(CD cd, Map<String, Object> data) {
        cd.setArtists(getString(data, "artists"));
        cd.setRecordLabel(getString(data, "recordLabel"));
        cd.setTrackList(getString(data, "trackList"));
        cd.setGenre(getString(data, "genre"));
        cd.setReleaseDate(DateUtils.parseDateNullable(data.get("releaseDate"), "releaseDate"));
    }

    @Override
    protected void updateSpecFields(CD cd, Map<String, Object> data) {
        if (data.containsKey("artists"))
            cd.setArtists(getString(data, "artists"));
        if (data.containsKey("recordLabel"))
            cd.setRecordLabel(getString(data, "recordLabel"));
        if (data.containsKey("trackList"))
            cd.setTrackList(getString(data, "trackList"));
        if (data.containsKey("genre"))
            cd.setGenre(getString(data, "genre"));
        if (data.containsKey("releaseDate"))
            cd.setReleaseDate(DateUtils.parseDateNullable(data.get("releaseDate"), "releaseDate"));
    }
}
