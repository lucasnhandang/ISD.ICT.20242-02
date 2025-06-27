package com.hustict.aims.controller;

import com.hustict.aims.service.order.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/product-manager/orders")
@CrossOrigin(origins = "*")
public class OrderPMController {

    private final OrderService orderService;

    public OrderPMController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PutMapping("/{id}/approve")
    public ResponseEntity<Void> approveOrder(@PathVariable Long id) {
        orderService.approveOrder(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/reject")
    public ResponseEntity<Void> rejectOrder(@PathVariable Long id,String reason) {
        orderService.rejectOrder(id,reason);
        return ResponseEntity.ok().build();
    }
} 