package com.hustict.aims.service.home;

import com.hustict.aims.dto.home.ProductListResponseDTO;
import com.hustict.aims.dto.home.ProductSearchRequestDTO;
import com.hustict.aims.dto.home.ProductSummaryDTO;
import com.hustict.aims.model.product.Product;
import com.hustict.aims.repository.product.ProductRepository;
import com.hustict.aims.utils.mapper.ProductSummaryMapper;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    public ProductListResponseDTO getRandomProducts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> productPage = repository.getRandomProducts(pageable);

        return buildProductListResponse(productPage);
    }

    public ProductListResponseDTO searchProducts(ProductSearchRequestDTO searchRequest) {
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
        return buildProductListResponse(page);
    }

    public ProductListResponseDTO getProductsByCategory(String category, String sortBy, String sortDirection, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, createSort(sortBy, sortDirection));
        Page<Product> pageResult = repository.findByCategory(category, pageable);
        return buildProductListResponse(pageResult);
    }

    private Sort createSort(String sortBy, String sortDirection) {
        String field = SORT_FIELD_MAP.getOrDefault(sortBy.toLowerCase(), "title");
        Sort.Direction dir = "desc".equalsIgnoreCase(sortDirection) ? Sort.Direction.DESC : Sort.Direction.ASC;
        return Sort.by(dir, field);
    }

    private ProductListResponseDTO buildProductListResponse(Page<Product> productPage) {
        List<ProductSummaryDTO> products = productPage.getContent()
                .stream()
                .map(mapper::toSummaryDTO)
                .collect(Collectors.toList());

        return new ProductListResponseDTO(
                products,
                productPage.getTotalElements(),
                productPage.getTotalPages(),
                productPage.getNumber(),
                productPage.getSize(),
                productPage.isFirst(),
                productPage.isLast()
        );
    }
}