package com.hustict.aims.service.validation;

import com.hustict.aims.model.product.CD;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class CDValidator extends ProductValidator<CD> {
    @Override
    public String getType() { return "CD"; }

    @Override
    protected Class<CD> getSupportedClass() { return CD.class; }

    @Override
    protected void validateSpecific(CD cd, List<String> errs) {
        rejectIfBlank(cd.getArtists(), "Artists", 100, errs);
        rejectIfBlank(cd.getRecordLabel(), "Record label", 100, errs);
        rejectIfBlank(cd.getTrackList(), "Track list", 0, errs);
        rejectIfBlank(cd.getGenre(), "Genre", 100, errs);
    }
}
