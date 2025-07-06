package com.hustict.aims.service;

import com.hustict.aims.dto.product.ProductDTO;
import com.hustict.aims.dto.product.ProductModifyRequest;
import com.hustict.aims.exception.ProductNotFoundException;
import com.hustict.aims.exception.ProductOperationException;
import com.hustict.aims.exception.ProductTypeException;
import com.hustict.aims.model.product.Book;
import com.hustict.aims.model.product.Product;
import com.hustict.aims.model.user.ActionType;
import com.hustict.aims.repository.product.ProductRepository;
import com.hustict.aims.service.factory.ProductFactory;
import com.hustict.aims.service.factory.ProductFactoryProvider;
import com.hustict.aims.service.product.ImageService;
import com.hustict.aims.service.product.ProductActionService;
import com.hustict.aims.service.product.ProductService;
import com.hustict.aims.service.rules.ActionTypeStrategy;
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
    @Mock private ProductRepository productRepo;
    @Mock private ProductFactory productFactory;
    @Mock private ActionTypeStrategy actionTypeStrategy;

    @InjectMocks private ProductService productService;

    private ProductModifyRequest validRequest;
    private ProductDTO validProductDTO;
    private Product validProduct;
    private ProductDTO expectedDTO;
    private MockMultipartFile mockImage;
    private MockMultipartFile emptyImage;

    @BeforeEach
    void setUp() {
        // Setup ProductDTO
        validProductDTO = new ProductDTO();
        validProductDTO.setId(1L);
        validProductDTO.setTitle("Test Book");
        validProductDTO.setValue(100000);
        validProductDTO.setCurrentPrice(90000);
        validProductDTO.setBarcode("BOOK001");
        validProductDTO.setDescription("A test book");
        validProductDTO.setQuantity(10);
        validProductDTO.setEntryDate(LocalDate.of(2025, 1, 15));
        validProductDTO.setDimension("13x20x2 cm");
        validProductDTO.setWeight(0.3);
        validProductDTO.setRushOrderSupported(true);
        validProductDTO.setCategory("Book");

        // Setup ProductModifyRequest
        Map<String, Object> specificData = new HashMap<>();
        specificData.put("authors", "Test Author");
        specificData.put("coverType", "Hardcover");
        specificData.put("publisher", "Test Publisher");
        specificData.put("publicationDate", "2024-01-01");
        specificData.put("numPages", 300);
        specificData.put("language", "English");
        specificData.put("genre", "Fiction");
        
        validRequest = new ProductModifyRequest(validProductDTO, specificData);

        // Setup Product entity
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
        // Category is set automatically by the Book constructor

        // Setup expected DTO
        expectedDTO = new ProductDTO();
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

        // Setup mock images
        mockImage = new MockMultipartFile(
                "image", "test-image.jpg", "image/jpeg", "test image content".getBytes()
        );

        emptyImage = new MockMultipartFile(
                "image", "empty.jpg", "image/jpeg", new byte[0]
        );
    }

    @Test
    void createProduct_Success_WithImage() {
        String expectedImageUrl = "http://example.com/test-image.jpg";
        
        when(imageService.upload(mockImage)).thenReturn(expectedImageUrl);
        when(factoryProvider.getFactory("Book")).thenReturn(Optional.of(productFactory));
        when(productFactory.createProduct(any(ProductModifyRequest.class))).thenReturn(expectedDTO);

        ProductDTO result = productService.createProduct(validRequest, mockImage);

        assertNotNull(result);
        assertEquals(expectedDTO.getId(), result.getId());
        assertEquals(expectedDTO.getTitle(), result.getTitle());

        verify(imageService).upload(mockImage);
        verify(factoryProvider).getFactory("Book");
        verify(productFactory).createProduct(argThat(request ->
                request.getProduct().getImageUrl() != null &&
                request.getProduct().getImageUrl().equals(expectedImageUrl)
        ));
    }

    @Test
    void createProduct_Success_WithoutImage() {
        when(factoryProvider.getFactory("Book")).thenReturn(Optional.of(productFactory));
        when(productFactory.createProduct(validRequest)).thenReturn(expectedDTO);

        ProductDTO result = productService.createProduct(validRequest, null);

        assertNotNull(result);
        assertEquals(expectedDTO.getId(), result.getId());

        verify(imageService, never()).upload(any());
        verify(factoryProvider).getFactory("Book");
        verify(productFactory).createProduct(validRequest);
    }

    @Test
    void createProduct_Success_WithEmptyImage() {
        when(factoryProvider.getFactory("Book")).thenReturn(Optional.of(productFactory));
        when(productFactory.createProduct(validRequest)).thenReturn(expectedDTO);

        ProductDTO result = productService.createProduct(validRequest, emptyImage);

        assertNotNull(result);
        assertEquals(expectedDTO.getId(), result.getId());

        verify(imageService, never()).upload(any());
        verify(factoryProvider).getFactory("Book");
        verify(productFactory).createProduct(validRequest);
    }

    @Test
    void createProduct_MissingCategory_ThrowsException() {
        ProductDTO invalidProductDTO = new ProductDTO();
        invalidProductDTO.setTitle("Test Book");
        // Missing category
        ProductModifyRequest invalidRequest = new ProductModifyRequest(invalidProductDTO, new HashMap<>());

        ProductOperationException exception = assertThrows(
                ProductOperationException.class,
                () -> productService.createProduct(invalidRequest, null)
        );

        assertTrue(exception.getMessage().contains("Missing category field"));
        verify(factoryProvider, never()).getFactory(any());
        verify(imageService, never()).upload(any());
    }

    @Test
    void createProduct_UnsupportedCategory_ThrowsException() {
        ProductDTO invalidProductDTO = new ProductDTO();
        invalidProductDTO.setCategory("InvalidCategory");
        ProductModifyRequest invalidRequest = new ProductModifyRequest(invalidProductDTO, new HashMap<>());

        when(factoryProvider.getFactory("InvalidCategory")).thenReturn(Optional.empty());

        ProductTypeException exception = assertThrows(
                ProductTypeException.class,
                () -> productService.createProduct(invalidRequest, null)
        );

        assertTrue(exception.getMessage().contains("InvalidCategory"));
        verify(factoryProvider).getFactory("InvalidCategory");
        verify(productFactory, never()).createProduct(any());
    }

    @Test
    void createProduct_ImageUploadFailure_ThrowsException() {
        when(imageService.upload(mockImage)).thenThrow(new RuntimeException("Upload failed"));

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> productService.createProduct(validRequest, mockImage)
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
        
        when(productRepo.findByIdNotDeleted(productId)).thenReturn(Optional.of(validProduct));
        when(imageService.upload(mockImage)).thenReturn(expectedImageUrl);
        when(factoryProvider.getFactory("Book")).thenReturn(Optional.of(productFactory));
        when(productFactory.updateProduct(eq(validProduct), any(ProductModifyRequest.class))).thenReturn(expectedDTO);
        when(actionTypeStrategy.determine(eq(validProduct), any(ProductModifyRequest.class))).thenReturn(ActionType.UPDATE);

        ProductDTO result = productService.updateProduct(productId, validRequest, mockImage, userId);

        assertNotNull(result);
        assertEquals(expectedDTO.getId(), result.getId());

        verify(actionService).validateUpdate(userId, productId, validRequest);
        verify(productRepo).findByIdNotDeleted(productId);
        verify(imageService).upload(mockImage);
        verify(factoryProvider).getFactory("Book");
        verify(productFactory).updateProduct(eq(validProduct), argThat(request ->
                request.getProduct().getImageUrl() != null &&
                request.getProduct().getImageUrl().equals(expectedImageUrl)
        ));
        verify(actionTypeStrategy).determine(eq(validProduct), any(ProductModifyRequest.class));
        verify(actionService).logProductAction(eq(userId), eq(productId), eq(ActionType.UPDATE));
    }

    @Test
    void updateProduct_ProductNotFound_ThrowsException() {
        Long productId = 1L;
        Long userId = 10L;

        when(productRepo.findByIdNotDeleted(productId)).thenReturn(Optional.empty());

        ProductNotFoundException exception = assertThrows(
                ProductNotFoundException.class,
                () -> productService.updateProduct(productId, validRequest, null, userId)
        );

        assertTrue(exception.getMessage().contains(productId.toString()));
        verify(actionService).validateUpdate(userId, productId, validRequest);
        verify(productRepo).findByIdNotDeleted(productId);
        verify(factoryProvider, never()).getFactory(any());
    }

    @Test
    void updateProduct_UnsupportedCategory_ThrowsException() {
        Long productId = 1L;
        Long userId = 10L;
        Product invalidProduct = mock(Product.class);
        when(invalidProduct.getCategory()).thenReturn("InvalidCategory");

        when(productRepo.findByIdNotDeleted(productId)).thenReturn(Optional.of(invalidProduct));
        when(factoryProvider.getFactory("InvalidCategory")).thenReturn(Optional.empty());

        ProductTypeException exception = assertThrows(
                ProductTypeException.class,
                () -> productService.updateProduct(productId, validRequest, null, userId)
        );

        assertTrue(exception.getMessage().contains("InvalidCategory"));
        verify(actionService).validateUpdate(userId, productId, validRequest);
        verify(productRepo).findByIdNotDeleted(productId);
        verify(factoryProvider).getFactory("InvalidCategory");
    }

    @Test
    void viewProduct_Success() {
        Long productId = 1L;

        when(productRepo.findByIdNotDeleted(productId)).thenReturn(Optional.of(validProduct));
        when(factoryProvider.getFactory("Book")).thenReturn(Optional.of(productFactory));
        when(productFactory.viewProduct(validProduct)).thenReturn(expectedDTO);

        ProductDTO result = productService.viewProduct(productId);

        assertNotNull(result);
        assertEquals(expectedDTO.getId(), result.getId());

        verify(productRepo).findByIdNotDeleted(productId);
        verify(factoryProvider).getFactory("Book");
        verify(productFactory).viewProduct(validProduct);
    }

    @Test
    void viewProduct_ProductNotFound_ThrowsException() {
        Long productId = 1L;

        when(productRepo.findByIdNotDeleted(productId)).thenReturn(Optional.empty());

        ProductNotFoundException exception = assertThrows(
                ProductNotFoundException.class,
                () -> productService.viewProduct(productId)
        );

        assertTrue(exception.getMessage().contains(productId.toString()));
        verify(productRepo).findByIdNotDeleted(productId);
        verify(factoryProvider, never()).getFactory(any());
    }

    @Test
    void viewProduct_UnsupportedCategory_ThrowsException() {
        Long productId = 1L;
        Product invalidProduct = mock(Product.class);
        when(invalidProduct.getCategory()).thenReturn("InvalidCategory");

        when(productRepo.findByIdNotDeleted(productId)).thenReturn(Optional.of(invalidProduct));
        when(factoryProvider.getFactory("InvalidCategory")).thenReturn(Optional.empty());

        ProductTypeException exception = assertThrows(
                ProductTypeException.class,
                () -> productService.viewProduct(productId)
        );

        assertTrue(exception.getMessage().contains("InvalidCategory"));
        verify(productRepo).findByIdNotDeleted(productId);
        verify(factoryProvider).getFactory("InvalidCategory");
    }

    @Test
    void deleteProducts_Success() {
        Long userId = 10L;
        List<Long> productIds = Arrays.asList(1L, 2L, 3L);
        List<Product> products = Arrays.asList(validProduct, validProduct, validProduct);

        when(productRepo.findAllByIdNotDeleted(productIds)).thenReturn(products);

        productService.deleteProducts(userId, productIds);

        verify(actionService).validateDelete(userId, productIds);
        verify(productRepo).findAllByIdNotDeleted(productIds);
        for (Long productId : productIds) {
            verify(actionService).logProductAction(userId, productId, ActionType.DELETE);
            verify(productRepo).softDeleteById(productId);
        }
    }

    @Test
    void deleteProducts_EmptyList_ThrowsException() {
        Long userId = 10L;
        List<Long> productIds = new ArrayList<>();

        ProductOperationException exception = assertThrows(
                ProductOperationException.class,
                () -> productService.deleteProducts(userId, productIds)
        );

        assertTrue(exception.getMessage().contains("Product IDs cannot be empty"));
        verify(actionService, never()).validateDelete(any(), any());
        verify(productRepo, never()).findAllByIdNotDeleted(any());
    }

    @Test
    void deleteProducts_TooManyProducts_ThrowsException() {
        Long userId = 10L;
        List<Long> productIds = new ArrayList<>();
        for (int i = 0; i < 11; i++) {
            productIds.add((long) i);
        }

        ProductOperationException exception = assertThrows(
                ProductOperationException.class,
                () -> productService.deleteProducts(userId, productIds)
        );

        assertTrue(exception.getMessage().contains("Cannot delete more than 10 products"));
        verify(actionService, never()).validateDelete(any(), any());
        verify(productRepo, never()).findAllByIdNotDeleted(any());
    }

    @Test
    void deleteProducts_ProductNotFound_ThrowsException() {
        Long userId = 10L;
        List<Long> productIds = Arrays.asList(1L, 2L, 3L);
        List<Product> foundProducts = Arrays.asList(validProduct, validProduct); // Only 2 products found

        when(productRepo.findAllByIdNotDeleted(productIds)).thenReturn(foundProducts);

        ProductNotFoundException exception = assertThrows(
                ProductNotFoundException.class,
                () -> productService.deleteProducts(userId, productIds)
        );

        assertTrue(exception.getMessage().contains("One or more products not found"));
        verify(actionService).validateDelete(userId, productIds);
        verify(productRepo).findAllByIdNotDeleted(productIds);
        verify(productRepo, never()).softDeleteById(any());
    }
}
