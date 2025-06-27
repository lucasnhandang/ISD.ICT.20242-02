package com.hustict.aims.service.placeRushOrder;

import com.hustict.aims.dto.deliveryForm.DeliveryFormDTO;
import com.hustict.aims.exception.RushOrderException;
import org.springframework.stereotype.Service;

@Service
public class RushOrderValidationService {

    public void validateRushOrderInfo(DeliveryFormDTO deliveryInfo) {
        // Kiểm tra expected delivery time
        if (deliveryInfo.getExpectedDateTime() == null) {
            throw new RushOrderException(
                "Missing expected delivery time for rush order.",
                "MISSING_EXPECTED_TIME",
                null
            );
        }

        // Kiểm tra delivery instructions
        if (deliveryInfo.getDeliveryInstructions() == null || 
            deliveryInfo.getDeliveryInstructions().trim().isEmpty()) {
            throw new RushOrderException(
                "Missing delivery instructions for rush order.",
                "MISSING_DELIVERY_INSTRUCTIONS",
                deliveryInfo.getExpectedDateTime()
            );
        }

        // Có thể thêm validation cho expected time (ví dụ: phải trong tương lai, 
        // phải trong khung giờ làm việc, etc.)
        validateExpectedTime(deliveryInfo.getExpectedDateTime());
    }

    private void validateExpectedTime(java.time.LocalDateTime expectedTime) {
        java.time.LocalDateTime now = java.time.LocalDateTime.now();
        
        // Kiểm tra thời gian phải trong tương lai
        if (expectedTime.isBefore(now)) {
            throw new RushOrderException(
                "Expected delivery time must be in the future.",
                "INVALID_EXPECTED_TIME",
                expectedTime
            );
        }

        // Kiểm tra thời gian phải trong vòng 2 giờ tới
        java.time.LocalDateTime maxTime = now.plusHours(2);
        if (expectedTime.isAfter(maxTime)) {
            throw new RushOrderException(
                "Rush order delivery time must be within 2 hours from now.",
                "EXPECTED_TIME_TOO_FAR",
                expectedTime
            );
        }
    }
} 