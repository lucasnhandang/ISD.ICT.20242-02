// package com.hustict.aims.service;

// import com.hustict.aims.dto.cart.CartItemRequestDTO;
// import com.hustict.aims.dto.cart.CartRequestDTO;
// import com.hustict.aims.dto.deliveryForm.DeliveryFormDTO;
// import com.hustict.aims.dto.order.RushOrderEligibilityResponseDTO;
// import com.hustict.aims.exception.RushOrderException;
// import com.hustict.aims.model.product.Book;
// import com.hustict.aims.model.product.CD;
// import com.hustict.aims.model.product.Product;
// import com.hustict.aims.repository.product.ProductRepository;
// import com.hustict.aims.service.placeRushOrder.CartSplitter;
// import com.hustict.aims.service.placeRushOrder.RushOrderEligibilityService;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.extension.ExtendWith;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.mockito.junit.jupiter.MockitoExtension;

// import java.time.LocalDate;
// import java.util.ArrayList;
// import java.util.Arrays;
// import java.util.Collections;
// import java.util.List;

// import static org.junit.jupiter.api.Assertions.*;
// import static org.mockito.ArgumentMatchers.any;
// import static org.mockito.Mockito.*;

// @ExtendWith(MockitoExtension.class)
// public class RushOrderEligibilityServiceTest {

//     @Mock
//     private CartSplitter cartSplitter;

//     @Mock
//     private ProductRepository productRepository;

//     @Mock
//     private MessageService messageService;

//     @InjectMocks
//     private RushOrderEligibilityService rushOrderEligibilityService;

//     private CartRequestDTO cartRequest;
//     private DeliveryFormDTO deliveryForm;
//     private CartItemRequestDTO rushItem;
//     private CartItemRequestDTO normalItem;
//     private Book rushProduct;
//     private CD normalProduct;

//     @BeforeEach
//     void setUp() {
//         // Setup cart items
//         rushItem = new CartItemRequestDTO();
//         rushItem.setProductID(1L);
//         rushItem.setProductName("Test Book");
//         rushItem.setQuantity(2);
//         rushItem.setPrice(100000);
//         rushItem.setWeight(0.3);

//         normalItem = new CartItemRequestDTO();
//         normalItem.setProductID(2L);
//         normalItem.setProductName("Test CD");
//         normalItem.setQuantity(1);
//         normalItem.setPrice(50000);
//         normalItem.setWeight(0.1);

//         // Setup cart request
//         cartRequest = new CartRequestDTO();
//         cartRequest.setProductList(Arrays.asList(rushItem, normalItem));
//         cartRequest.setTotalPrice(250000);
//         cartRequest.setTotalItem(3);
//         cartRequest.setCurrency("VND");
//         cartRequest.setDiscount(0);

//         // Setup delivery form for Hanoi
//         deliveryForm = new DeliveryFormDTO();
//         deliveryForm.setCustomerName("Test Customer");
//         deliveryForm.setPhoneNumber("0123456789");
//         deliveryForm.setDeliveryProvince("Hanoi");
//         deliveryForm.setDeliveryAddress("123 Test Street, Hanoi");
//         deliveryForm.setDeliveryInstructions("Please deliver in the morning");
//         deliveryForm.setRushOrder(true);
//         deliveryForm.setEmail("test@example.com");

//         // Setup products
//         rushProduct = new Book();
//         rushProduct.setTitle("Test Book");
//         rushProduct.setRushOrderSupported(true);
//         rushProduct.setEntryDate(LocalDate.now());

//         normalProduct = new CD();
//         normalProduct.setTitle("Test CD");
//         normalProduct.setRushOrderSupported(false);
//         normalProduct.setEntryDate(LocalDate.now());
//     }

//     @Test
//     void checkEligibility_Success_WithRushAndNormalItems() {
//         // Arrange
//         List<CartItemRequestDTO> rushItems = Arrays.asList(rushItem);
//         List<CartItemRequestDTO> normalItems = Arrays.asList(normalItem);
//         CartSplitter.Pair<List<CartItemRequestDTO>, List<CartItemRequestDTO>> splitResult = 
//             new CartSplitter.Pair<>(rushItems, normalItems);

//         when(cartSplitter.splitRushAndNormal(any(CartRequestDTO.class), any(ProductRepository.class)))
//             .thenReturn(splitResult);

//         // Act
//         RushOrderEligibilityResponseDTO result = rushOrderEligibilityService.checkEligibility(cartRequest, deliveryForm);

//         // Assert
//         assertNotNull(result);
//         assertTrue(result.isEligible());
//         assertEquals(rushItems, result.getRushItems());
//         assertEquals(normalItems, result.getNormalItems());
//         assertEquals("Rush order is eligible for your cart and delivery address.", result.getMessage());
//         assertNull(result.getErrorCode());

//         verify(cartSplitter).splitRushAndNormal(cartRequest, productRepository);
//     }

