package com.hustict.aims.service.validator;

import com.hustict.aims.exception.InvalidProductException;
import com.hustict.aims.model.Product;

public interface ProductValidator {
    void validate(Product product) throws InvalidProductException;
}
