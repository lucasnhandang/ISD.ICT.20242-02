package com.hustict.aims.controller;

import com.hustict.aims.service.email.EmailSenderFactory;
import com.hustict.aims.service.email.OrderInfoService;
import com.hustict.aims.service.order.OrderService;
import com.hustict.aims.service.refund.RefundStrategySelector;
import com.hustict.aims.dto.order.OrderDTO;
import com.hustict.aims.dto.order.OrderInformationDTO;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/product-manager/orders")
@CrossOrigin(origins = "*")
public class OrderPMController {

    private final OrderService orderService;
    private final EmailSenderFactory emailSenderFactory;
    private final RefundStrategySelector refundStrategySelector;

    @Autowired
    private OrderInfoService orderInfoService;

    public OrderPMController(OrderService orderService, EmailSenderFactory emailSenderFactory, RefundStrategySelector refundStrategySelector) {
        this.orderService = orderService;
        this.emailSenderFactory = emailSenderFactory;
        this.refundStrategySelector = refundStrategySelector;
    }

    @GetMapping("/pending")
    public ResponseEntity<List<OrderInformationDTO>> getPendingOrders() {
        return ResponseEntity.ok(orderService.getPendingOrders());
    }

    @PutMapping("/{id}/approve")
    public ResponseEntity<Void> approveOrder(@PathVariable Long id,HttpSession session) {
        orderService.approveOrder(id);
        orderService.prepareOrderSessionForEmail(id, session);

        OrderDTO order = orderInfoService.getOrderDTOByOrderId(id);
        emailSenderFactory.process("approveOrder", order);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/reject")
    public ResponseEntity<Void> rejectOrder(@PathVariable Long id,HttpSession session) {
        orderService.rejectOrder(id);
        orderService.prepareOrderSessionForEmail(id, session);
        OrderDTO order = orderInfoService.getOrderDTOByOrderId(id);
        emailSenderFactory.process("rejectOrder", order);
        System.out.println("Refund order "+ id);
        refundStrategySelector.refund(id);
        
        return ResponseEntity.ok().build();
    }

} 