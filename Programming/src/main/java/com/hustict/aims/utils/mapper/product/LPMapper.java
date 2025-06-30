package com.hustict.aims.utils.mapper.product;

import com.hustict.aims.dto.product.LPDetailDTO;
import com.hustict.aims.model.product.LP;
import com.hustict.aims.utils.DateUtils;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class LPMapper extends ProductMapper<LP, LPDetailDTO> {

    @Override
    protected LP createProduct() {
        return new LP();
    }

    @Override
    protected LPDetailDTO createDetailDTO() {
        return new LPDetailDTO();
    }

    @Override
    protected void mapSpecFields(LP lp, Map<String, Object> data) {
        lp.setArtists(getString(data, "artists"));
        lp.setRecordLabel(getString(data, "recordLabel"));
        lp.setTrackList(getString(data, "trackList"));
        lp.setGenre(getString(data, "genre"));
        lp.setReleaseDate(DateUtils.parseDateNullable(data.get("releaseDate"), "releaseDate"));
    }

    @Override
    protected void updateSpecFields(LP lp, Map<String, Object> data) {
        if (data.containsKey("artists"))
            lp.setArtists(getString(data, "artists"));
        if (data.containsKey("recordLabel"))
            lp.setRecordLabel(getString(data, "recordLabel"));
        if (data.containsKey("trackList"))
            lp.setTrackList(getString(data, "trackList"));
        if (data.containsKey("genre"))
            lp.setGenre(getString(data, "genre"));
        if (data.containsKey("releaseDate"))
            lp.setReleaseDate(DateUtils.parseDateNullable(data.get("releaseDate"), "releaseDate"));
    }
}
