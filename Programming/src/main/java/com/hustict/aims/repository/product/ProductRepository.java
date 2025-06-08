package com.hustict.aims.repository.product;

import com.hustict.aims.model.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {}
