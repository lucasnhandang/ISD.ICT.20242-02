package com.hustict.aims.service.validator;

import com.hustict.aims.model.product.DVD;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class DVDValidator extends ProductValidator<DVD> {
    @Override
    public String getType() { return "DVD"; }

    @Override
    protected Class<DVD> getSupportedClass() { return DVD.class; }

    @Override
    protected void validateSpecific(DVD d, List<String> errs) {
        rejectIfBlank(d.getDiscType(), "Disc type", 50, errs);
        rejectIfBlank(d.getDirector(), "Director", 100, errs);
        rejectIfNegative(d.getRuntime(), "Runtime", errs);
        rejectIfBlank(d.getStudio(), "Studio", 100, errs);
        rejectIfBlank(d.getLanguage(), "Language", 50, errs);
        rejectIfBlank(d.getSubtitles(), "Subtitles", 255, errs);
    }
}
