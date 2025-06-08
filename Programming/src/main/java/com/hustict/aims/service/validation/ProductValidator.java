package com.hustict.aims.service.validation;

import com.hustict.aims.model.product.Product;
import java.util.ArrayList;
import java.util.List;

public abstract class ProductValidator<T extends Product> {
    public abstract String getType();

    protected abstract Class<T> getSupportedClass();

    public boolean canValidate(Product product) {
        return getSupportedClass().isInstance(product);
    }

    @SuppressWarnings("unchecked")
    public final List<String> validate(Product product) {
        List<String> errors = new ArrayList<>();
        if (canValidate(product)) {
            T typedProduct = (T) product;
            validateCommon(typedProduct, errors);
            validateSpecific(typedProduct, errors);
        } else {
            errors.add("Validator type mismatch for product: " + product.getTitle());
        }
        return errors;
    }
    
    protected void rejectIfBlank(String value, String field, int max, List<String> errs) {
        if (value == null || value.trim().isEmpty()) {
            errs.add(field + " is required");
        } else if (max > 0 && value.length() > max) {
            errs.add(field + " must not exceed " + max + " characters!");
        }
    }

    protected void rejectIfNegative(int num, String field, List<String> errs) {
        if (num <= 0) {
            errs.add(field + " must be positive!");
        }
    }

    protected void rejectIfNull(Object o, String field, List<String> errs) {
        if (o == null) {
            errs.add(field + " is required!");
        }
    }

    protected void validateCommon(Product p, List<String> errs) {
        rejectIfBlank(p.getTitle(), "Product title", 255, errs);
        rejectIfNegative(p.getValue(), "Product value", errs);
        rejectIfNegative(p.getCurrentPrice(), "Product current price", errs);
        if (p.getValue() > 0) {
            double min = p.getValue() * 0.3;
            double max = p.getValue() * 1.5;
            if (p.getCurrentPrice() < min || p.getCurrentPrice() > max) {
                errs.add(String.format("Current price must be between 30%% and 150%% of value (%.0f – %.0f)!", min, max));
            }
        }
        rejectIfBlank(p.getBarcode(), "Barcode", 100, errs);
        rejectIfBlank(p.getDescription(), "Product description", 255, errs);
        rejectIfNegative(p.getQuantity(), "Product quantity", errs);
        rejectIfNull(p.getEntryDate(), "Entry date", errs);
        rejectIfBlank(p.getDimension(), "Product dimension", 50, errs);

        if (p.getWeight() <= 0) {
            errs.add("Product weight must be positive!");
        }
    }

    protected abstract void validateSpecific(T product, List<String> errs);
}
