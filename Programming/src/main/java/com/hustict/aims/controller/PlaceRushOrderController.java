package com.hustict.aims.controller;

import com.hustict.aims.dto.order.RushOrderEligibilityResponseDTO;
import com.hustict.aims.dto.order.RushOrderResponseDTO;
import com.hustict.aims.dto.deliveryForm.DeliveryFormDTO;
import com.hustict.aims.dto.invoice.InvoiceDTO;
import com.hustict.aims.service.placeRushOrder.RushOrderEligibilityService;
import com.hustict.aims.service.placeRushOrder.RushOrderValidationService;
import com.hustict.aims.service.placeRushOrder.RushOrderProcessingService;
import com.hustict.aims.service.placeRushOrder.RushOrderSaveService;
import com.hustict.aims.service.sessionValidator.SessionValidatorService;
import com.hustict.aims.service.placeOrder.PaymentHandlerService;
import com.hustict.aims.service.placeOrder.CartCleanupService;
import com.hustict.aims.service.email.EmailSenderFactory;
import com.hustict.aims.service.reservation.ReservationService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/place-rush-order")
public class PlaceRushOrderController {
    
    private final RushOrderEligibilityService rushOrderEligibilityService;
    private final RushOrderValidationService rushOrderValidationService;
    private final RushOrderProcessingService rushOrderProcessingService;
    private final RushOrderSaveService rushOrderSaveService;
    private final SessionValidatorService sessionValidatorService;
    private final PaymentHandlerService paymentHandlerService;
    private final CartCleanupService cartCleanupService;
    private final EmailSenderFactory emailSenderFactory;
    private final ReservationService reservationService;

    @Autowired
    public PlaceRushOrderController(RushOrderEligibilityService rushOrderEligibilityService,
                                   RushOrderValidationService rushOrderValidationService,
                                   RushOrderProcessingService rushOrderProcessingService,
                                   RushOrderSaveService rushOrderSaveService,
                                   SessionValidatorService sessionValidatorService,
                                   PaymentHandlerService paymentHandlerService,
                                   CartCleanupService cartCleanupService,
                                   EmailSenderFactory emailSenderFactory,
                                   ReservationService reservationService) {
        this.rushOrderEligibilityService = rushOrderEligibilityService;
        this.rushOrderValidationService = rushOrderValidationService;
        this.rushOrderProcessingService = rushOrderProcessingService;
        this.rushOrderSaveService = rushOrderSaveService;
        this.sessionValidatorService = sessionValidatorService;
        this.paymentHandlerService = paymentHandlerService;
        this.cartCleanupService = cartCleanupService;
        this.emailSenderFactory = emailSenderFactory;
        this.reservationService = reservationService;
    }

    /**
     * Bước 1: Kiểm tra điều kiện rush order
     * Sử dụng cart và delivery info từ session của Place Order
     */
    @PostMapping("/check-eligibility")
    public ResponseEntity<RushOrderEligibilityResponseDTO> checkRushOrderEligibility(HttpSession session) {
        // Validate session có đủ dữ liệu từ Place Order
        sessionValidatorService.validateDeliveryAndCartForCheckout(session);
        
        // Lấy dữ liệu từ session
        com.hustict.aims.dto.cart.CartRequestDTO cart = 
            (com.hustict.aims.dto.cart.CartRequestDTO) session.getAttribute("cartRequested");
        DeliveryFormDTO deliveryInfo = (DeliveryFormDTO) session.getAttribute("deliveryForm");
        
        // Kiểm tra điều kiện rush order
        RushOrderEligibilityResponseDTO response = 
            rushOrderEligibilityService.checkEligibility(cart, deliveryInfo);
        
        // Lưu kết quả vào session để sử dụng ở các bước tiếp theo
        session.setAttribute("rushEligibility", response);
        
        return ResponseEntity.ok(response);
    }

    /**
     * Bước 2: Submit thông tin rush order
     * Cập nhật delivery form với thông tin rush order
     */
    @PostMapping("/submit-rush-info")
    public ResponseEntity<String> submitRushOrderInfo(@RequestBody com.hustict.aims.dto.deliveryForm.RushDeliveryInfoDTO rushDeliveryInfo, 
                                                     HttpSession session) {
        // Validate session có đủ dữ liệu
        sessionValidatorService.validateDeliveryAndCartForCheckout(session);
        
        // Lấy deliveryForm đã lưu ở Place Order
        DeliveryFormDTO originalDeliveryInfo = (DeliveryFormDTO) session.getAttribute("deliveryForm");
        if (originalDeliveryInfo == null) {
            return ResponseEntity.badRequest().body("No delivery info found in session. Please submit delivery info first.");
        }
        // Cập nhật các trường rush
        originalDeliveryInfo.setExpectedDateTime(rushDeliveryInfo.getExpectedDateTime());
        originalDeliveryInfo.setDeliveryInstructions(rushDeliveryInfo.getDeliveryInstructions());
        originalDeliveryInfo.setRushOrder(true);

        // Chỉ validate các trường rush info
        rushOrderValidationService.validateRushOrderInfo(originalDeliveryInfo);

        // Lưu lại vào session
        session.setAttribute("deliveryForm", originalDeliveryInfo);
        session.setAttribute("rushDeliveryInfo", rushDeliveryInfo);
        
        return ResponseEntity.ok("Rush order information submitted successfully");
    }

