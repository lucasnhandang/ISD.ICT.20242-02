package com.hustict.aims.controller;

import com.hustict.aims.dto.product.ProductDetailDTO;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.hustict.aims.service.ProductService;
import com.hustict.aims.service.ProductActionService;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/product-manager/products")
@CrossOrigin(origins = "*")
public class ProductPMController {
    private final ProductService productService;
    private final ProductActionService actionService;
    
    public ProductPMController(ProductService productService, ProductActionService actionService) {
        this.productService = productService;
        this.actionService = actionService;
    }

    @PostMapping
    public ResponseEntity<ProductDetailDTO> create(@RequestBody Map<String, Object> data) {
        ProductDetailDTO created = productService.createProduct(data);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDetailDTO> update(
            @PathVariable Long id,
            @RequestBody Map<String, Object> data,
            @RequestHeader("X-User-ID") Long userId) {
        
        ProductDetailDTO updated = productService.updateProduct(id, data, userId);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDetailDTO> view(@PathVariable Long id) {
        ProductDetailDTO dto = productService.viewProduct(id);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/limits/user/{userId}")
    public ResponseEntity<Map<String, Object>> getUserLimits(@PathVariable Long userId) {
        Map<String, Object> limits = actionService.getUserLimitsInfo(userId);
        return ResponseEntity.ok(limits);
    }

    @GetMapping("/limits/product/{productId}/user/{userId}")
    public ResponseEntity<Map<String, Object>> getProductLimits(
            @PathVariable Long productId, 
            @PathVariable Long userId) {
        Map<String, Object> limits = actionService.getProductLimitsInfo(userId, productId);
        return ResponseEntity.ok(limits);
    }
}
