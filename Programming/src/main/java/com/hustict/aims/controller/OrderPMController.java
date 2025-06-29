package com.hustict.aims.controller;

import com.hustict.aims.service.email.EmailSenderFactory;
import com.hustict.aims.service.order.OrderService;
import com.hustict.aims.service.refund.RefundService;
import com.hustict.aims.service.refund.RefundStrategySelector;
import com.hustict.aims.dto.order.OrderInformationDTO;

import jakarta.servlet.http.HttpSession;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/product-manager/orders")
@CrossOrigin(origins = "*")
public class OrderPMController {

    private final OrderService orderService;
    private final EmailSenderFactory emailSenderFactory;
   // private final RefundService refundService;
    private final RefundStrategySelector refundStrategySelector;

    public OrderPMController(OrderService orderService, EmailSenderFactory emailSenderFactory, RefundStrategySelector refundStrategySelector) {
        this.orderService = orderService;
        this.emailSenderFactory = emailSenderFactory;
        this.refundStrategySelector = refundStrategySelector;
    }

    @GetMapping("/pending")
    public ResponseEntity<List<OrderInformationDTO>> getPendingOrders() {
        return ResponseEntity.ok(orderService.getPendingOrders());
    }

    // @PutMapping("/{id}/approve")
    // public ResponseEntity<Void> approveOrder(@PathVariable Long id,HttpSession session) {
    //     orderService.approveOrder(id);
    //     orderService.prepareOrderSessionForEmail(id, session);
        
    //     emailSenderFactory.process("approveOrder", session);
    //     return ResponseEntity.ok().build();
    // }

    // @PutMapping("/{id}/reject")
    // public ResponseEntity<Void> rejectOrder(@PathVariable Long id,HttpSession session) {
    //     orderService.rejectOrder(id);
    //     orderService.prepareOrderSessionForEmail(id, session);
    //     emailSenderFactory.process("rejectOrder", session);
    //     return ResponseEntity.ok().build();
    // }

    @PutMapping("/{id}/refund")
    public ResponseEntity<Void> refund(@PathVariable Long id,HttpSession session) {
        //String system = (String) session.getAttribute("system");
        System.out.println("Refund order "+ id);

        refundStrategySelector.refund(id);
        
        // emailSenderFactory.process("rejectOrder", session);
        return ResponseEntity.ok().build();
    }
} 