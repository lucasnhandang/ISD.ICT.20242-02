package com.hustict.aims;
import com.hustict.aims.exception.*;

import com.hustict.aims.service.*;
import com.hustict.aims.model.*;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ReturnToInventoryTest {

    private final ProductService productService = new ProductService();

    private Product createProduct(int id, int quantity) {
        return new Product(
            id,
            "Test Product",       // title
            100,                  // price
            quantity,
            1.0,                  // weight
            true,                 // rushOrderSupported
            "img.jpg",            // imageUrl
            "1234567890",         // barcode
            "description",        // description
            "10x10x10"            // productDimension
        );
    }

    @Test
    void testRestockValidQuantity() {
        Product product = createProduct(1, 10);
        productService.restock(product, 3);
        assertEquals(13, product.getTotalQuantity());
    }

    @Test
    void testRestockZeroQuantityThrowsException() {
        Product product = createProduct(2, 5);
        IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> productService.restock(product, 0)
        );
        assertEquals("Restock quantity must be positive!", thrown.getMessage());
    }

    @Test
    void testRestockNegativeQuantityThrowsException() {
        Product product = createProduct(3, 5);
        IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> productService.restock(product, -2)
        );
        assertEquals("Restock quantity must be positive!", thrown.getMessage());
    }
}
