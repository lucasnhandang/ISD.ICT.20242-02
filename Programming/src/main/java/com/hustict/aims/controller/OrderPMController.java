package com.hustict.aims.controller;

import com.hustict.aims.service.email.EmailSenderFactory;
import com.hustict.aims.service.order.OrderService;

import jakarta.servlet.http.HttpSession;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/product-manager/orders")
@CrossOrigin(origins = "*")
public class OrderPMController {

    private final OrderService orderService;
    private final EmailSenderFactory emailSenderFactory;

    public OrderPMController(OrderService orderService,EmailSenderFactory emailSenderFactory) {
        this.orderService = orderService;
        this.emailSenderFactory=emailSenderFactory;
    }

    @PutMapping("/{id}/approve")
    public ResponseEntity<Void> approveOrder(@PathVariable Long id,HttpSession session) {
        orderService.approveOrder(id);
        emailSenderFactory.process("approveOrder", session);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/reject")
    public ResponseEntity<Void> rejectOrder(@PathVariable Long id,HttpSession session) {
        orderService.rejectOrder(id);
        emailSenderFactory.process("rejectOrder", session);
        return ResponseEntity.ok().build();
    }
} 