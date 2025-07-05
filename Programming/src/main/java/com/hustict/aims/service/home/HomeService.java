package com.hustict.aims.service.home;

import com.hustict.aims.dto.PagedResponseDTO;
import com.hustict.aims.dto.home.ProductSearchRequestDTO;
import com.hustict.aims.dto.home.ProductSummaryDTO;
import com.hustict.aims.model.product.Product;
import com.hustict.aims.repository.product.ProductRepository;
import com.hustict.aims.utils.builder.PagedResponseBuilder;
import com.hustict.aims.utils.mapper.ProductSummaryMapper;

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

    private final ProductRepository repository;
    private final ProductSummaryMapper mapper;

    private static final Map<String, String> SORT_FIELD_MAP = Map.of(
            "price", "currentPrice",
            "title", "title"
    );

    public HomeService(ProductRepository repository, ProductSummaryMapper productMapper) {
        this.repository = repository;
        this.mapper = productMapper;
    }

    public PagedResponseDTO<ProductSummaryDTO> getRandomProducts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> productPage = repository.getRandomProducts(pageable);

        return PagedResponseBuilder.fromPage(productPage, mapper::toSummaryDTO);
    }

    public PagedResponseDTO<ProductSummaryDTO> searchProducts(ProductSearchRequestDTO searchRequest) {
        Pageable pageable = PageRequest.of(
                searchRequest.getPage(),
                searchRequest.getSize(),
                createSort(searchRequest.getSortBy(), searchRequest.getSortDirection())
        );

        Page<Product> page;

        if (searchRequest.getCategory() != null && !searchRequest.getCategory().isEmpty()) {
            page = repository.searchByTitleAndCategory(searchRequest.getSearchQuery(), searchRequest.getCategory(), pageable);
        } else {
            page = repository.searchByTitle(searchRequest.getSearchQuery(), pageable);
        }
        return PagedResponseBuilder.fromPage(page, mapper::toSummaryDTO);
    }

    public PagedResponseDTO<ProductSummaryDTO> getProductsByCategory(String category, String sortBy, String sortDirection, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, createSort(sortBy, sortDirection));
        Page<Product> pageResult = repository.findByCategory(category, pageable);
        return PagedResponseBuilder.fromPage(pageResult, mapper::toSummaryDTO);
    }

    private Sort createSort(String sortBy, String sortDirection) {
        String field = SORT_FIELD_MAP.getOrDefault(sortBy.toLowerCase(), "title");
        Sort.Direction dir = "desc".equalsIgnoreCase(sortDirection) ? Sort.Direction.DESC : Sort.Direction.ASC;
        return Sort.by(dir, field);
    }
}