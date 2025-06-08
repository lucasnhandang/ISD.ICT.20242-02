package com.hustict.aims.utils.mapper;


import com.hustict.aims.dto.product.LPDetailDTO;
import com.hustict.aims.model.product.LP;
import com.hustict.aims.utils.DateUtils;
import org.springframework.stereotype.Component;
import java.util.Map;

@Component
public class LPMapper extends AbstractProductMapper<LP, LPDetailDTO> {

    @Override
    protected LP createProduct() {
        return new LP();
    }

    @Override
    protected void mapSpecFields(LP lp, Map<String, Object> data) {
        lp.setArtists((String) data.get("artists"));
        lp.setRecordLabel((String) data.get("recordLabel"));
        lp.setTrackList((String) data.get("trackList"));
        lp.setGenre((String) data.get("genre"));
        if (data.get("releaseDate") != null) {
            lp.setReleaseDate(DateUtils.parseDateNullable(data.get("releaseDate"), "releaseDate"));
        }
    }

    @Override
    protected void updateSpecFields(LP lp, Map<String, Object> data) {
        if (data.containsKey("artists")) lp.setArtists((String) data.get("artists"));
        if (data.containsKey("recordLabel")) lp.setRecordLabel((String) data.get("recordLabel"));
        if (data.containsKey("trackList")) lp.setTrackList((String) data.get("trackList"));
        if (data.containsKey("genre")) lp.setGenre((String) data.get("genre"));
        if (data.containsKey("releaseDate")) lp.setReleaseDate(DateUtils.parseDateNullable(data.get("releaseDate"), "releaseDate"));
    }

    @Override
    protected LPDetailDTO createDetailDTO() {
        return new LPDetailDTO();
    }
}