package com.hustict.aims.service;

import com.hustict.aims.dto.product.ProductDetailDTO;
import com.hustict.aims.exception.ProductNotFoundException;
import com.hustict.aims.exception.ProductOperationException;
import com.hustict.aims.exception.ProductTypeException;
import com.hustict.aims.exception.ProductValidationException;
import com.hustict.aims.model.product.Book;
import com.hustict.aims.model.product.Product;
import com.hustict.aims.repository.product.ProductRepository;
import com.hustict.aims.service.factory.ProductFactory;
import com.hustict.aims.service.factory.ProductFactoryProvider;
import com.hustict.aims.service.product.ImageService;
import com.hustict.aims.service.product.ProductActionService;
import com.hustict.aims.service.product.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock private ImageService imageService;
    @Mock private ProductFactoryProvider factoryProvider;
    @Mock private ProductActionService actionService;
    @Mock private MessageService messageService;
    @Mock private ProductRepository productRepo;
    @Mock private ProductFactory<Product, ProductDetailDTO> productFactory;

    @InjectMocks private ProductService productService;

    private Map<String, Object> validProductData;
    private Product validProduct;
    private ProductDetailDTO expectedDTO;
    private MockMultipartFile mockImage;
    private MockMultipartFile emptyImage;

    @BeforeEach
    void setUp() {
        validProductData = new HashMap<>();
        validProductData.put("category", "Book");
        validProductData.put("title", "Test Book");
        validProductData.put("value", 100000);
        validProductData.put("currentPrice", 90000);
        validProductData.put("barcode", "BOOK001");
        validProductData.put("description", "A test book");
        validProductData.put("quantity", 10);
        validProductData.put("entryDate", "2025-01-15");
        validProductData.put("dimension", "13x20x2 cm");
        validProductData.put("weight", 0.3);
        validProductData.put("rushOrderSupported", true);
        validProductData.put("authors", "Test Author");
        validProductData.put("coverType", "Hardcover");
        validProductData.put("publisher", "Test Publisher");
        validProductData.put("publicationDate", "2024-01-01");
        validProductData.put("numPages", 300);
        validProductData.put("language", "English");
        validProductData.put("genre", "Fiction");

        validProduct = new Book();
        validProduct.setTitle("Test Book");
        validProduct.setValue(100000);
        validProduct.setCurrentPrice(90000);
        validProduct.setBarcode("BOOK001");
        validProduct.setDescription("A test book");
        validProduct.setQuantity(10);
        validProduct.setEntryDate(LocalDate.of(2025, 1, 15));
        validProduct.setDimension("13x20x2 cm");
        validProduct.setWeight(0.3);
        validProduct.setRushOrderSupported(true);

        expectedDTO = new ProductDetailDTO();
        expectedDTO.setId(1L);
        expectedDTO.setTitle("Test Book");
        expectedDTO.setValue(100000);
        expectedDTO.setCurrentPrice(90000);
        expectedDTO.setBarcode("BOOK001");
        expectedDTO.setDescription("A test book");
        expectedDTO.setQuantity(10);
        expectedDTO.setEntryDate(LocalDate.of(2025, 1, 15));
        expectedDTO.setDimension("13x20x2 cm");
        expectedDTO.setWeight(0.3);
        expectedDTO.setRushOrderSupported(true);
        expectedDTO.setCategory("Book");

        mockImage = new MockMultipartFile(
                "image", "test-image.jpg", "image/jpeg", "test image content".getBytes()
        );

        emptyImage = new MockMultipartFile(
                "image", "empty.jpg", "image/jpeg", new byte[0]
        );

        lenient().when(messageService.getInvalidInput()).thenReturn("Invalid input provided");
        lenient().when(messageService.getValidationError()).thenReturn("Validation failed");
    }

    @Test
    void createProduct_Success_WithImage() {
        String expectedImageUrl = "http://example.com/test-image.jpg";
        
        when(imageService.upload(mockImage)).thenReturn(expectedImageUrl);
        when(factoryProvider.getFactory("Book")).thenReturn(Optional.of(productFactory));
        when(productFactory.createProduct(any())).thenReturn(expectedDTO);

        ProductDetailDTO result = productService.createProduct(validProductData, mockImage);

        assertNotNull(result);
        assertEquals(expectedDTO.getId(), result.getId());
        assertEquals(expectedDTO.getTitle(), result.getTitle());

        verify(imageService).upload(mockImage);
        verify(factoryProvider).getFactory("Book");
        verify(productFactory).createProduct(argThat(data ->
                data.containsKey("imageUrl") &&
                        data.get("imageUrl").equals(expectedImageUrl)
        ));
    }

    @Test
    void createProduct_Success_WithoutImage() {
        when(factoryProvider.getFactory("Book")).thenReturn(Optional.of(productFactory));
        when(productFactory.createProduct(validProductData)).thenReturn(expectedDTO);

        ProductDetailDTO result = productService.createProduct(validProductData, null);

        assertNotNull(result);
        assertEquals(expectedDTO.getId(), result.getId());

        verify(imageService, never()).upload(any());
        verify(factoryProvider).getFactory("Book");
        verify(productFactory).createProduct(validProductData);
    }

    @Test
    void createProduct_Success_WithEmptyImage() {
        when(factoryProvider.getFactory("Book")).thenReturn(Optional.of(productFactory));
        when(productFactory.createProduct(validProductData)).thenReturn(expectedDTO);

        ProductDetailDTO result = productService.createProduct(validProductData, emptyImage);

        assertNotNull(result);
        assertEquals(expectedDTO.getId(), result.getId());

        verify(imageService, never()).upload(any());
        verify(factoryProvider).getFactory("Book");
        verify(productFactory).createProduct(validProductData);
    }

    @Test
    void createProduct_MissingCategory_ThrowsException() {
        Map<String, Object> invalidData = new HashMap<>(validProductData);
        invalidData.remove("category");

        ProductOperationException exception = assertThrows(
                ProductOperationException.class,
                () -> productService.createProduct(invalidData, null)
        );

        assertTrue(exception.getMessage().contains("Missing category field"));
        verify(factoryProvider, never()).getFactory(any());
        verify(imageService, never()).upload(any());
    }

    @Test
    void createProduct_UnsupportedCategory_ThrowsException() {
        Map<String, Object> invalidData = new HashMap<>(validProductData);
        invalidData.put("category", "InvalidCategory");

        when(factoryProvider.getFactory("InvalidCategory")).thenReturn(Optional.empty());

        ProductTypeException exception = assertThrows(
                ProductTypeException.class,
                () -> productService.createProduct(invalidData, null)
        );

        assertTrue(exception.getMessage().contains("InvalidCategory"));
        verify(factoryProvider).getFactory("InvalidCategory");
        verify(productFactory, never()).createProduct(any());
    }

    @Test
    void createProduct_ValidationErrors_ThrowsException() {
        List<String> errors = List.of("Title is required", "Price must be positive");
        ProductValidationException validationException = new ProductValidationException(errors);

        when(factoryProvider.getFactory("Book")).thenReturn(Optional.of(productFactory));
        when(productFactory.createProduct(validProductData)).thenThrow(validationException);

        ProductValidationException exception = assertThrows(
                ProductValidationException.class,
                () -> productService.createProduct(validProductData, null)
        );

        assertEquals(errors, exception.getValidationErrors());
        verify(factoryProvider).getFactory("Book");
        verify(productFactory).createProduct(validProductData);
    }

    @Test
    void createProduct_ImageUploadFailure_ThrowsException() {
        when(imageService.upload(mockImage)).thenThrow(new RuntimeException("Upload failed"));

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> productService.createProduct(validProductData, mockImage)
        );

        assertTrue(exception.getMessage().contains("Upload failed"));
        verify(imageService).upload(mockImage);
        verify(factoryProvider, never()).getFactory(any());
    }

    @Test
    void updateProduct_Success_WithImage() {
        Long productId = 1L;
        Long userId = 10L;
        String expectedImageUrl = "http://example.com/updated-image.jpg";
        
        Product mockProduct = mock(Product.class);
        when(mockProduct.getCategory()).thenReturn("Book");
        
        when(productRepo.findById(productId)).thenReturn(Optional.of(mockProduct));
        when(imageService.upload(mockImage)).thenReturn(expectedImageUrl);
        when(factoryProvider.getFactory("Book")).thenReturn(Optional.of(productFactory));
        when(productFactory.updateProduct(eq(mockProduct), any())).thenReturn(expectedDTO);

        ProductDetailDTO result = productService.updateProduct(productId, validProductData, mockImage, userId);

        assertNotNull(result);
        assertEquals(expectedDTO.getId(), result.getId());

        verify(actionService).validateUpdate(userId, productId, validProductData);
        verify(productRepo).findById(productId);
        verify(imageService).upload(mockImage);
        verify(factoryProvider).getFactory("Book");
        verify(productFactory).updateProduct(eq(mockProduct), argThat(data ->
                data.containsKey("imageUrl") &&
                        data.get("imageUrl").equals(expectedImageUrl)
        ));
        verify(actionService).logProductAction(eq(userId), eq(productId), any());
    }

    @Test
    void updateProduct_ProductNotFound_ThrowsException() {
        Long productId = 1L;
        Long userId = 10L;

        when(productRepo.findById(productId)).thenReturn(Optional.empty());

        ProductNotFoundException exception = assertThrows(
                ProductNotFoundException.class,
                () -> productService.updateProduct(productId, validProductData, null, userId)
        );

        assertTrue(exception.getMessage().contains(productId.toString()));
        verify(actionService).validateUpdate(userId, productId, validProductData);
        verify(productRepo).findById(productId);
        verify(factoryProvider, never()).getFactory(any());
    }

    @Test
    void viewProduct_Success() {
        Long productId = 1L;

        Product mockProduct = mock(Product.class);
        when(mockProduct.getCategory()).thenReturn("Book");

        when(productRepo.findById(productId)).thenReturn(Optional.of(mockProduct));
        when(factoryProvider.getFactory("Book")).thenReturn(Optional.of(productFactory));
        when(productFactory.viewProduct(mockProduct)).thenReturn(expectedDTO);

        ProductDetailDTO result = productService.viewProduct(productId);

        assertNotNull(result);
        assertEquals(expectedDTO.getId(), result.getId());

        verify(productRepo).findById(productId);
        verify(factoryProvider).getFactory("Book");
        verify(productFactory).viewProduct(mockProduct);
    }

    @Test
    void viewProduct_ProductNotFound_ThrowsException() {
        Long productId = 1L;

        when(productRepo.findById(productId)).thenReturn(Optional.empty());

        ProductNotFoundException exception = assertThrows(
                ProductNotFoundException.class,
                () -> productService.viewProduct(productId)
        );

        assertTrue(exception.getMessage().contains(productId.toString()));
        verify(productRepo).findById(productId);
        verify(factoryProvider, never()).getFactory(any());
    }

    @Test
    void viewProduct_UnsupportedCategory_ThrowsException() {
        Long productId = 1L;
        Product mockProduct = mock(Product.class);
        when(mockProduct.getCategory()).thenReturn("InvalidCategory");

        when(productRepo.findById(productId)).thenReturn(Optional.of(mockProduct));
        when(factoryProvider.getFactory("InvalidCategory")).thenReturn(Optional.empty());

        ProductTypeException exception = assertThrows(
                ProductTypeException.class,
                () -> productService.viewProduct(productId)
        );

        assertTrue(exception.getMessage().contains("InvalidCategory"));
        verify(productRepo).findById(productId);
        verify(factoryProvider).getFactory("InvalidCategory");
    }
}
