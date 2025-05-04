package com.hustict.aims;

import com.hustict.aims.controller.OrderController;
import com.hustict.aims.exception.*;
import com.hustict.aims.model.*;
import com.hustict.aims.repository.OrderRepository;
import com.hustict.aims.service.OrderService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
public class PayOrderTest {

    private OrderService orderService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private DeliveryInfo deliveryInfo;

    @Mock
    private Product product;

    private OrderController orderController;

    @BeforeEach
    public void setUp() {
        orderService = new OrderService(orderRepository);
        orderController = new OrderController(orderService);
    }

    @Test
    public void testTC1_SuccessfulPayment() throws Exception {
        Order order = new Order(Arrays.asList(product), deliveryInfo);
        when(product.checkAvailabilityofProduct(anyInt())).thenReturn(true);
        when(orderRepository.saveOrder(order)).thenReturn(true);

        boolean result = orderController.payOrder(order);
        assertTrue(result);
        verify(product).checkAvailabilityofProduct(anyInt());
        verify(orderRepository).saveOrder(order);
    }

    @Test
    public void testTC2_InsufficientStock() throws Exception {
        Order order = new Order(Arrays.asList(product), deliveryInfo);
        doThrow(new InsufficientStockException("Not enough stock")).when(product).checkAvailabilityofProduct(anyInt());

        assertThrows(InsufficientStockException.class, () -> orderController.payOrder(order));
        verify(product).checkAvailabilityofProduct(anyInt());
        verify(orderRepository, never()).saveOrder(any());
    }

    @Test
    public void testTC3_OutOfStock() throws Exception {
        Order order = new Order(Arrays.asList(product), deliveryInfo);
        doThrow(new OutOfStockException("Out of stock")).when(product).checkAvailabilityofProduct(anyInt());

        assertThrows(OutOfStockException.class, () -> orderController.payOrder(order));
        verify(product).checkAvailabilityofProduct(anyInt());
        verify(orderRepository, never()).saveOrder(any());
    }

    @Test
    public void testTC4_NullDeliveryInfo() {
        Order order = new Order(Arrays.asList(product), null);

        assertThrows(IllegalArgumentException.class, () -> orderController.payOrder(order));
    }

    @Test
    public void testTC5_NullProductInOrder() {
        Order order = new Order(null, deliveryInfo);

        assertThrows(IllegalArgumentException.class, () -> orderController.payOrder(order));
    }

    @Test
    public void testTC6_EmptyProductList() {
        Order order = new Order(Collections.emptyList(), deliveryInfo);

        assertThrows(IllegalArgumentException.class, () -> orderController.payOrder(order));
    }

    @Test
    public void testTC7_DatabaseFailure() throws Exception {
        Order order = new Order(Arrays.asList(product), deliveryInfo);
        when(product.checkAvailabilityofProduct(anyInt())).thenReturn(true);
        doThrow(new DatabaseFailConnectionException("DB Error")).when(orderRepository).saveOrder(order);

        assertThrows(DatabaseFailConnectionException.class, () -> orderController.payOrder(order));
    }
}
