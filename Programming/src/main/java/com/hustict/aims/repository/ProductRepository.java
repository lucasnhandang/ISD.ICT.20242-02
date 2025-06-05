package com.hustict.aims.repository;

import com.hustict.aims.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("SELECT p FROM Product p WHERE p.title LIKE CONCAT(:namePart, '%')")
    List<Product> findByName(@Param("namePart") String namePart);
} 