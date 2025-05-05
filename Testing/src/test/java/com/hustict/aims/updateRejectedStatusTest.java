package com.hustict.aims;
import com.hustict.aims.exception.*;

import com.hustict.aims.service.*;
import com.hustict.aims.model.*;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Collections;

public class updateRejectedStatusTest {

    private final OrderService orderService = new OrderService();

    private Order createPendingOrder() {
        return new Order(
            1, // id
            1, // totalItem
            100, // totalPrice
            "VND", // currency
            null, // invoiceID
            1, // deliveryInfoID
            LocalDate.now(), // orderDate
            "pending", // orderStatus
            false // isRushOrder
        );
    }

    @Test
    public void TestCase1() {
        Order order = createPendingOrder();
        String reason = "Customer request";

        orderService.updateRejectedStatusAndReason(order, reason);

        assertEquals("rejectedNotRefunded", order.getOrderStatus());
        assertEquals(reason, order.getRejectionReason());
    }

    @Test
    public void TestCase2() {
        Order order = createPendingOrder();

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            orderService.updateRejectedStatusAndReason(order, null);
        });

        assertEquals("Lý do từ chối không được để trống.", exception.getMessage());
    }

    @Test
    public void TestCase3() {
        Order order = createPendingOrder();

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            orderService.updateRejectedStatusAndReason(order, "  ");
        });

        assertEquals("Lý do từ chối không được để trống.", exception.getMessage());
    }

    @Test
    public void TestCase4() {
        Order order = createPendingOrder();
        order.setOrderStatus("rejectedNotRefunded");

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            orderService.updateRejectedStatusAndReason(order, "Customer request");
        });

        assertEquals("Trạng thái đơn hàng không đạt yêu cầu để từ chối.", exception.getMessage());
    }

    @Test
    public void TestCase5() {
        Order order = createPendingOrder();
        order.setOrderStatus("refunded");

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            orderService.updateRejectedStatusAndReason(order, "Customer request");
        });

        assertEquals("Trạng thái đơn hàng không đạt yêu cầu để từ chối.", exception.getMessage());
    }

    @Test
    public void TestCase6() {
        Order order = createPendingOrder();
        order.setOrderStatus("approved");

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            orderService.updateRejectedStatusAndReason(order, "Customer request");
        });

        assertEquals("Trạng thái đơn hàng không đạt yêu cầu để từ chối.", exception.getMessage());
    }

    @Test
    public void TestCase7() {
        Order order = createPendingOrder();
        order.setOrderStatus("delivered");

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            orderService.updateRejectedStatusAndReason(order, "Customer request");
        });

        assertEquals("Trạng thái đơn hàng không đạt yêu cầu để từ chối.", exception.getMessage());
    }

    @Test
    public void TestCase8() {
        Order order = createPendingOrder();
        order.setOrderStatus("canceled");

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            orderService.updateRejectedStatusAndReason(order, "Customer request");
        });

        assertEquals("Trạng thái đơn hàng không đạt yêu cầu để từ chối.", exception.getMessage());
    }
}
