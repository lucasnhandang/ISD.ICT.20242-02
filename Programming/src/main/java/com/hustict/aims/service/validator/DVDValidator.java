package com.hustict.aims.service.validator;

import com.hustict.aims.model.product.DVD;
import com.hustict.aims.model.product.Product;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class DVDValidator extends ProductValidator {
    @Override
    protected void validateSpecific(Product product, List<String> errors) {
        if (!(product instanceof DVD)) {
            errors.add("Invalid product type for DVD validator");
            return;
        }
        
        DVD dvd = (DVD) product;
        rejectIfBlank(dvd.getDirector(), "DVD director", 100, errors);
        rejectIfBlank(dvd.getStudio(), "DVD studio", 100, errors);
        
        if (dvd.getRuntime() <= 0) {
            errors.add("DVD runtime must be positive!");
        }
    }
}
