package com.hustict.aims.service.handler;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class ProductHandlerRegistry {
    private final List<ProductHandler> handlers;

    public ProductHandlerRegistry(List<ProductHandler> handlers) {
        this.handlers = handlers;
    }

    public Optional<ProductHandler> getHandler(String type) {
        for (ProductHandler handler : handlers) {
            if (handler.supports(type)) {
                return Optional.of(handler);
            }
        }
        return Optional.empty();
    }
}
