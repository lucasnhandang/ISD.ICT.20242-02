package com.hustict.aims.service.product;

import com.hustict.aims.service.factory.ProductFactoryProvider;
import com.hustict.aims.service.factory.ProductBundle;
import com.hustict.aims.service.handler.ProductHandler;
import com.hustict.aims.service.validation.ProductValidator;
import com.hustict.aims.model.product.Product;
import com.hustict.aims.dto.product.ProductDetailDTO;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProductComponentService {
    
    private final ProductFactoryProvider factoryProvider;
    
    public ProductComponentService(ProductFactoryProvider factoryProvider) {
        this.factoryProvider = factoryProvider;
    }

    public ProductHandler getHandler(String productType) {
        return getBundle(productType).getHandler();
    }

    public <T extends Product, D extends ProductDetailDTO> ProductBundle<T, D> getBundle(String productType) {
        Optional<ProductBundle<T, D>> bundle = factoryProvider.createBundle(productType);
        return bundle.orElseThrow(() -> 
            new RuntimeException("Unsupported product type: " + productType));
    }

    @SuppressWarnings("unchecked")
    public <T extends Product> ProductValidator<T> getValidator(String productType) {
        return (ProductValidator<T>) getBundle(productType).getValidator();
    }

    public boolean supports(String productType) {
        return factoryProvider.supports(productType);
    }
} 