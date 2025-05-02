package com.hustict.aims.repository;

import com.hustict.aims.exception.DatabaseFailConnectionException;
import com.hustict.aims.model.Product;

public interface ProductRepository {
    boolean isProductExists(String barcode);
    void saveProduct(Product product) throws DatabaseFailConnectionException;
}