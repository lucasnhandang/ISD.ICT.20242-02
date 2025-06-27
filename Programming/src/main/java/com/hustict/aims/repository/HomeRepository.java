package com.hustict.aims.repository;

import com.hustict.aims.model.product.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface HomeRepository extends JpaRepository<Product, Long> {
       @Query("SELECT p FROM Product p WHERE p.isDeleted = false")
       Page<Product> getRandomProducts(Pageable pageable);

       @Query("SELECT p FROM Product p WHERE p.isDeleted = false AND LOWER(p.title) LIKE LOWER(CONCAT('%', :keyword, '%'))")
       Page<Product> searchByTitle(@Param("keyword") String keyword, Pageable pageable);

       @Query("SELECT p FROM Product p WHERE p.isDeleted = false AND LOWER(p.title) LIKE LOWER(CONCAT('%', :keyword, '%')) AND LOWER(p.category) = LOWER(:category)")
       Page<Product> searchByTitleAndCategory(@Param("keyword") String keyword, @Param("category") String category, Pageable pageable);

       @Query("SELECT p FROM Product p WHERE p.isDeleted = false AND LOWER(p.category) = LOWER(:category)")
       Page<Product> findByCategory(@Param("category") String category, Pageable pageable);
}