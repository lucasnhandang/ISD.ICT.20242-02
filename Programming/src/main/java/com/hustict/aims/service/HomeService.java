package com.hustict.aims.service;

import com.hustict.aims.dto.PagedResponseDTO;
import com.hustict.aims.dto.home.ProductSearchRequestDTO;
import com.hustict.aims.dto.home.ProductSummaryDTO;
import com.hustict.aims.model.product.Product;
import com.hustict.aims.repository.HomeRepository;
import com.hustict.aims.utils.builder.PagedResponseBuilder;
import com.hustict.aims.utils.mapper.ProductSummaryMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/*
- For Home Page and Product Browsing operations
- Handles customer-facing product operations: home page display + search products
- Focus only on customer product browsing
*/

@Service
@Transactional(readOnly = true)
public class HomeService {

    private final HomeRepository homeRepo;
    private final ProductSummaryMapper productSummaryMapper;

    private static final Map<String, String> SORT_FIELD_MAP = Map.of(
        "price", "currentPrice",
        "title", "title"
    );

    @Autowired
    public HomeService(HomeRepository homeRepo, ProductSummaryMapper productMapper) {
        this.homeRepo = homeRepo;
        this.productSummaryMapper = productMapper;
    }

    public PagedResponseDTO<ProductSummaryDTO> getRandomProducts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> productPage = homeRepo.getRandomProducts(pageable);

        return PagedResponseBuilder.fromPage(productPage, productSummaryMapper::toSummaryDTO);
    }

    public PagedResponseDTO<ProductSummaryDTO> searchProducts(ProductSearchRequestDTO searchRequest) {
        Pageable pageable = PageRequest.of(
            searchRequest.getPage(),
            searchRequest.getSize(),
            createSort(searchRequest.getSortBy(), searchRequest.getSortDirection())
        );

        Page<Product> page = homeRepo.findByTitle(searchRequest.getSearchQuery(), pageable);
        return PagedResponseBuilder.fromPage(page, productSummaryMapper::toSummaryDTO);
    }

    public PagedResponseDTO<ProductSummaryDTO> getProductsByCategory(String category, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> pageResult = homeRepo.findByCategory(category, pageable);
        return PagedResponseBuilder.fromPage(pageResult, productSummaryMapper::toSummaryDTO);
    }

    private Sort createSort(String sortBy, String sortDirection) {
        String field = SORT_FIELD_MAP.getOrDefault(sortBy.toLowerCase(), "title");
        Sort.Direction dir = "desc".equalsIgnoreCase(sortDirection) ? Sort.Direction.DESC : Sort.Direction.ASC;
        return Sort.by(dir, field);
    }
}