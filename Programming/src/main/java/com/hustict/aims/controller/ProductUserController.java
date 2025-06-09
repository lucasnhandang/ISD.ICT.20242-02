package com.hustict.aims.controller;

import com.hustict.aims.dto.product.ProductDetailDTO;
import com.hustict.aims.utils.mapper.ProductSummaryMapper;
import com.hustict.aims.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/v1/products")
@CrossOrigin(origins = "*")
public class ProductUserController {

    private final ProductService productService;

    public ProductUserController(ProductService productService, ProductSummaryMapper productMapper) {
        this.productService = productService;
    }

    // View product details
    @GetMapping("/{id}")
    public ResponseEntity<ProductDetailDTO> getProductDetail(@PathVariable Long id) {
        try {
            ProductDetailDTO product = productService.viewProduct(id);
            return ResponseEntity.ok(product);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Check product availability when adding to cart
    @GetMapping("/{id}/availability")
    public ResponseEntity<Boolean> checkProductAvailability(
            @PathVariable Long id,
            @RequestParam(defaultValue = "1") int quantity) {

        boolean available = productService.isProductAvailable(id, quantity);
        return ResponseEntity.ok(available);
    }
}