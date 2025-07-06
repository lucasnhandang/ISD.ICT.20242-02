package com.hustict.aims.controller;

import com.hustict.aims.service.email.EmailSenderFactory;
import com.hustict.aims.service.email.OrderInfoService;
import com.hustict.aims.service.order.ApproveService;
import com.hustict.aims.service.order.ApproveServiceImpl;
import com.hustict.aims.service.order.OrderService;
import com.hustict.aims.service.order.RejectServiceImpl;
import com.hustict.aims.service.refund.RefundSelector;
import com.hustict.aims.dto.order.OrderDTO;
import com.hustict.aims.dto.order.OrderInformationDTO;
import com.hustict.aims.repository.OrderRepository;

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
    private final EmailSenderFactory emailSenderFactory;
    private final RefundSelector refundStrategySelector;
    
    @Autowired
    private ApproveServiceImpl approveService;
    
    
    @Autowired
    private RejectServiceImpl rejectService;
    

    @Autowired
    private OrderInfoService orderInfoService;

    public OrderPMController(OrderService orderService, EmailSenderFactory emailSenderFactory, RefundSelector refundStrategySelector) {
        this.orderService = orderService;
        this.emailSenderFactory = emailSenderFactory;
        this.refundStrategySelector = refundStrategySelector;
    }

    @GetMapping("/pending")
    public ResponseEntity<Map<String, Object>> getPendingOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "30") int size) {
        try {
            List<OrderInformationDTO> orders = orderService.getPendingOrders(page, size);
            long totalOrders = orderService.getTotalPendingOrders();
            
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
        approveService.approveOrder(id);
        orderService.prepareOrderSessionForEmail("approveOrder",id, session);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/reject")
    public ResponseEntity<Void> rejectOrder(@PathVariable Long id,HttpSession session) {
        rejectService.rejectOrder(id);
        orderService.prepareOrderSessionForEmail("rejectOrder",id, session);
    
        System.out.println("Refund order "+ id);
        refundStrategySelector.refund(id);
        return ResponseEntity.ok().build();
    }

} 