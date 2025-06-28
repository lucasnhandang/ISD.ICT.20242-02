package com.hustict.aims.service.validation;

import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@Component
public class ProductValidatorRegistry {
    private final Map<String, ProductValidator<?>> validators;

    public ProductValidatorRegistry(List<ProductValidator<?>> validators) {
        this.validators = new HashMap<>();
        
        for (ProductValidator<?> validator : validators) {
            this.validators.put(validator.getType().toLowerCase(), validator);
        }
    }

    public Optional<ProductValidator<?>> getValidator(String type) {
        if (type == null || type.trim().isEmpty()) {
            return Optional.empty();
        }

        return Optional.ofNullable(validators.get(type.toLowerCase()));
    }
}
