package com.hustict.aims.service;

import com.hustict.aims.dto.product.ProductDetailDTO;
import com.hustict.aims.model.product.Book;
import com.hustict.aims.model.product.Product;
import com.hustict.aims.repository.ReservationItemRepository;
import com.hustict.aims.repository.product.ProductRepository;
import com.hustict.aims.service.handler.ProductHandler;
import com.hustict.aims.service.handler.ProductHandlerRegistry;
import com.hustict.aims.service.product.ProductActionService;
import com.hustict.aims.service.product.ProductService;
import com.hustict.aims.service.storage.ImageUploadStorage;
import com.hustict.aims.service.validation.ProductValidator;
import com.hustict.aims.service.validation.ProductValidatorRegistry;
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

    @Mock
    private ImageUploadStorage uploadService;

    @Mock
    private ProductHandlerRegistry handlerReg;

    @Mock
    private ProductValidatorRegistry validatorReg;

    @Mock
    private ProductRepository productRepo;

    @Mock
    private ProductActionService actionService;

    @Mock
    private ReservationItemRepository reservationItemRepository;

    @Mock
    private MessageService messageService;

    @Mock
    private ProductHandler productHandler;

    @Mock
    private ProductValidator<Product> productValidator;

    @InjectMocks
    private ProductService productService;

    private Map<String, Object> validProductData;
    private Product validProduct;
    private ProductDetailDTO expectedDTO;
    private MockMultipartFile mockImage;

    @BeforeEach
    void setUp() {
        // Setup valid product data
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

        // Setup valid product
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

        // Setup expected DTO
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

        // Setup mock image
        mockImage = new MockMultipartFile(
            "image",
            "test-image.jpg",
            "image/jpeg",
            "test image content".getBytes()
        );

        // Setup message service responses - using lenient to avoid UnnecessaryStubbingException
        lenient().when(messageService.getInvalidInput()).thenReturn("Invalid input provided");
        lenient().when(messageService.getValidationError()).thenReturn("Validation failed");
    }

    @Test
    void createProduct_Success_WithoutImage() {
        // Arrange
        when(handlerReg.getHandler("Book")).thenReturn(Optional.of(productHandler));
        when(productHandler.toEntity(validProductData)).thenReturn(validProduct);
        when(validatorReg.getValidator("Book")).thenReturn(Optional.of(productValidator));
        when(productValidator.validate(validProduct)).thenReturn(Collections.emptyList());
        when(productHandler.saveAndReturnDTO(validProduct)).thenReturn(expectedDTO);

        // Act
        ProductDetailDTO result = productService.createProduct(validProductData, null);

        // Assert
        assertNotNull(result);
        assertEquals(expectedDTO.getId(), result.getId());
        assertEquals(expectedDTO.getTitle(), result.getTitle());
        assertEquals(expectedDTO.getCategory(), result.getCategory());
        
        verify(handlerReg).getHandler("Book");
        verify(productHandler).toEntity(validProductData);
        verify(validatorReg).getValidator("Book");
        verify(productValidator).validate(validProduct);
        verify(productHandler).saveAndReturnDTO(validProduct);
        verify(uploadService, never()).upload(any(), any(), any());
    }

    @Test
    void createProduct_Success_WithImage() throws Exception {
        // Arrange
        String expectedImageUrl = "http://example.com/test-image.jpg";
        when(uploadService.upload(any(byte[].class), anyString(), anyString()))
            .thenReturn(expectedImageUrl);
        when(handlerReg.getHandler("Book")).thenReturn(Optional.of(productHandler));
        when(productHandler.toEntity(any())).thenReturn(validProduct);
        when(validatorReg.getValidator("Book")).thenReturn(Optional.of(productValidator));
        when(productValidator.validate(validProduct)).thenReturn(Collections.emptyList());
        when(productHandler.saveAndReturnDTO(validProduct)).thenReturn(expectedDTO);

        // Act
        ProductDetailDTO result = productService.createProduct(validProductData, mockImage);

        // Assert
        assertNotNull(result);
        assertEquals(expectedDTO.getId(), result.getId());
        
        verify(uploadService).upload(
            eq("test image content".getBytes()),
            contains("test-image.jpg"),
            eq("image/jpeg")
        );
        verify(productHandler).toEntity(argThat(data -> 
            data.containsKey("imageUrl") && 
            data.get("imageUrl").equals(expectedImageUrl)
        ));
    }

    @Test
    void createProduct_MissingCategory_ThrowsException() {
        // Arrange
        Map<String, Object> invalidData = new HashMap<>(validProductData);
        invalidData.remove("category");

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> productService.createProduct(invalidData, null)
        );
        
        assertTrue(exception.getMessage().contains("Missing category field"));
        verify(handlerReg, never()).getHandler(any());
    }

    @Test
    void createProduct_NullCategory_ThrowsException() {
        // Arrange
        Map<String, Object> invalidData = new HashMap<>(validProductData);
        invalidData.put("category", null);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> productService.createProduct(invalidData, null)
        );
        
        assertTrue(exception.getMessage().contains("Missing category field"));
        verify(handlerReg, never()).getHandler(any());
    }

    @Test
    void createProduct_UnsupportedCategory_ThrowsException() {
        // Arrange
        when(handlerReg.getHandler("InvalidCategory")).thenReturn(Optional.empty());

        Map<String, Object> invalidData = new HashMap<>(validProductData);
        invalidData.put("category", "InvalidCategory");

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> productService.createProduct(invalidData, null)
        );
        
        assertTrue(exception.getMessage().contains("Unsupported category: InvalidCategory"));
        verify(handlerReg).getHandler("InvalidCategory");
        verify(validatorReg, never()).getValidator(any());
    }

    @Test
    void createProduct_NoValidator_ThrowsException() {
        // Arrange
        when(handlerReg.getHandler("Book")).thenReturn(Optional.of(productHandler));
        when(productHandler.toEntity(validProductData)).thenReturn(validProduct);
        when(validatorReg.getValidator("Book")).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> productService.createProduct(validProductData, null)
        );
        
        assertTrue(exception.getMessage().contains("No validator for: Book"));
        verify(handlerReg).getHandler("Book");
        verify(productHandler).toEntity(validProductData);
        verify(validatorReg).getValidator("Book");
        verify(productHandler, never()).saveAndReturnDTO(any());
    }

    @Test
    void createProduct_ValidationErrors_ThrowsException() {
        // Arrange
        List<String> validationErrors = Arrays.asList("Title is required", "Price must be positive");
        
        when(handlerReg.getHandler("Book")).thenReturn(Optional.of(productHandler));
        when(productHandler.toEntity(validProductData)).thenReturn(validProduct);
        when(validatorReg.getValidator("Book")).thenReturn(Optional.of(productValidator));
        when(productValidator.validate(validProduct)).thenReturn(validationErrors);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> productService.createProduct(validProductData, null)
        );
        
        assertTrue(exception.getMessage().contains("Validation failed"));
        assertTrue(exception.getMessage().contains("Title is required"));
        assertTrue(exception.getMessage().contains("Price must be positive"));
        
        verify(handlerReg).getHandler("Book");
        verify(productHandler).toEntity(validProductData);
        verify(validatorReg).getValidator("Book");
        verify(productValidator).validate(validProduct);
        verify(productHandler, never()).saveAndReturnDTO(any());
    }

    @Test
    void createProduct_ImageUploadFailure_ThrowsException() throws Exception {
        // Arrange
        when(uploadService.upload(any(byte[].class), anyString(), anyString()))
            .thenThrow(new RuntimeException("Upload failed"));

        // Act & Assert
        RuntimeException exception = assertThrows(
            RuntimeException.class,
            () -> productService.createProduct(validProductData, mockImage)
        );
        
        assertTrue(exception.getMessage().contains("Failed to upload image"));
        assertTrue(exception.getMessage().contains("Upload failed"));
        
        verify(uploadService).upload(any(byte[].class), anyString(), anyString());
        verify(handlerReg, never()).getHandler(any());
    }

    @Test
    void createProduct_EmptyImage_ProcessesWithoutImage() {
        // Arrange
        MockMultipartFile emptyImage = new MockMultipartFile(
            "image",
            "empty.jpg",
            "image/jpeg",
            new byte[0]
        );
        
        when(handlerReg.getHandler("Book")).thenReturn(Optional.of(productHandler));
        when(productHandler.toEntity(validProductData)).thenReturn(validProduct);
        when(validatorReg.getValidator("Book")).thenReturn(Optional.of(productValidator));
        when(productValidator.validate(validProduct)).thenReturn(Collections.emptyList());
        when(productHandler.saveAndReturnDTO(validProduct)).thenReturn(expectedDTO);

        // Act
        ProductDetailDTO result = productService.createProduct(validProductData, emptyImage);

        // Assert
        assertNotNull(result);
        assertEquals(expectedDTO.getId(), result.getId());
        
        verify(uploadService, never()).upload(any(), any(), any());
        verify(productHandler).toEntity(validProductData);
    }

    @Test
    void createProduct_HandlerSaveFailure_ThrowsException() {
        // Arrange
        when(handlerReg.getHandler("Book")).thenReturn(Optional.of(productHandler));
        when(productHandler.toEntity(validProductData)).thenReturn(validProduct);
        when(validatorReg.getValidator("Book")).thenReturn(Optional.of(productValidator));
        when(productValidator.validate(validProduct)).thenReturn(Collections.emptyList());
        when(productHandler.saveAndReturnDTO(validProduct))
            .thenThrow(new RuntimeException("Database error"));

        // Act & Assert
        RuntimeException exception = assertThrows(
            RuntimeException.class,
            () -> productService.createProduct(validProductData, null)
        );
        
        assertEquals("Database error", exception.getMessage());
        
        verify(handlerReg).getHandler("Book");
        verify(productHandler).toEntity(validProductData);
        verify(validatorReg).getValidator("Book");
        verify(productValidator).validate(validProduct);
        verify(productHandler).saveAndReturnDTO(validProduct);
    }

    @Test
    void createProduct_CDCategory_Success() {
        // Arrange
        Map<String, Object> cdData = new HashMap<>(validProductData);
        cdData.put("category", "CD");
        cdData.put("artists", "Test Artist");
        cdData.put("recordLabel", "Test Label");
        cdData.put("trackList", "Track 1, Track 2, Track 3");
        cdData.put("genre", "Rock");
        cdData.put("releaseDate", "2024-01-01");

        when(handlerReg.getHandler("CD")).thenReturn(Optional.of(productHandler));
        when(productHandler.toEntity(cdData)).thenReturn(validProduct);
        when(validatorReg.getValidator("CD")).thenReturn(Optional.of(productValidator));
        when(productValidator.validate(validProduct)).thenReturn(Collections.emptyList());
        when(productHandler.saveAndReturnDTO(validProduct)).thenReturn(expectedDTO);

        // Act
        ProductDetailDTO result = productService.createProduct(cdData, null);

        // Assert
        assertNotNull(result);
        assertEquals(expectedDTO.getId(), result.getId());
        
        verify(handlerReg).getHandler("CD");
        verify(productHandler).toEntity(cdData);
        verify(validatorReg).getValidator("CD");
    }

    @Test
    void createProduct_DVDCategory_Success() {
        // Arrange
        Map<String, Object> dvdData = new HashMap<>(validProductData);
        dvdData.put("category", "DVD");
        dvdData.put("discType", "DVD");
        dvdData.put("director", "Test Director");
        dvdData.put("runtime", 120);
        dvdData.put("studio", "Test Studio");
        dvdData.put("language", "English");
        dvdData.put("subtitles", "English, Vietnamese");
        dvdData.put("releaseDate", "2024-01-01");
        dvdData.put("genre", "Action");

        when(handlerReg.getHandler("DVD")).thenReturn(Optional.of(productHandler));
        when(productHandler.toEntity(dvdData)).thenReturn(validProduct);
        when(validatorReg.getValidator("DVD")).thenReturn(Optional.of(productValidator));
        when(productValidator.validate(validProduct)).thenReturn(Collections.emptyList());
        when(productHandler.saveAndReturnDTO(validProduct)).thenReturn(expectedDTO);

        // Act
        ProductDetailDTO result = productService.createProduct(dvdData, null);

        // Assert
        assertNotNull(result);
        assertEquals(expectedDTO.getId(), result.getId());
        
        verify(handlerReg).getHandler("DVD");
        verify(productHandler).toEntity(dvdData);
        verify(validatorReg).getValidator("DVD");
    }

    @Test
    void createProduct_LPCategory_Success() {
        // Arrange
        Map<String, Object> lpData = new HashMap<>(validProductData);
        lpData.put("category", "LP");
        lpData.put("artists", "Test Artist");
        lpData.put("recordLabel", "Test Label");
        lpData.put("trackList", "Track 1, Track 2, Track 3");
        lpData.put("genre", "Rock");
        lpData.put("releaseDate", "2024-01-01");

        when(handlerReg.getHandler("LP")).thenReturn(Optional.of(productHandler));
        when(productHandler.toEntity(lpData)).thenReturn(validProduct);
        when(validatorReg.getValidator("LP")).thenReturn(Optional.of(productValidator));
        when(productValidator.validate(validProduct)).thenReturn(Collections.emptyList());
        when(productHandler.saveAndReturnDTO(validProduct)).thenReturn(expectedDTO);

        // Act
        ProductDetailDTO result = productService.createProduct(lpData, null);

        // Assert
        assertNotNull(result);
        assertEquals(expectedDTO.getId(), result.getId());
        
        verify(handlerReg).getHandler("LP");
        verify(productHandler).toEntity(lpData);
        verify(validatorReg).getValidator("LP");
    }
}
