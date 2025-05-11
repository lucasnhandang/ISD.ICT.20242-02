package com.hustict.aims.controller;
import com.hustict.aims.model.*;
import com.hustict.aims.service.*;
/* 
The RejectOrderController has functional cohesion.
It holds the responsibility of executing the reject order process by calling the corresponding services, and it should only change when the reject order workflow itself changes (e.g., adding, removing, or reordering steps).
*/
public class RejectOrderController {
    private final OrderRejectionService rejectionService;
    private final OrderEmailService orderEmailService;
    private final VNPayRefundService vnpayRefundService;
    private final OrderRefundService orderRefundService;
    private final RefundEmailService refundEmailService;

    // Constructor
    public RejectOrderController(OrderRejectionService rejectionService,
                                 OrderEmailService orderEmailService,
                                 VNPayRefundService vnpayRefundService,
                                 OrderRefundService orderRefundService,
                                 RefundEmailService refundEmailService) {
        this.rejectionService = rejectionService;
        this.orderEmailService = orderEmailService;
        this.vnpayRefundService = vnpayRefundService;
        this.orderRefundService = orderRefundService;
        this.refundEmailService = refundEmailService;
    }

    // Executing the reject order process
    public void rejectOrder(Order order, String reason) {
        rejectionService.reject(order, reason);
        orderEmailService.sendRejectionEmail(order);
        vnpayRefundService.refund(order);
        orderRefundService.updateRefundStatus(order);
        refundEmailService.sendRefundConfirmation(order);
    }
    
}
