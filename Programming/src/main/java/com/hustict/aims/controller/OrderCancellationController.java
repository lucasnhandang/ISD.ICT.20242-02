package com.hustict.aims.controller;

import com.hustict.aims.service.email.EmailSenderFactory;
import com.hustict.aims.service.email.OrderInfoService;
import com.hustict.aims.service.order.OrderQueryService;
import com.hustict.aims.service.order.OrderCancellationService;
import com.hustict.aims.service.refund.PaymentSystem;
import com.hustict.aims.service.refund.RefundSelector;
import com.hustict.aims.dto.order.OrderDTO;
import com.hustict.aims.dto.order.OrderInformationDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/v1/orders")
@CrossOrigin(origins = "*")
public class OrderCancellationController {

    private final OrderQueryService orderQueryService;
    private final OrderCancellationService orderCancellationService;
    private final PaymentSystem paymentSystem;

    @Autowired
    private  EmailSenderFactory emailSenderFactory;

    @Autowired
    private OrderInfoService orderInfoService;

    public OrderCancellationController(OrderQueryService orderQueryService,
                                     OrderCancellationService orderCancellationService,
                                     PaymentSystem paymentSystem) {
        this.orderQueryService = orderQueryService;
        this.orderCancellationService = orderCancellationService;
        this.paymentSystem = paymentSystem;
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderInformationDTO> getOrderDetails(@PathVariable Long id) {
        try {
            OrderInformationDTO orderDetails = orderQueryService.getOrderDetails(id);
            return ResponseEntity.ok(orderDetails);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<Void> cancelOrder(@PathVariable Long id, HttpSession session) {
        try {
            // Hủy đơn hàng
            orderCancellationService.cancelOrder(id);
            
            // Xử lý hoàn tiền nếu cần
            String system = (String) session.getAttribute("system");
            if (system != null) {
                paymentSystem.processRefund(id, system);
                
            }
            OrderDTO order = orderInfoService.getOrderDTOByOrderId(id);
            emailSenderFactory.process("cancelOrder", order);

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
} 