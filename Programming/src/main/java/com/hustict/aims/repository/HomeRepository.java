package com.hustict.aims.repository;

import com.hustict.aims.model.product.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/*
 * Unified repository interface for all Product entities (home page and search functionality)
 * Provides general access to all product types (Books, CDs, DVDs, LPs) for customer operations
 * Differs from specialized repositories in /product/ folder which are for specific product type operations
 */
@Repository
public interface HomeRepository extends JpaRepository<Product, Long> {
       // Find products by title containing search query (case-insensitive)
       @Query("SELECT p FROM Product p WHERE " +
              "(:searchQuery IS NULL OR :searchQuery = '' OR LOWER(p.title) LIKE LOWER(CONCAT('%', :searchQuery, '%')))")
       Page<Product> findByTitle(@Param("searchQuery") String searchQuery, Pageable pageable);

       // Get random products for home page
       @Query("SELECT p FROM Product p")
       Page<Product> getRandomProducts(Pageable pageable);

       // Get products by category
       @Query("SELECT p FROM Product p WHERE p.category = :category")
       Page<Product> findByCategory(@Param("category") String category, Pageable pageable);
}