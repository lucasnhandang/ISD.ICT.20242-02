package com.hustict.aims.service;

import com.hustict.aims.dto.order.RushOrderRequestDTO;
import com.hustict.aims.dto.order.RushOrderResponseDTO;
import com.hustict.aims.dto.cart.CartRequestDTO;
import com.hustict.aims.dto.deliveryForm.DeliveryFormDTO;
import com.hustict.aims.service.placeRushOrder.RushOrderEligibilityService;
import com.hustict.aims.service.placeRushOrder.RushOrderValidationService;
import com.hustict.aims.service.placeRushOrder.RushOrderProcessingService;
import com.hustict.aims.service.placeRushOrder.RushOrderSaveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RushOrderServiceImpl implements RushOrderService {
    
    private final RushOrderEligibilityService rushOrderEligibilityService;
    private final RushOrderValidationService rushOrderValidationService;
    private final RushOrderProcessingService rushOrderProcessingService;
    private final RushOrderSaveService rushOrderSaveService;

    @Autowired
    public RushOrderServiceImpl(RushOrderEligibilityService rushOrderEligibilityService,
                               RushOrderValidationService rushOrderValidationService,
                               RushOrderProcessingService rushOrderProcessingService,
                               RushOrderSaveService rushOrderSaveService) {
        this.rushOrderEligibilityService = rushOrderEligibilityService;
        this.rushOrderValidationService = rushOrderValidationService;
        this.rushOrderProcessingService = rushOrderProcessingService;
        this.rushOrderSaveService = rushOrderSaveService;
    }

    @Override
    public RushOrderResponseDTO processRushOrder(RushOrderRequestDTO request, jakarta.servlet.http.HttpSession session) {
        CartRequestDTO cart = request.getCart();
        DeliveryFormDTO deliveryInfo = request.getDeliveryInfo();
        
        // Bước 1: Kiểm tra điều kiện rush order
        var eligibility = rushOrderEligibilityService.checkEligibility(cart, deliveryInfo);
        
        // Bước 2: Validate thông tin rush order
        rushOrderValidationService.validateRushOrderInfo(deliveryInfo);
        
        // Bước 3: Xử lý rush order
        RushOrderResponseDTO response = rushOrderProcessingService.processRushOrder(
            cart, deliveryInfo, eligibility.getRushItems(), eligibility.getNormalItems(), session
        );
        
        // Bước 4: Lưu rush order vào database
        rushOrderSaveService.saveRushOrder(cart, deliveryInfo, response.getInvoiceList().get(0));
        
        return response;
    }
} 