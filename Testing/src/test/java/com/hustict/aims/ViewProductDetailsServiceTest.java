package com.hustict.aims;


import com.hustict.aims.model.ProductInfo;
import com.hustict.aims.service.validator.ViewProductDetailsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ViewProductDetailsServiceTest {

    private ViewProductDetailsService viewProductDetailsService;

    @BeforeEach
    void setUp() {
        viewProductDetailsService = new ViewProductDetailsService();
    }

    @Test
    void testValidProduct() {
        ProductInfo info = viewProductDetailsService.getProductDetails(42);
        assertNotNull(info);
        assertEquals("Effective Java", info.getProduct().getTitle());
    }

    @Test
    void testProductNotFound() {
        Exception ex = assertThrows(RuntimeException.class, () ->
                viewProductDetailsService.getProductDetails(999));
        assertTrue(ex.getMessage().contains("Product 999 not found"));
    }

    @Test
    void testInactiveProduct() {
        Exception ex = assertThrows(IllegalStateException.class, () ->
                viewProductDetailsService.getProductDetails(87));
        assertEquals("Product 87 is inactive", ex.getMessage());
    }

    @Test
    void testZeroProductId() {
        Exception ex = assertThrows(IllegalArgumentException.class, () ->
                viewProductDetailsService.getProductDetails(0));
        assertEquals("product_id must be a positive integer", ex.getMessage());
    }

    @Test
    void testNegativeProductId() {
        Exception ex = assertThrows(IllegalArgumentException.class, () ->
                viewProductDetailsService.getProductDetails(-5));
        assertEquals("product_id must be a positive integer", ex.getMessage());
    }

    @Test
    void testNonIntegerInput() {
        Exception ex = assertThrows(NumberFormatException.class, () -> {
            String badInput = "abc";
            int id = Integer.parseInt(badInput);
            viewProductDetailsService.getProductDetails(id);
        });
        assertTrue(ex.getMessage().contains("For input string"));
    }
}