    /**
     * Bước 3: Xử lý rush order và tạo invoice
     */
    @PostMapping("/process-rush-order")
    public ResponseEntity<RushOrderResponseDTO> processRushOrder(HttpSession session) {
        // Validate session có đủ dữ liệu
        sessionValidatorService.validateDeliveryAndCartForCheckout(session);
        // Lấy dữ liệu từ session
        com.hustict.aims.dto.cart.CartRequestDTO cart = 
            (com.hustict.aims.dto.cart.CartRequestDTO) session.getAttribute("cartRequested");
        DeliveryFormDTO deliveryInfo = (DeliveryFormDTO) session.getAttribute("deliveryForm");
        RushOrderEligibilityResponseDTO eligibility = 
            (RushOrderEligibilityResponseDTO) session.getAttribute("rushEligibility");
        if (eligibility == null) {
            throw new IllegalStateException("Rush order eligibility not checked. Please check eligibility first.");
        }
        // Xử lý rush order
        RushOrderResponseDTO response = rushOrderProcessingService.processRushOrder(
            cart, deliveryInfo, eligibility.getRushItems(), eligibility.getNormalItems(), session
        );
        // Không cần setAttribute("invoice", ...) nữa vì đã lưu danh sách invoice/cart vào session
        session.setAttribute("rushOrderResponse", response);
        return ResponseEntity.ok(response);
    }

    /**
     * Bước 4: Lưu rush order vào database
     */
    @PostMapping("/save-rush-order")
    public ResponseEntity<String> saveRushOrder(HttpSession session) {
        // Validate session có đủ dữ liệu
        sessionValidatorService.validateDeliveryAndCartForCheckout(session);
        // Gọi paymentHandlerService để lưu order (dùng SaveOrderService.saveAll bên trong)
        paymentHandlerService.handlePaymentSuccess(session);
        return ResponseEntity.ok("Rush order saved successfully (using common save order logic)");
    }

    /**
     * Bước 5: Xử lý thanh toán (dùng chung với Place Order)
     */
    @PostMapping("/handle-payment")
    public ResponseEntity<String> handlePaymentSuccess(HttpSession session) {
        // Validate session có đủ dữ liệu
        sessionValidatorService.validateAfterPayment(session);
        
        // Xử lý thanh toán (dùng chung logic với Place Order)
        paymentHandlerService.handlePaymentSuccess(session);
        
        return ResponseEntity.ok("Rush order payment processed successfully");
    }

    /**
     * Hủy rush order
     */
    @PostMapping("/cancel")
    public ResponseEntity<String> cancelRushOrder(HttpSession session) {
        // Xóa các attributes liên quan đến rush order
        session.removeAttribute("rushEligibility");
        session.removeAttribute("rushDeliveryInfo");
        session.removeAttribute("rushOrderResponse");
        session.removeAttribute("invoice");
        session.removeAttribute("orderInformation");
        
        // Giải phóng reservation
        reservationService.releaseReservation(session);
        
        return ResponseEntity.ok("Rush order has been canceled");
    }

    @PostMapping("/pay-invoice")
    public ResponseEntity<String> payInvoice(@RequestParam Long invoiceId, HttpSession session) {
        java.util.List<com.hustict.aims.dto.invoice.InvoiceDTO> invoiceList = (java.util.List<com.hustict.aims.dto.invoice.InvoiceDTO>) session.getAttribute("invoiceList");
        java.util.List<com.hustict.aims.dto.cart.CartRequestDTO> cartList = (java.util.List<com.hustict.aims.dto.cart.CartRequestDTO>) session.getAttribute("cartList");
        if (invoiceList == null || cartList == null) {
            return ResponseEntity.badRequest().body("No invoices found in session.");
        }
        int idx = -1;
        for (int i = 0; i < invoiceList.size(); i++) {
            if (invoiceId.equals(invoiceList.get(i).getId())) {
                idx = i;
                break;
            }
        }
        if (idx == -1) return ResponseEntity.badRequest().body("Invoice not found.");
        com.hustict.aims.dto.invoice.InvoiceDTO invoiceToPay = invoiceList.get(idx);
        com.hustict.aims.dto.cart.CartRequestDTO cartToPay = cartList.get(idx);

        // Lưu vào session để SaveOrderService lấy đúng dữ liệu
        session.setAttribute("cartRequested", cartToPay);
        session.setAttribute("invoice", invoiceToPay);
        paymentHandlerService.handlePaymentSuccess(session);

        // Xóa invoice/cart đã thanh toán khỏi list
        invoiceList.remove(idx);
        cartList.remove(idx);
        session.setAttribute("invoiceList", invoiceList);
        session.setAttribute("cartList", cartList);

        return ResponseEntity.ok("Invoice paid successfully.");
    }
} 