package com.hustict.aims.service.factory;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class ProductFactoryProvider {
    private final Map<String, ProductFactory> factories;
    
    public ProductFactoryProvider(List<ProductFactory> factoryList) {
        this.factories = new HashMap<>();
        for (ProductFactory factory : factoryList) {
            String category = factory.getCategory();
            factories.put(category, factory);
        }
    }
    
    public Optional<ProductFactory> getFactory(String category) {
        return Optional.ofNullable(factories.get(category));
    }

    public boolean supports(String category) {
        return factories.containsKey(category);
    }
}