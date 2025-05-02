package com.hustict.aims.service.validator;

import com.hustict.aims.model.Product;
import com.hustict.aims.model.CD;
import com.hustict.aims.model.DVD;
import com.hustict.aims.model.LP;
import com.hustict.aims.model.Book;

public class ProductValidatorFactory {
    public static ProductValidator getValidator(Product product) {
        if (product instanceof Book) return new BookValidator();
        if (product instanceof CD) return new CDValidator();
        if (product instanceof DVD) return new DVDValidator();
        if (product instanceof LP) return new LPValidator();
        throw new IllegalArgumentException("No validator found for product type: " + product.getClass().getSimpleName());
    }
}
