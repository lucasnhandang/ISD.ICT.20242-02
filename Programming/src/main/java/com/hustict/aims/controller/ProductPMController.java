package com.hustict.aims.controller;

import com.hustict.aims.dto.product.ProductDTO;
import com.hustict.aims.dto.product.ProductModifyRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.*;

import com.hustict.aims.service.product.ProductService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/product-manager/products")
@CrossOrigin(origins = "*")
public class ProductPMController {
    private final ProductService productService;

    public ProductPMController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProductDTO> create(
            @RequestPart("data") ProductModifyRequest request,
            @RequestPart(value = "image", required = false) MultipartFile image) {

        ProductDTO created = productService.createProduct(request, image);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProductDTO> update(
            @PathVariable Long id,
            @RequestPart("data") ProductModifyRequest request,
            @RequestPart(value = "image", required = false) MultipartFile image,
            @RequestHeader("X-User-ID") Long userId) {

        ProductDTO updated = productService.updateProduct(id, request, image, userId);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> view(@PathVariable Long id) {
        ProductDTO dto = productService.viewProduct(id);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping
    public ResponseEntity<Map<String, String>> deleteProducts(
            @RequestBody Map<String, List<Long>> payload,
            @RequestHeader("X-User-ID") Long userId) {

        List<Long> productIds = payload.get("productIds");
        productService.deleteProducts(userId, productIds);

        return ResponseEntity.ok(Map.of("message", "Products deleted successfully!"));
    }
}
