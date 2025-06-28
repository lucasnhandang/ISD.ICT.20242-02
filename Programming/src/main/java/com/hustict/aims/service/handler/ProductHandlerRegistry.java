package com.hustict.aims.service.handler;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class ProductHandlerRegistry {
    private final Map<String, ProductHandler> handlers;

    public ProductHandlerRegistry(List<ProductHandler> handlers) {
        this.handlers = new HashMap<>();

        for (ProductHandler handler : handlers) {
            this.handlers.put(handler.getType().toLowerCase(), handler);
        }
    }

    public Optional<ProductHandler> getHandler(String type) {
        if (type == null || type.trim().isEmpty()) {
            return Optional.empty();
        }
        
        return Optional.ofNullable(handlers.get(type.toLowerCase()));
    }
}
