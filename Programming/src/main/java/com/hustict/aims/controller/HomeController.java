package com.hustict.aims.controller;

import com.hustict.aims.dto.PagedResponseDTO;
import com.hustict.aims.dto.home.ProductSearchRequestDTO;
import com.hustict.aims.dto.home.ProductSummaryDTO;
import com.hustict.aims.service.HomeService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = "*")
public class HomeController {
    private final HomeService homeService;

    @Autowired
    public HomeController(HomeService homeService) {
        this.homeService = homeService;
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Lỗi xử lý backend: " + e.getMessage());
    }

    @PostConstruct
    public void init() {
        System.out.println("✅ HomeController đã được khởi tạo");
    }

    // Random products
    @GetMapping("/home")
    public ResponseEntity<PagedResponseDTO<ProductSummaryDTO>> getHomePage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        PagedResponseDTO<ProductSummaryDTO> products = homeService.getRandomProducts(page, size);
        return ResponseEntity.ok(products);
    }

    // Search products by name with sorting
    @GetMapping("/products/search")
    public ResponseEntity<PagedResponseDTO<ProductSummaryDTO>> searchProducts(
            @RequestParam(required = false) String query,
            @RequestParam(defaultValue = "title") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        ProductSearchRequestDTO searchRequest = new ProductSearchRequestDTO(query, sortBy, sortDirection, page, size);
        PagedResponseDTO<ProductSummaryDTO> products = homeService.searchProducts(searchRequest);
        return ResponseEntity.ok(products);
    }

    // Filter by category (Book, CD, ...)
    @GetMapping("/products/category/{category}")
    public ResponseEntity<PagedResponseDTO<ProductSummaryDTO>> getProductsByCategory(
            @PathVariable String category,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        PagedResponseDTO<ProductSummaryDTO> products = homeService.getProductsByCategory(category, page, size);
        return ResponseEntity.ok(products);
    }

    // Health check
    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("AIMS backend is running");
    }
}
