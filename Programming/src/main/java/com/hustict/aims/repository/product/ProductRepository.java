package com.hustict.aims.repository.product;

import com.hustict.aims.model.product.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    @Modifying
    @Query("UPDATE Product p SET p.isDeleted = true WHERE p.id = :id")
    void softDeleteById(@Param("id") Long id);

    @Query("SELECT p FROM Product p WHERE p.id = :id AND p.isDeleted = false")
    Optional<Product> findByIdNotDeleted(@Param("id") Long id);

    @Query("SELECT p FROM Product p WHERE p.id IN :ids AND p.isDeleted = false")
    List<Product> findAllByIdNotDeleted(@Param("ids") List<Long> ids);

    @Query("SELECT p.currentPrice FROM Product p WHERE p.id = :productId")
    Integer getCurrentPrice(@Param("productId") Long productId);

    @Query("SELECT p FROM Product p WHERE p.isDeleted = false")
    Page<Product> getRandomProducts(Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.isDeleted = false AND LOWER(p.title) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Product> searchByTitle(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.isDeleted = false AND LOWER(p.title) LIKE LOWER(CONCAT('%', :keyword, '%')) AND LOWER(p.category) = LOWER(:category)")
    Page<Product> searchByTitleAndCategory(@Param("keyword") String keyword, @Param("category") String category, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.isDeleted = false AND LOWER(p.category) = LOWER(:category)")
    Page<Product> findByCategory(@Param("category") String category, Pageable pageable);
}
