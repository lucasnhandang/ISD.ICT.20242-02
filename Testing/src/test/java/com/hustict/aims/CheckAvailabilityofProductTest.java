package com.hustict.aims;
import com.hustict.aims.exception.*;

import com.hustict.aims.service.*;
import com.hustict.aims.model.*;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CheckAvailabilityofProductTest {

    private final ProductService productService = new ProductService();

    private Product createProduct(int id, String title, int totalQuantity) {
        return new Product(
            id,
            title,
            100,                  // price
            totalQuantity,
            0.5,                  // weight
            true,                 // rushOrderSupported
            "image.jpg",          // imageUrl
            "123456789",          // barcode
            "Test description",   // description
            "10x10x10"            // productDimension
        );
    }

    @Test
    void testAvailableProduct() {
        Product product = createProduct(1, "Book", 10);
        assertTrue(productService.checkAvailability(product, 5));
    }

    @Test
    void testInsufficientStock() {
        Product product = createProduct(2, "Pen", 2);
        InsufficientStockException thrown = assertThrows(
            InsufficientStockException.class,
            () -> productService.checkAvailability(product, 5)
        );
        assertEquals("Not enough stock for product Pen.", thrown.getMessage());
    }

    @Test
    void testOutOfStock() {
        Product product = createProduct(3, "Notebook", 0);
        OutOfStockException thrown = assertThrows(
            OutOfStockException.class,
            () -> productService.checkAvailability(product, 5)
        );
        assertEquals("Notebook is out of stock.", thrown.getMessage());
    }
}