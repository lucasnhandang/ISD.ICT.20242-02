/*
 * Cohesion Analysis:
 * - Functional Cohesion: As all methods are related to product creation and validation
 * - SRP Analysis:
 *   1. It handles both validation and creation logic
 *   2. It contains error display functionality which could be separated
 * 
 * Improvement Suggestions:
 * 1. Split into separate classes:
 *    - ProductValidator: Handle all validation logic
 *    - ProductCreator: Handle product creation and database operations
 *    - ErrorHandler: Handle error display and logging
 * 2. Use dependency injection for better testability
 */
package com.hustict.aims.controller;

import com.hustict.aims.exception.DatabaseFailConnectionException;
import com.hustict.aims.exception.DuplicateProductException;
import com.hustict.aims.exception.InvalidProductException;
import com.hustict.aims.model.Product;
import com.hustict.aims.model.ProductInfo;
import com.hustict.aims.repository.ProductRepository;
import com.hustict.aims.service.validator.ProductValidator;
import com.hustict.aims.service.validator.ProductValidatorFactory;

public class CreateProductController {
    private final ProductRepository repository;

    public CreateProductController(ProductRepository repository) {
        this.repository = repository;
    }

    public boolean checkValidityOfProductInfo(ProductInfo productInfo) throws InvalidProductException {
        if (productInfo == null || productInfo.getProduct() == null) {
            throw new InvalidProductException("Product information is incomplete or null.");
        }

        Product product = productInfo.getProduct();
        validateCommonFields(product);
        validateSpecificFields(product);

        return true;
    }

    private void validateCommonFields(Product product) throws InvalidProductException {
        if (product.getTitle() == null || product.getTitle().isEmpty()
                || product.getPrice() <= 0
                || product.getTotalQuantity() <= 0
                || product.getWeight() <= 0
                || product.getBarcode() == null || product.getBarcode().isEmpty()
                || product.getDescription() == null || product.getDescription().isEmpty()
                || product.getProductDimension() == null || product.getProductDimension().isEmpty()
                || product.getImageUrl() == null || product.getImageUrl().isEmpty()) {
            throw new InvalidProductException("One or more required fields are invalid.");
        }
    }

    private void validateSpecificFields(Product product) throws InvalidProductException {
        ProductValidator validator = ProductValidatorFactory.getValidator(product);
        validator.validate(product);
    }

    public void createProduct(ProductInfo productInfo)
            throws InvalidProductException, DatabaseFailConnectionException, DuplicateProductException {
        if (!checkValidityOfProductInfo(productInfo)) return;

        Product product = productInfo.getProduct();
        if (repository.isProductExists(product.getBarcode())) {
            throw new DuplicateProductException("Product with this barcode already exists.");
        }
        repository.saveProduct(product);
    }

    public void displayError(String message) {
        System.err.println("ERROR: " + message);
    }
}
