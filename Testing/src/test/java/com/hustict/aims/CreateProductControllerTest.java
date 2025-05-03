package com.hustict.aims;

import com.hustict.aims.controller.CreateProductController;
import com.hustict.aims.exception.*;
import com.hustict.aims.model.*;
import com.hustict.aims.repository.ProductRepository;
import com.hustict.aims.service.validator.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CreateProductControllerTest {

    private ProductRepository repository;
    private CreateProductController controller;

    @BeforeEach
    public void setUp() {
        repository = mock(ProductRepository.class);
        controller = new CreateProductController(repository);
    }

    @Test
    public void testCreateValidBook() throws Exception {
        Book book = new Book("Clean Code", 100, 10, 0.5, true, "img.jpg", "BOOK001",
                "Best practices", "10x20x2",
                Collections.singletonList("Robert C. Martin"), "Hardcover", "Pearson",
                "2020-01-01", 350, "English", "Programming");

        when(repository.isProductExists("BOOK001")).thenReturn(false);

        controller.createProduct(new ProductInfo(book));

        verify(repository).saveProduct(book);
    }

    @Test
    public void testCreateValidCD() throws Exception {
        CD cd = new CD("Best of 90s", 50, 5, 0.3, true, "img.jpg", "CD001",
                "Classic hits", "10x10x1",
                Arrays.asList("Artist A", "Artist B"), "Universal", Arrays.asList("Track1", "Track2"),
                "Pop", "1999-12-31");

        when(repository.isProductExists("CD001")).thenReturn(false);

        controller.createProduct(new ProductInfo(cd));

        verify(repository).saveProduct(cd);
    }

    @Test
    public void testCreateValidDVD() throws Exception {
        DVD dvd = new DVD("Inception", 120, 20, 0.4, false, "img.jpg", "DVD001",
                "A sci-fi thriller", "10x15x2",
                "Blu-ray", "Nolan", 148, "WB", "English", "EN,FR", "Sci-Fi", "2010-07-16");

        when(repository.isProductExists("DVD001")).thenReturn(false);

        controller.createProduct(new ProductInfo(dvd));

        verify(repository).saveProduct(dvd);
    }

    @Test
    public void testCreateValidLP() throws Exception {
        LP lp = new LP("Vinyl Legends", 200, 8, 0.6, true, "img.jpg", "LP001",
                "Limited Edition", "30x30x1",
                Collections.singletonList("The Beatles"), "EMI", Arrays.asList("Track A", "Track B"),
                "Rock", "1970-05-08");

        when(repository.isProductExists("LP001")).thenReturn(false);

        controller.createProduct(new ProductInfo(lp));

        verify(repository).saveProduct(lp);
    }

    @Test
    public void testDuplicateBarcodeThrowsException() {
        Book book = new Book("Clean Code", 100, 10, 0.5, true, "img.jpg", "BOOK001",
                "Best practices", "10x20x2",
                Collections.singletonList("Robert C. Martin"), "Hardcover", "Pearson",
                "2020-01-01", 350, "English", "Programming");

        when(repository.isProductExists("BOOK001")).thenReturn(true);

        assertThrows(DuplicateProductException.class, () -> controller.createProduct(new ProductInfo(book)));
        verify(repository, never()).saveProduct(any());
    }

    @Test
    public void testInvalidCommonFieldThrowsException() {
        DVD dvd = new DVD("", 0, 0, -1.0, true, "", "", "", "",
                "DVD", "Nolan", 148, "WB", "English", "EN", "Sci-Fi", "2010-07-16");

        assertThrows(InvalidProductException.class, () -> controller.createProduct(new ProductInfo(dvd)));
        verify(repository, never()).saveProduct(any());
    }

    @Test
    public void testInvalidSpecificFieldThrowsException() {
        CD cd = new CD("Invalid CD", 50, 5, 0.3, true, "img.jpg", "CD001",
                "Missing tracklist", "10x10x1",
                Arrays.asList("Artist A", "Artist B"), "Label", Collections.emptyList(), // <-- Invalid track list
                "Pop", "1999-12-31");

        assertThrows(InvalidProductException.class, () -> controller.createProduct(new ProductInfo(cd)));
        verify(repository, never()).saveProduct(any());
    }
}