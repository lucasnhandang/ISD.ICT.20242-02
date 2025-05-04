package com.hustict.aims;

import com.hustict.aims.model.DeliveryInfo;
import com.hustict.aims.model.Product;
import com.hustict.aims.service.validator.RushOrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RushOrderServiceTest {

    private RushOrderService rushOrderService;

    @Mock
    private DeliveryInfo deliveryInfo;

    @Mock
    private Product eligibleProduct;

    @Mock
    private Product ineligibleProduct;

    @BeforeEach
    public void setUp() {
        rushOrderService = new RushOrderService();
        // Di chuyển các stub vào từng test case cụ thể để tránh UnnecessaryStubbingException
    }

    @Test
    public void testTC1_AllProductsEligibleInHanoi() {
        // Thiết lập địa chỉ giao hàng ở Hà Nội
        when(deliveryInfo.getProvince()).thenReturn("HN");
        when(eligibleProduct.getRushOrderSupported()).thenReturn(true);

        // Danh sách sản phẩm - tất cả đều hỗ trợ giao hàng nhanh
        List<Product> products = Arrays.asList(eligibleProduct, eligibleProduct);

        boolean result = rushOrderService.isRushOrderEligible(deliveryInfo, products);
        assertTrue(result);
        verify(eligibleProduct, atLeastOnce()).getRushOrderSupported();
    }

    @Test
    public void testTC2_SomeProductsEligibleInHanoi() {
        // Thiết lập địa chỉ giao hàng ở Hà Nội
        when(deliveryInfo.getProvince()).thenReturn("HN");

        // Chỉ thiết lập stub cho eligibleProduct, vì chỉ có nó được gọi trong thực tế
        when(eligibleProduct.getRushOrderSupported()).thenReturn(true);

        // Không thiết lập stub cho ineligibleProduct vì nó có thể không được gọi
        // tùy thuộc vào thứ tự duyệt danh sách

        // Danh sách sản phẩm - một số hỗ trợ giao hàng nhanh
        List<Product> products = Arrays.asList(eligibleProduct, ineligibleProduct);

        boolean result = rushOrderService.isRushOrderEligible(deliveryInfo, products);
        assertTrue(result);
        verify(eligibleProduct).getRushOrderSupported();
    }

    @Test
    public void testTC3_NoProductsEligibleInHanoi() {
        // Thiết lập địa chỉ giao hàng ở Hà Nội
        when(deliveryInfo.getProvince()).thenReturn("HN");
        when(ineligibleProduct.getRushOrderSupported()).thenReturn(false);

        // Danh sách sản phẩm - không có sản phẩm nào hỗ trợ giao hàng nhanh
        List<Product> products = Arrays.asList(ineligibleProduct, ineligibleProduct);

        boolean result = rushOrderService.isRushOrderEligible(deliveryInfo, products);
        assertFalse(result);
        verify(ineligibleProduct, times(2)).getRushOrderSupported();
    }

    @Test
    public void testTC4_AllProductsEligibleOutsideHanoi() {
        // Thiết lập địa chỉ giao hàng không ở Hà Nội
        when(deliveryInfo.getProvince()).thenReturn("HP");
        // Không cần stub eligibleProduct vì nó không được gọi

        // Danh sách sản phẩm - tất cả đều hỗ trợ giao hàng nhanh
        List<Product> products = Arrays.asList(eligibleProduct, eligibleProduct);

        boolean result = rushOrderService.isRushOrderEligible(deliveryInfo, products);
        assertFalse(result);
        verify(eligibleProduct, never()).getRushOrderSupported();
    }

    @Test
    public void testTC5_SomeProductsEligibleOutsideHanoi() {
        // Thiết lập địa chỉ giao hàng không ở Hà Nội
        when(deliveryInfo.getProvince()).thenReturn("HP");
        // Không cần stub các sản phẩm vì chúng không được gọi

        // Danh sách sản phẩm - một số hỗ trợ giao hàng nhanh
        List<Product> products = Arrays.asList(eligibleProduct, ineligibleProduct);

        boolean result = rushOrderService.isRushOrderEligible(deliveryInfo, products);
        assertFalse(result);
        verify(eligibleProduct, never()).getRushOrderSupported();
        verify(ineligibleProduct, never()).getRushOrderSupported();
    }

    @Test
    public void testTC6_NoProductsEligibleOutsideHanoi() {
        // Thiết lập địa chỉ giao hàng không ở Hà Nội
        when(deliveryInfo.getProvince()).thenReturn("HP");
        // Không cần stub các sản phẩm vì chúng không được gọi

        // Danh sách sản phẩm - không có sản phẩm nào hỗ trợ giao hàng nhanh
        List<Product> products = Arrays.asList(ineligibleProduct, ineligibleProduct);

        boolean result = rushOrderService.isRushOrderEligible(deliveryInfo, products);
        assertFalse(result);
        verify(ineligibleProduct, never()).getRushOrderSupported();
    }

    @Test
    public void testTC7_NullDeliveryInfo() {
        // Danh sách sản phẩm - không quan trọng vì ngoại lệ sẽ xảy ra trước khi kiểm tra sản phẩm
        List<Product> products = Arrays.asList(eligibleProduct);

        // Kiểm tra ngoại lệ khi deliveryInfo là null
        assertThrows(IllegalArgumentException.class, () ->
                rushOrderService.isRushOrderEligible(null, products));
    }

    @Test
    public void testTC8_NullProductList() {
        // Không cần thiết lập stub cho deliveryInfo.getProvince() vì phương thức
        // sẽ thả ra IllegalArgumentException trước khi gọi đến getProvince()

        // Kiểm tra ngoại lệ khi danh sách sản phẩm là null
        assertThrows(IllegalArgumentException.class, () ->
                rushOrderService.isRushOrderEligible(deliveryInfo, null));
    }

    @Test
    public void testEmptyProductList() {
        // Thiết lập địa chỉ giao hàng ở Hà Nội
        when(deliveryInfo.getProvince()).thenReturn("HN");

        // Danh sách sản phẩm rỗng
        List<Product> products = Collections.emptyList();

        boolean result = rushOrderService.isRushOrderEligible(deliveryInfo, products);
        assertFalse(result);
    }
}