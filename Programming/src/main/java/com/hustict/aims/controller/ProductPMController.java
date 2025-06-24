package com.hustict.aims.controller;

import com.hustict.aims.dto.product.ProductDetailDTO;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
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

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProductDetailDTO> create(
            @RequestPart("data") Map<String, Object> data,
            @RequestPart(value = "image", required = false) MultipartFile image) {

        ProductDetailDTO created = productService.createProduct(data, image);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProductDetailDTO> update(
            @PathVariable Long id,
            @RequestPart("data") Map<String, Object> data,
            @RequestPart(value = "image", required = false) MultipartFile image,
            @RequestHeader("X-User-ID") Long userId) {

        ProductDetailDTO updated = productService.updateProduct(id, data, image, userId);
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
