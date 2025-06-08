package com.hustict.aims.repository.product;

import com.hustict.aims.model.product.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {}
