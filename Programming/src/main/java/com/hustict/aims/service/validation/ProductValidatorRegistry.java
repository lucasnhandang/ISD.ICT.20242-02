package com.hustict.aims.service.validation;

import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Optional;


@Component
public class ProductValidatorRegistry {
    private final List<ProductValidator<?>> validators;

    // Spring sẽ tự động inject TẤT CẢ các bean implement ProductValidator vào đây
    public ProductValidatorRegistry(List<ProductValidator<?>> validators) {
        this.validators = validators;
    }

    public Optional<ProductValidator<?>> getValidator(String type) {
        if (type == null || type.trim().isEmpty()) {
            return Optional.empty();
        }

        for (ProductValidator<?> validator : validators) {
            if (validator.getType().equalsIgnoreCase(type)) {
                return Optional.of(validator);
            }
        }

        return Optional.empty();
    }
}