//     @Test
//     void checkEligibility_Success_OnlyRushItems() {
//         // Arrange
//         List<CartItemRequestDTO> rushItems = Arrays.asList(rushItem);
//         List<CartItemRequestDTO> normalItems = new ArrayList<>();
//         CartSplitter.Pair<List<CartItemRequestDTO>, List<CartItemRequestDTO>> splitResult = 
//             new CartSplitter.Pair<>(rushItems, normalItems);

//         when(cartSplitter.splitRushAndNormal(any(CartRequestDTO.class), any(ProductRepository.class)))
//             .thenReturn(splitResult);

//         // Act
//         RushOrderEligibilityResponseDTO result = rushOrderEligibilityService.checkEligibility(cartRequest, deliveryForm);

//         // Assert
//         assertNotNull(result);
//         assertTrue(result.isEligible());
//         assertEquals(rushItems, result.getRushItems());
//         assertEquals(normalItems, result.getNormalItems());
//         assertEquals("Rush order is eligible for your cart and delivery address.", result.getMessage());

//         verify(cartSplitter).splitRushAndNormal(cartRequest, productRepository);
//     }

//     @Test
//     void checkEligibility_AddressNotEligible_ThrowsException() {
//         // Arrange
//         deliveryForm.setDeliveryProvince("Ho Chi Minh City");

//         // Act & Assert
//         RushOrderException exception = assertThrows(
//             RushOrderException.class,
//             () -> rushOrderEligibilityService.checkEligibility(cartRequest, deliveryForm)
//         );

//         assertEquals("Delivery address does not support rush order delivery. Rush order is only available for addresses within Hanoi inner city.", 
//                      exception.getMessage());
//         assertEquals("ADDRESS_NOT_ELIGIBLE", exception.getErrorCode());
//         assertNull(exception.getCause());

//         verify(cartSplitter, never()).splitRushAndNormal(any(), any());
//     }

//     @Test
//     void checkEligibility_AddressNotEligible_WithHaNoiVariant_ThrowsException() {
//         // Arrange
//         deliveryForm.setDeliveryProvince("Ha Noi");

//         // Act & Assert
//         RushOrderException exception = assertThrows(
//             RushOrderException.class,
//             () -> rushOrderEligibilityService.checkEligibility(cartRequest, deliveryForm)
//         );

//         assertEquals("Delivery address does not support rush order delivery. Rush order is only available for addresses within Hanoi inner city.", 
//                      exception.getMessage());
//         assertEquals("ADDRESS_NOT_ELIGIBLE", exception.getErrorCode());

//         verify(cartSplitter, never()).splitRushAndNormal(any(), any());
//     }

//     @Test
//     void checkEligibility_NoEligibleProducts_ThrowsException() {
//         // Arrange
//         List<CartItemRequestDTO> rushItems = new ArrayList<>();
//         List<CartItemRequestDTO> normalItems = Arrays.asList(normalItem);
//         CartSplitter.Pair<List<CartItemRequestDTO>, List<CartItemRequestDTO>> splitResult = 
//             new CartSplitter.Pair<>(rushItems, normalItems);

//         when(cartSplitter.splitRushAndNormal(any(CartRequestDTO.class), any(ProductRepository.class)))
//             .thenReturn(splitResult);
//         when(messageService.getRushOrderNoEligibleProducts())
//             .thenReturn("No products in your cart support rush order delivery.");

//         // Act & Assert
//         RushOrderException exception = assertThrows(
//             RushOrderException.class,
//             () -> rushOrderEligibilityService.checkEligibility(cartRequest, deliveryForm)
//         );

//         assertEquals("No products in your cart support rush order delivery.", exception.getMessage());
//         assertEquals("NO_ELIGIBLE_PRODUCTS", exception.getErrorCode());
//         assertNull(exception.getCause());

//         verify(cartSplitter).splitRushAndNormal(cartRequest, productRepository);
//         verify(messageService).getRushOrderNoEligibleProducts();
//     }

//     @Test
//     void checkEligibility_NullRushItems_ThrowsException() {
//         // Arrange
//         List<CartItemRequestDTO> normalItems = Arrays.asList(normalItem);
//         CartSplitter.Pair<List<CartItemRequestDTO>, List<CartItemRequestDTO>> splitResult = 
//             new CartSplitter.Pair<>(null, normalItems);

//         when(cartSplitter.splitRushAndNormal(any(CartRequestDTO.class), any(ProductRepository.class)))
//             .thenReturn(splitResult);
//         when(messageService.getRushOrderNoEligibleProducts())
//             .thenReturn("No products in your cart support rush order delivery.");

//         // Act & Assert
//         RushOrderException exception = assertThrows(
//             RushOrderException.class,
//             () -> rushOrderEligibilityService.checkEligibility(cartRequest, deliveryForm)
//         );

//         assertEquals("No products in your cart support rush order delivery.", exception.getMessage());
//         assertEquals("NO_ELIGIBLE_PRODUCTS", exception.getErrorCode());

//         verify(cartSplitter).splitRushAndNormal(cartRequest, productRepository);
//         verify(messageService).getRushOrderNoEligibleProducts();
//     }

