package com.hustict.aims.controller;

import com.hustict.aims.service.email.EmailSenderFactory;
import com.hustict.aims.service.email.OrderInfoService;
import com.hustict.aims.service.order.OrderService;
import com.hustict.aims.service.order.OrderQueryService;
import com.hustict.aims.service.order.OrderEmailService;
import com.hustict.aims.service.order.OrderCancellationService;
import com.hustict.aims.service.refund.RefundSelector;
import com.hustict.aims.dto.order.OrderDTO;
import com.hustict.aims.dto.order.OrderInformationDTO;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/v1/product-manager/orders")
@CrossOrigin(origins = "*")
public class OrderPMController {

    private final OrderService orderService;
    private final OrderQueryService orderQueryService;
    private final OrderEmailService orderEmailService;
    private final OrderCancellationService orderCancellationService;
    private final EmailSenderFactory emailSenderFactory;
    private final RefundSelector refundStrategySelector;

    @Autowired
    private OrderInfoService orderInfoService;

    public OrderPMController(OrderService orderService, 
                           OrderQueryService orderQueryService,
                           OrderEmailService orderEmailService,
                           OrderCancellationService orderCancellationService,
                           EmailSenderFactory emailSenderFactory, 
                           RefundSelector refundStrategySelector) {
        this.orderService = orderService;
        this.orderQueryService = orderQueryService;
        this.orderEmailService = orderEmailService;
        this.orderCancellationService = orderCancellationService;
        this.emailSenderFactory = emailSenderFactory;
        this.refundStrategySelector = refundStrategySelector;
    }

    @GetMapping("/pending")
    public ResponseEntity<Map<String, Object>> getPendingOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "30") int size) {
        try {
            List<OrderInformationDTO> orders = orderQueryService.getPendingOrders(page, size);
            long totalOrders = orderQueryService.getTotalPendingOrders();
            
            Map<String, Object> response = new HashMap<>();
            response.put("orders", orders);
            response.put("totalOrders", totalOrders);
            response.put("currentPage", page);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @PutMapping("/{id}/approve")
    public ResponseEntity<Void> approveOrder(@PathVariable Long id,HttpSession session) {
        orderService.approveOrder(id);
        orderEmailService.prepareOrderSessionForEmail("approveOrder", id, session);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/reject")
    public ResponseEntity<Void> rejectOrder(@PathVariable Long id,HttpSession session) {
        orderService.rejectOrder(id);
        orderEmailService.prepareOrderSessionForEmail("rejectOrder", id, session);
    
        System.out.println("Refund order "+ id);
        refundStrategySelector.refund(id);
        return ResponseEntity.ok().build();
    }

} 