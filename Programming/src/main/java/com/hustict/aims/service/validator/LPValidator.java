package com.hustict.aims.service.validator;

import com.hustict.aims.model.product.LP;
import com.hustict.aims.model.product.Product;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class LPValidator extends ProductValidator {
    @Override
    protected void validateSpecific(Product product, List<String> errors) {
        if (!(product instanceof LP)) {
            errors.add("Invalid product type for LP validator");
            return;
        }
        
        LP lp = (LP) product;
        rejectIfBlank(lp.getArtists(), "LP artists", 100, errors);
        rejectIfBlank(lp.getRecordLabel(), "LP record label", 100, errors);
    }
}
