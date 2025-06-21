package com.hustict.aims.repository;

import com.hustict.aims.model.product.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface HomeRepository extends JpaRepository<Product, Long> {
       // Find products by title containing search query (case-insensitive)
       Page<Product> findByTitleContainingIgnoreCase(String title, Pageable pageable);

       // Get random products for home page
       @Query("SELECT p FROM Product p")
       Page<Product> getRandomProducts(Pageable pageable);

       // Get products by category
       Page<Product> findByCategory(String category, Pageable pageable);

       // Find by title and category (both case-insensitive)
       Page<Product> findByTitleContainingIgnoreCaseAndCategoryIgnoreCase(String title, String category, Pageable pageable);
}