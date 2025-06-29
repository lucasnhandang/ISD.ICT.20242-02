package com.hustict.aims.controller;

import com.hustict.aims.dto.order.RushOrderEligibilityResponseDTO;
import com.hustict.aims.dto.order.RushOrderResponseDTO;
import com.hustict.aims.dto.deliveryForm.DeliveryFormDTO;
import com.hustict.aims.dto.invoice.InvoiceDTO;
import com.hustict.aims.service.placeRushOrder.RushOrderEligibilityService;
import com.hustict.aims.service.placeRushOrder.RushOrderValidationService;
import com.hustict.aims.service.placeRushOrder.RushOrderProcessingService;
import com.hustict.aims.service.sessionValidator.SessionValidatorService;
import com.hustict.aims.service.placeOrder.PaymentHandlerService;
import com.hustict.aims.service.placeOrder.CartCleanupService;
import com.hustict.aims.service.placeOrder.SaveTempOrder;
import com.hustict.aims.service.email.EmailSenderFactory;
import com.hustict.aims.service.reservation.ReservationService;
import java.util.HashMap;
import java.util.Map;
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
    private final SessionValidatorService sessionValidatorService;
    private final PaymentHandlerService paymentHandlerService;
    private final CartCleanupService cartCleanupService;
    private final SaveTempOrder saveTempOrder;
    private final EmailSenderFactory emailSenderFactory;
    private final ReservationService reservationService;

    @Autowired
    public PlaceRushOrderController(RushOrderEligibilityService rushOrderEligibilityService,
                                   RushOrderValidationService rushOrderValidationService,
                                   RushOrderProcessingService rushOrderProcessingService,
                                   SessionValidatorService sessionValidatorService,
                                   PaymentHandlerService paymentHandlerService,
                                   CartCleanupService cartCleanupService,
                                   SaveTempOrder saveTempOrder,
                                   EmailSenderFactory emailSenderFactory,
                                   ReservationService reservationService) {
        this.rushOrderEligibilityService = rushOrderEligibilityService;
        this.rushOrderValidationService = rushOrderValidationService;
        this.rushOrderProcessingService = rushOrderProcessingService;
        this.sessionValidatorService = sessionValidatorService;
        this.paymentHandlerService = paymentHandlerService;
        this.cartCleanupService = cartCleanupService;
        this.saveTempOrder = saveTempOrder;
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
     * Bước 4: Lưu multiple temp orders và chuẩn bị cho thanh toán từng invoice
     */
    @PostMapping("/save-rush-orders")
    public ResponseEntity<Map<String, Object>> saveRushOrders(HttpSession session) {
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

        // Xử lý rush order để tạo invoices
        RushOrderResponseDTO processResult = rushOrderProcessingService.processRushOrder(
            cart, deliveryInfo, eligibility.getRushItems(), eligibility.getNormalItems(), session
        );

        // Lưu multiple temp orders
        Map<String, Long> orderIds = saveMultipleOrders(
            processResult.getCartList(), 
            deliveryInfo, 
            processResult.getInvoiceList()
        );

        // Chuẩn bị response
        Map<String, Object> response = new HashMap<>();
        
        // Thêm order IDs
        if (orderIds.containsKey("rush")) {
            response.put("rushOrderId", orderIds.get("rush"));
            response.put("rushInvoice", processResult.getInvoiceList().get(0));
        }
        
        if (orderIds.containsKey("normal")) {
            response.put("normalOrderId", orderIds.get("normal"));
            // Normal invoice sẽ là phần tử thứ 2 nếu có rush, hoặc phần tử đầu nếu không có rush
            int normalInvoiceIndex = orderIds.containsKey("rush") ? 1 : 0;
            response.put("normalInvoice", processResult.getInvoiceList().get(normalInvoiceIndex));
        }
        
        response.put("message", "Rush orders saved successfully. You can now pay for each order separately.");
        response.put("expectedDateTime", deliveryInfo.getExpectedDateTime());

        // Lưu order IDs vào session để sử dụng cho payment
        session.setAttribute("rushOrderIds", orderIds);
        
        return ResponseEntity.ok(response);
    }

    /**
     * Helper method để lưu multiple orders
     */
    private Map<String, Long> saveMultipleOrders(java.util.List<com.hustict.aims.dto.cart.CartRequestDTO> cartList, 
                                                 DeliveryFormDTO deliveryInfo, 
                                                 java.util.List<InvoiceDTO> invoiceList) {
        Map<String, Long> orderIds = new HashMap<>();
        
        for (int i = 0; i < cartList.size(); i++) {
            com.hustict.aims.dto.cart.CartRequestDTO currentCart = cartList.get(i);
            InvoiceDTO currentInvoice = invoiceList.get(i);
            
            // Save temp order
            Long orderId = saveTempOrder.save(currentCart, deliveryInfo, currentInvoice);
            
            // Phân loại order theo rush flag
            if (currentCart.isRushOrder()) {
                orderIds.put("rush", orderId);
            } else {
                orderIds.put("normal", orderId);
            }
        }
        
        return orderIds;
    }
    
} 