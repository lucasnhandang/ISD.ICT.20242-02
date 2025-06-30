package com.hustict.aims.service.placeRushOrder;

import com.hustict.aims.dto.cart.CartRequestDTO;
import com.hustict.aims.dto.cart.CartItemRequestDTO;
import com.hustict.aims.dto.deliveryForm.DeliveryFormDTO;
import com.hustict.aims.dto.order.RushOrderEligibilityResponseDTO;
import com.hustict.aims.exception.RushOrderException;
import com.hustict.aims.repository.product.ProductRepository;
import com.hustict.aims.service.MessageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RushOrderEligibilityServiceTest {

    @Mock
    private CartSplitter cartSplitter;
    
    @Mock
    private ProductRepository productRepository;
    
    @Mock
    private MessageService messageService;

    @InjectMocks
    private RushOrderEligibilityService rushOrderEligibilityService;

    private CartRequestDTO validCart;
    private DeliveryFormDTO hanoiDeliveryInfo;
    private DeliveryFormDTO nonHanoiDeliveryInfo;
    private CartItemRequestDTO rushItem1;
    private CartItemRequestDTO rushItem2;
    private CartItemRequestDTO normalItem1;

    @BeforeEach
    void setUp() {
        // Tạo test data cho cart items
        rushItem1 = new CartItemRequestDTO();
        rushItem1.setProductID(1L);
        rushItem1.setProductName("Rush Product 1");
        rushItem1.setQuantity(2);
        rushItem1.setPrice(50000);

        rushItem2 = new CartItemRequestDTO();
        rushItem2.setProductID(2L);
        rushItem2.setProductName("Rush Product 2");
        rushItem2.setQuantity(1);
        rushItem2.setPrice(100000);

        normalItem1 = new CartItemRequestDTO();
        normalItem1.setProductID(3L);
        normalItem1.setProductName("Normal Product 1");
        normalItem1.setQuantity(1);
        normalItem1.setPrice(75000);

        // Tạo cart request
        validCart = new CartRequestDTO();
        validCart.setProductList(Arrays.asList(rushItem1, rushItem2, normalItem1));
        validCart.setTotalPrice(275000);
        validCart.setTotalItem(4);
        validCart.setCurrency("VND");
        validCart.setDiscount(0);

        // Tạo delivery info cho Hanoi (hỗ trợ rush order)
        hanoiDeliveryInfo = new DeliveryFormDTO();
        hanoiDeliveryInfo.setCustomerName("Nguyen Van A");
        hanoiDeliveryInfo.setPhoneNumber("0123456789");
        hanoiDeliveryInfo.setEmail("test@example.com");
        hanoiDeliveryInfo.setDeliveryAddress("123 Cau Giay");
        hanoiDeliveryInfo.setDeliveryProvince("Hanoi");
        hanoiDeliveryInfo.setRushOrder(false);

        // Tạo delivery info cho địa chỉ khác (không hỗ trợ rush order)
        nonHanoiDeliveryInfo = new DeliveryFormDTO();
        nonHanoiDeliveryInfo.setCustomerName("Tran Van B");
        nonHanoiDeliveryInfo.setPhoneNumber("0987654321");
        nonHanoiDeliveryInfo.setEmail("test2@example.com");
        nonHanoiDeliveryInfo.setDeliveryAddress("456 District 1");
        nonHanoiDeliveryInfo.setDeliveryProvince("Hochiminh");
        nonHanoiDeliveryInfo.setRushOrder(false);

        // Setup default mock behavior
        lenient().when(messageService.getRushOrderNoEligibleProducts())
                .thenReturn("There are no products eligible for rush order.");
    }

    @Test
    void checkEligibility_Success_WithRushAndNormalItems() {
        // Arrange
        List<CartItemRequestDTO> rushItems = Arrays.asList(rushItem1, rushItem2);
        List<CartItemRequestDTO> normalItems = Arrays.asList(normalItem1);
        CartSplitter.Pair<List<CartItemRequestDTO>, List<CartItemRequestDTO>> splitResult = 
            new CartSplitter.Pair<>(rushItems, normalItems);

        when(cartSplitter.splitRushAndNormal(validCart, productRepository))
            .thenReturn(splitResult);

        // Act
        RushOrderEligibilityResponseDTO result = rushOrderEligibilityService.checkEligibility(validCart, hanoiDeliveryInfo);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEligible());
        assertEquals(2, result.getRushItems().size());
        assertEquals(1, result.getNormalItems().size());
        assertEquals("Rush order is eligible for your cart and delivery address.", result.getMessage());
        
        verify(cartSplitter).splitRushAndNormal(validCart, productRepository);
    }

    @Test
    void checkEligibility_Success_WithOnlyRushItems() {
        // Arrange
        List<CartItemRequestDTO> rushItems = Arrays.asList(rushItem1, rushItem2);
        List<CartItemRequestDTO> normalItems = Collections.emptyList();
        CartSplitter.Pair<List<CartItemRequestDTO>, List<CartItemRequestDTO>> splitResult = 
            new CartSplitter.Pair<>(rushItems, normalItems);

        when(cartSplitter.splitRushAndNormal(validCart, productRepository))
            .thenReturn(splitResult);

        // Act
        RushOrderEligibilityResponseDTO result = rushOrderEligibilityService.checkEligibility(validCart, hanoiDeliveryInfo);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEligible());
        assertEquals(2, result.getRushItems().size());
        assertTrue(result.getNormalItems().isEmpty());
        assertEquals("Rush order is eligible for your cart and delivery address.", result.getMessage());
    }

    @Test
    void checkEligibility_AddressNotEligible_ThrowsException() {
        // Act & Assert
        RushOrderException exception = assertThrows(
            RushOrderException.class,
            () -> rushOrderEligibilityService.checkEligibility(validCart, nonHanoiDeliveryInfo)
        );

        assertEquals("Delivery address does not support rush order delivery. Rush order is only available for addresses within Hanoi inner city.", 
                    exception.getMessage());
        assertEquals("ADDRESS_NOT_ELIGIBLE", exception.getErrorCode());
        assertNull(exception.getExpectedDateTime());

        // Verify cartSplitter không được gọi khi địa chỉ không hợp lệ
        verify(cartSplitter, never()).splitRushAndNormal(any(), any());
    }

    @Test
    void checkEligibility_NoEligibleProducts_ThrowsException() {
        // Arrange
        List<CartItemRequestDTO> rushItems = Collections.emptyList();
        List<CartItemRequestDTO> normalItems = Arrays.asList(normalItem1);
        CartSplitter.Pair<List<CartItemRequestDTO>, List<CartItemRequestDTO>> splitResult = 
            new CartSplitter.Pair<>(rushItems, normalItems);

        when(cartSplitter.splitRushAndNormal(validCart, productRepository))
            .thenReturn(splitResult);

        // Act & Assert
        RushOrderException exception = assertThrows(
            RushOrderException.class,
            () -> rushOrderEligibilityService.checkEligibility(validCart, hanoiDeliveryInfo)
        );

        assertEquals("There are no products eligible for rush order.", exception.getMessage());
        assertEquals("NO_ELIGIBLE_PRODUCTS", exception.getErrorCode());
        assertNull(exception.getExpectedDateTime());

        verify(cartSplitter).splitRushAndNormal(validCart, productRepository);
        verify(messageService).getRushOrderNoEligibleProducts();
    }

    @Test
    void checkEligibility_NullRushItems_ThrowsException() {
        // Arrange
        List<CartItemRequestDTO> normalItems = Arrays.asList(normalItem1);
        CartSplitter.Pair<List<CartItemRequestDTO>, List<CartItemRequestDTO>> splitResult = 
            new CartSplitter.Pair<>(null, normalItems);

        when(cartSplitter.splitRushAndNormal(validCart, productRepository))
            .thenReturn(splitResult);

        // Act & Assert
        RushOrderException exception = assertThrows(
            RushOrderException.class,
            () -> rushOrderEligibilityService.checkEligibility(validCart, hanoiDeliveryInfo)
        );

        assertEquals("There are no products eligible for rush order.", exception.getMessage());
        assertEquals("NO_ELIGIBLE_PRODUCTS", exception.getErrorCode());
    }

    @Test
    void checkEligibility_AddressHaNoiCaseInsensitive_Success() {
        // Arrange
        DeliveryFormDTO haNoiDeliveryInfo = new DeliveryFormDTO();
        haNoiDeliveryInfo.setCustomerName("Test User");
        haNoiDeliveryInfo.setPhoneNumber("0123456789");
        haNoiDeliveryInfo.setEmail("test@example.com");
        haNoiDeliveryInfo.setDeliveryAddress("Test Address");
        haNoiDeliveryInfo.setDeliveryProvince("Ha Noi"); // Test case-insensitive

        List<CartItemRequestDTO> rushItems = Arrays.asList(rushItem1);
        List<CartItemRequestDTO> normalItems = Collections.emptyList();
        CartSplitter.Pair<List<CartItemRequestDTO>, List<CartItemRequestDTO>> splitResult = 
            new CartSplitter.Pair<>(rushItems, normalItems);

        when(cartSplitter.splitRushAndNormal(validCart, productRepository))
            .thenReturn(splitResult);

        // Act
        RushOrderEligibilityResponseDTO result = rushOrderEligibilityService.checkEligibility(validCart, haNoiDeliveryInfo);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEligible());
        assertEquals(1, result.getRushItems().size());
    }

    @Test
    void checkEligibility_AddressHanoiUppercase_Success() {
        // Arrange
        DeliveryFormDTO upperCaseHanoiInfo = new DeliveryFormDTO();
        upperCaseHanoiInfo.setCustomerName("Test User");
        upperCaseHanoiInfo.setPhoneNumber("0123456789");
        upperCaseHanoiInfo.setEmail("test@example.com");
        upperCaseHanoiInfo.setDeliveryAddress("Test Address");
        upperCaseHanoiInfo.setDeliveryProvince("HANOI"); // Test uppercase

        List<CartItemRequestDTO> rushItems = Arrays.asList(rushItem1);
        List<CartItemRequestDTO> normalItems = Collections.emptyList();
        CartSplitter.Pair<List<CartItemRequestDTO>, List<CartItemRequestDTO>> splitResult = 
            new CartSplitter.Pair<>(rushItems, normalItems);

        when(cartSplitter.splitRushAndNormal(validCart, productRepository))
            .thenReturn(splitResult);

        // Act
        RushOrderEligibilityResponseDTO result = rushOrderEligibilityService.checkEligibility(validCart, upperCaseHanoiInfo);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEligible());
    }

    @Test
    void checkEligibility_EmptyCart_NoEligibleProducts() {
        // Arrange
        CartRequestDTO emptyCart = new CartRequestDTO();
        emptyCart.setProductList(Collections.emptyList());
        emptyCart.setTotalPrice(0);
        emptyCart.setTotalItem(0);
        emptyCart.setCurrency("VND");

        List<CartItemRequestDTO> rushItems = Collections.emptyList();
        List<CartItemRequestDTO> normalItems = Collections.emptyList();
        CartSplitter.Pair<List<CartItemRequestDTO>, List<CartItemRequestDTO>> splitResult = 
            new CartSplitter.Pair<>(rushItems, normalItems);

        when(cartSplitter.splitRushAndNormal(emptyCart, productRepository))
            .thenReturn(splitResult);

        // Act & Assert
        RushOrderException exception = assertThrows(
            RushOrderException.class,
            () -> rushOrderEligibilityService.checkEligibility(emptyCart, hanoiDeliveryInfo)
        );

        assertEquals("NO_ELIGIBLE_PRODUCTS", exception.getErrorCode());
        verify(cartSplitter).splitRushAndNormal(emptyCart, productRepository);
    }

    @Test
    void checkEligibility_VerifyResponseFields() {
        // Arrange
        List<CartItemRequestDTO> rushItems = Arrays.asList(rushItem1, rushItem2);
        List<CartItemRequestDTO> normalItems = Arrays.asList(normalItem1);
        CartSplitter.Pair<List<CartItemRequestDTO>, List<CartItemRequestDTO>> splitResult = 
            new CartSplitter.Pair<>(rushItems, normalItems);

        when(cartSplitter.splitRushAndNormal(validCart, productRepository))
            .thenReturn(splitResult);

        // Act
        RushOrderEligibilityResponseDTO result = rushOrderEligibilityService.checkEligibility(validCart, hanoiDeliveryInfo);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEligible());
        assertNotNull(result.getRushItems());
        assertNotNull(result.getNormalItems());
        assertNotNull(result.getMessage());
        assertNull(result.getErrorCode()); // Error code chỉ set khi có lỗi

        // Verify content của rush items
        assertEquals(rushItem1.getProductID(), result.getRushItems().get(0).getProductID());
        assertEquals(rushItem2.getProductID(), result.getRushItems().get(1).getProductID());
        
        // Verify content của normal items
        assertEquals(normalItem1.getProductID(), result.getNormalItems().get(0).getProductID());
    }
} 