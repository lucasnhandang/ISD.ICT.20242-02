package com.hustict.aims.service.validator;

import com.hustict.aims.model.product.LP;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class LPValidator extends ProductValidator<LP> {
    @Override
    public String getType() { return "LP"; }

    @Override
    protected Class<LP> getSupportedClass() { return LP.class; }

    @Override
    protected void validateSpecific(LP lp, List<String> errs) {
        rejectIfBlank(lp.getArtists(), "Artists", 100, errs);
        rejectIfBlank(lp.getRecordLabel(), "Record label", 100, errs);
        rejectIfBlank(lp.getTrackList(), "Track list", 0, errs);
        rejectIfBlank(lp.getGenre(), "Genre", 100, errs);
    }
}
