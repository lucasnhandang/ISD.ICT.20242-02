package com.hustict.aims.service.validator;

import com.hustict.aims.model.product.CD;
import com.hustict.aims.model.product.Product;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class CDValidator extends ProductValidator {
    @Override
    protected void validateSpecific(Product product, List<String> errors) {
        if (!(product instanceof CD)) {
            errors.add("Invalid product type for CD validator");
            return;
        }
        
        CD cd = (CD) product;
        rejectIfBlank(cd.getArtists(), "CD artists", 100, errors);
        rejectIfBlank(cd.getRecordLabel(), "CD record label", 100, errors);
    }
}