//     @Test
//     void checkEligibility_EmptyCart_ThrowsException() {
//         // Arrange
//         cartRequest.setProductList(new ArrayList<>());
//         List<CartItemRequestDTO> rushItems = new ArrayList<>();
//         List<CartItemRequestDTO> normalItems = new ArrayList<>();
//         CartSplitter.Pair<List<CartItemRequestDTO>, List<CartItemRequestDTO>> splitResult = 
//             new CartSplitter.Pair<>(rushItems, normalItems);

//         when(cartSplitter.splitRushAndNormal(any(CartRequestDTO.class), any(ProductRepository.class)))
//             .thenReturn(splitResult);
//         when(messageService.getRushOrderNoEligibleProducts())
//             .thenReturn("No products in your cart support rush order delivery.");

//         // Act & Assert
//         RushOrderException exception = assertThrows(
//             RushOrderException.class,
//             () -> rushOrderEligibilityService.checkEligibility(cartRequest, deliveryForm)
//         );

//         assertEquals("No products in your cart support rush order delivery.", exception.getMessage());
//         assertEquals("NO_ELIGIBLE_PRODUCTS", exception.getErrorCode());

//         verify(cartSplitter).splitRushAndNormal(cartRequest, productRepository);
//         verify(messageService).getRushOrderNoEligibleProducts();
//     }

//     @Test
//     void checkEligibility_NullDeliveryProvince_ThrowsException() {
//         // Arrange
//         deliveryForm.setDeliveryProvince(null);

//         // Act & Assert
//         RushOrderException exception = assertThrows(
//             RushOrderException.class,
//             () -> rushOrderEligibilityService.checkEligibility(cartRequest, deliveryForm)
//         );

//         assertEquals("Delivery address does not support rush order delivery. Rush order is only available for addresses within Hanoi inner city.", 
//                      exception.getMessage());
//         assertEquals("ADDRESS_NOT_ELIGIBLE", exception.getErrorCode());

//         verify(cartSplitter, never()).splitRushAndNormal(any(), any());
//     }

//     @Test
//     void checkEligibility_EmptyDeliveryProvince_ThrowsException() {
//         // Arrange
//         deliveryForm.setDeliveryProvince("");

//         // Act & Assert
//         RushOrderException exception = assertThrows(
//             RushOrderException.class,
//             () -> rushOrderEligibilityService.checkEligibility(cartRequest, deliveryForm)
//         );

//         assertEquals("Delivery address does not support rush order delivery. Rush order is only available for addresses within Hanoi inner city.", 
//                      exception.getMessage());
//         assertEquals("ADDRESS_NOT_ELIGIBLE", exception.getErrorCode());

//         verify(cartSplitter, never()).splitRushAndNormal(any(), any());
//     }

//     @Test
//     void checkEligibility_CaseInsensitiveHanoi_ShouldPass() {
//         // Arrange
//         deliveryForm.setDeliveryProvince("hanoi");
//         List<CartItemRequestDTO> rushItems = Arrays.asList(rushItem);
//         List<CartItemRequestDTO> normalItems = Arrays.asList(normalItem);
//         CartSplitter.Pair<List<CartItemRequestDTO>, List<CartItemRequestDTO>> splitResult = 
//             new CartSplitter.Pair<>(rushItems, normalItems);

//         when(cartSplitter.splitRushAndNormal(any(CartRequestDTO.class), any(ProductRepository.class)))
//             .thenReturn(splitResult);

//         // Act
//         RushOrderEligibilityResponseDTO result = rushOrderEligibilityService.checkEligibility(cartRequest, deliveryForm);

//         // Assert
//         assertNotNull(result);
//         assertTrue(result.isEligible());
//         assertEquals(rushItems, result.getRushItems());
//         assertEquals(normalItems, result.getNormalItems());

//         verify(cartSplitter).splitRushAndNormal(cartRequest, productRepository);
//     }

//     @Test
//     void checkEligibility_CaseInsensitiveHaNoi_ShouldPass() {
//         // Arrange
//         deliveryForm.setDeliveryProvince("ha noi");
//         List<CartItemRequestDTO> rushItems = Arrays.asList(rushItem);
//         List<CartItemRequestDTO> normalItems = Arrays.asList(normalItem);
//         CartSplitter.Pair<List<CartItemRequestDTO>, List<CartItemRequestDTO>> splitResult = 
//             new CartSplitter.Pair<>(rushItems, normalItems);

//         when(cartSplitter.splitRushAndNormal(any(CartRequestDTO.class), any(ProductRepository.class)))
//             .thenReturn(splitResult);

//         // Act
//         RushOrderEligibilityResponseDTO result = rushOrderEligibilityService.checkEligibility(cartRequest, deliveryForm);

//         // Assert
//         assertNotNull(result);
//         assertTrue(result.isEligible());
//         assertEquals(rushItems, result.getRushItems());
//         assertEquals(normalItems, result.getNormalItems());

//         verify(cartSplitter).splitRushAndNormal(cartRequest, productRepository);
//     }
// } 