package com.hustict.aims.service;

import com.hustict.aims.dto.product.ProductDetailDTO;
import com.hustict.aims.model.product.Book;
import com.hustict.aims.model.product.Product;
import com.hustict.aims.repository.ReservationItemRepository;
import com.hustict.aims.repository.product.ProductRepository;
import com.hustict.aims.service.product.ProductComponentService;
import com.hustict.aims.service.handler.ProductHandler;
import com.hustict.aims.service.product.ProductActionService;
import com.hustict.aims.service.product.ProductService;
import com.hustict.aims.service.storage.ImageUploadStorage;
import com.hustict.aims.service.validation.ProductValidator;
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

    @Mock private ImageUploadStorage uploadService;
    @Mock private ProductComponentService componentService;
    @Mock private ProductRepository productRepo;
    @Mock private ProductActionService actionService;
    @Mock private ReservationItemRepository reservationItemRepository;
    @Mock private MessageService messageService;
    @Mock private ProductHandler productHandler;
    @Mock private ProductValidator<Product> productValidator;

    @InjectMocks private ProductService productService;

    private Map<String, Object> validProductData;
    private Product validProduct;
    private ProductDetailDTO expectedDTO;
    private MockMultipartFile mockImage;

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

        lenient().when(messageService.getInvalidInput()).thenReturn("Invalid input provided");
        lenient().when(messageService.getValidationError()).thenReturn("Validation failed");
    }

    @Test
    void createProduct_Success_WithoutImage() {
        when(componentService.supports("Book")).thenReturn(true);
        when(componentService.getHandler("Book")).thenReturn(productHandler);
        when(productHandler.toEntity(validProductData)).thenReturn(validProduct);
        when(componentService.getValidator("Book")).thenReturn(productValidator);
        when(productValidator.validate(validProduct)).thenReturn(Collections.emptyList());
        when(productHandler.save(validProduct)).thenReturn(validProduct);
        when(productHandler.toDTO(validProduct)).thenReturn(expectedDTO);

        ProductDetailDTO result = productService.createProduct(validProductData, null);

        assertNotNull(result);
        assertEquals(expectedDTO.getId(), result.getId());
        assertEquals(expectedDTO.getTitle(), result.getTitle());
        assertEquals(expectedDTO.getCategory(), result.getCategory());

        verify(componentService).supports("Book");
        verify(componentService).getHandler("Book");
        verify(productHandler).toEntity(validProductData);
        verify(productHandler).save(validProduct);
        verify(productHandler).toDTO(validProduct);
        verify(componentService).getValidator("Book");
        verify(productValidator).validate(validProduct);
        verify(uploadService, never()).upload(any(), any(), any());
    }

    @Test
    void createProduct_Success_WithImage() throws Exception {
        String expectedImageUrl = "http://example.com/test-image.jpg";
        when(uploadService.upload(any(), any(), any())).thenReturn(expectedImageUrl);
        when(componentService.supports("Book")).thenReturn(true);
        when(componentService.getHandler("Book")).thenReturn(productHandler);
        when(productHandler.toEntity(any())).thenReturn(validProduct);
        when(componentService.getValidator("Book")).thenReturn(productValidator);
        when(productValidator.validate(validProduct)).thenReturn(Collections.emptyList());
        when(productHandler.save(validProduct)).thenReturn(validProduct);
        when(productHandler.toDTO(validProduct)).thenReturn(expectedDTO);

        ProductDetailDTO result = productService.createProduct(validProductData, mockImage);

        assertNotNull(result);
        assertEquals(expectedDTO.getId(), result.getId());

        verify(uploadService).upload(eq("test image content".getBytes()), contains("test-image.jpg"), eq("image/jpeg"));
        verify(productHandler).toEntity(argThat(data ->
                data.containsKey("imageUrl") &&
                        data.get("imageUrl").equals(expectedImageUrl)
        ));
        verify(productHandler).save(validProduct);
        verify(productHandler).toDTO(validProduct);
    }

    @Test
    void createProduct_MissingCategory_ThrowsException() {
        Map<String, Object> invalidData = new HashMap<>(validProductData);
        invalidData.remove("category");

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> productService.createProduct(invalidData, null)
        );

        assertTrue(exception.getMessage().contains("Missing category field"));
        verify(componentService, never()).supports(any());
    }

    @Test
    void createProduct_UnsupportedCategory_ThrowsException() {
        when(componentService.supports("InvalidCategory")).thenReturn(false);

        Map<String, Object> invalidData = new HashMap<>(validProductData);
        invalidData.put("category", "InvalidCategory");

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> productService.createProduct(invalidData, null)
        );

        assertTrue(exception.getMessage().contains("Unsupported category: InvalidCategory"));
        verify(componentService).supports("InvalidCategory");
        verify(componentService, never()).getHandler(any());
    }

    @Test
    void createProduct_ValidationErrors_ThrowsException() {
        List<String> errors = List.of("Title is required", "Price must be positive");

        when(componentService.supports("Book")).thenReturn(true);
        when(componentService.getHandler("Book")).thenReturn(productHandler);
        when(productHandler.toEntity(validProductData)).thenReturn(validProduct);
        when(componentService.getValidator("Book")).thenReturn(productValidator);
        when(productValidator.validate(validProduct)).thenReturn(errors);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> productService.createProduct(validProductData, null)
        );

        assertTrue(exception.getMessage().contains("Validation failed"));
        assertTrue(exception.getMessage().contains("Title is required"));
        assertTrue(exception.getMessage().contains("Price must be positive"));

        verify(productHandler, never()).save(any());
        verify(productHandler, never()).toDTO(any());
    }

    @Test
    void createProduct_ImageUploadFailure_ThrowsException() throws Exception {
        when(uploadService.upload(any(), any(), any()))
                .thenThrow(new RuntimeException("Upload failed"));

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> productService.createProduct(validProductData, mockImage)
        );

        assertTrue(exception.getMessage().contains("Failed to upload image"));
        verify(uploadService).upload(any(), any(), any());
        verify(componentService, never()).supports(any());
    }

    @Test
    void createProduct_EmptyImage_ProcessesWithoutImage() {
        MockMultipartFile emptyImage = new MockMultipartFile(
                "image", "empty.jpg", "image/jpeg", new byte[0]
        );

        when(componentService.supports("Book")).thenReturn(true);
        when(componentService.getHandler("Book")).thenReturn(productHandler);
        when(productHandler.toEntity(validProductData)).thenReturn(validProduct);
        when(componentService.getValidator("Book")).thenReturn(productValidator);
        when(productValidator.validate(validProduct)).thenReturn(Collections.emptyList());
        when(productHandler.save(validProduct)).thenReturn(validProduct);
        when(productHandler.toDTO(validProduct)).thenReturn(expectedDTO);

        ProductDetailDTO result = productService.createProduct(validProductData, emptyImage);

        assertNotNull(result);
        assertEquals(expectedDTO.getId(), result.getId());

        verify(uploadService, never()).upload(any(), any(), any());
        verify(productHandler).save(validProduct);
        verify(productHandler).toDTO(validProduct);
    }
}
