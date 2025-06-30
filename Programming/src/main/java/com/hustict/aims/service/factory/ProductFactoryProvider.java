package com.hustict.aims.service.factory;

import com.hustict.aims.model.product.Product;
import com.hustict.aims.dto.product.ProductDetailDTO;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class ProductFactoryProvider {
    private final Map<String, ProductFactory<?, ?>> factories;
    
    public ProductFactoryProvider(List<ProductFactory<?, ?>> factoryList) {
        this.factories = factoryList.stream()
                .collect(Collectors.toMap(
                    factory -> factory.getProductType().toLowerCase(),
                    factory -> factory
                ));
    }
    
    @SuppressWarnings("unchecked")
    public <T extends Product, D extends ProductDetailDTO> Optional<ProductFactory<T, D>> getFactory(String productType) {
        if (productType == null || productType.trim().isEmpty()) {
            return Optional.empty();
        }
        
        ProductFactory<?, ?> factory = factories.get(productType.toLowerCase());
        return Optional.ofNullable((ProductFactory<T, D>) factory);
    }

    public boolean supports(String productType) {
        return productType != null && factories.containsKey(productType.toLowerCase());
    }
}