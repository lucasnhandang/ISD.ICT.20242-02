package com.hustict.aims.controller;

import com.hustict.aims.dto.PagedResponseDTO;
import com.hustict.aims.dto.home.ProductSearchRequestDTO;
import com.hustict.aims.dto.home.ProductSummaryDTO;
import com.hustict.aims.service.home.HomeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = "*")
public class HomeController {
    private final HomeService homeService;

    public HomeController(HomeService homeService) {
        this.homeService = homeService;
    }

    @GetMapping("/home")
    public ResponseEntity<PagedResponseDTO<ProductSummaryDTO>> getHomePage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        PagedResponseDTO<ProductSummaryDTO> products = homeService.getRandomProducts(page, size);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/products/search")
    public ResponseEntity<PagedResponseDTO<ProductSummaryDTO>> searchProducts(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) String category,
            @RequestParam(defaultValue = "title") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        ProductSearchRequestDTO searchRequest = new ProductSearchRequestDTO(query, category, sortBy, sortDirection, page, size);
        PagedResponseDTO<ProductSummaryDTO> products = homeService.searchProducts(searchRequest);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/products/category/{category}")
    public ResponseEntity<PagedResponseDTO<ProductSummaryDTO>> getProductsByCategory(
            @PathVariable String category,
            @RequestParam(defaultValue = "title") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        PagedResponseDTO<ProductSummaryDTO> products = homeService.getProductsByCategory(category, sortBy, sortDirection, page, size);
        return ResponseEntity.ok(products);
    }
}