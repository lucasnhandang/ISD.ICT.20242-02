// package com.hustict.aims.controller;

// import com.hustict.aims.service.email.EmailSenderFactory;
// import com.hustict.aims.service.order.OrderService;
// import com.hustict.aims.service.refund.PaymentSystem;
// import com.hustict.aims.dto.order.OrderInformationDTO;

// import jakarta.servlet.http.HttpSession;

// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.*;

// import java.util.List;

// @RestController
// @RequestMapping("/api/v1/product-manager/orders")
// @CrossOrigin(origins = "*")
// public class OrderPMController {

//     private final OrderService orderService;
//     private final EmailSenderFactory emailSenderFactory;
//     private final PaymentSystem paymentSystem;

//     public OrderPMController(OrderService orderService, EmailSenderFactory emailSenderFactory, PaymentSystem paymentSystem) {
//         this.orderService = orderService;
//         this.emailSenderFactory = emailSenderFactory;
//         this.paymentSystem = paymentSystem;
//     }

//     @GetMapping("/pending")
//     public ResponseEntity<List<OrderInformationDTO>> getPendingOrders() {
//         return ResponseEntity.ok(orderService.getPendingOrders());
//     }

//     @PutMapping("/{id}/approve")
//     public ResponseEntity<Void> approveOrder(@PathVariable Long id,HttpSession session) {
//         orderService.approveOrder(id);
//         orderService.prepareOrderSessionForEmail(id, session);
//         emailSenderFactory.process("approveOrder", session);
//         return ResponseEntity.ok().build();
//     }

//     @PutMapping("/{id}/reject")
//     public ResponseEntity<Void> rejectOrder(@PathVariable Long id,HttpSession session) {
//         orderService.rejectOrder(id);
//         orderService.prepareOrderSessionForEmail(id, session);
//         emailSenderFactory.process("rejectOrder", session);
//         return ResponseEntity.ok().build();
//     }

//     @PutMapping("/{id}/refund")
//     public ResponseEntity<Void> refund(@PathVariable Long id,HttpSession session) {
//         String system = (String) session.getAttribute("system");
//         paymentSystem.processRefund(id, system);

//         // emailSenderFactory.process("rejectOrder", session);
//         return ResponseEntity.ok().build();
//     }
// } 