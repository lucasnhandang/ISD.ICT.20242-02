package com.hustict.aims.service.placeRushOrder;

import com.hustict.aims.dto.deliveryForm.DeliveryFormDTO;
import com.hustict.aims.dto.cart.CartItemRequestDTO;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class RushOrderValidator {
    public boolean validateAddress(DeliveryFormDTO deliveryInfo) {
        return deliveryInfo != null && "Hanoi".equalsIgnoreCase(deliveryInfo.getDeliveryProvince());
    }
    public boolean validateRushItems(List<CartItemRequestDTO> rushItems) {
        return rushItems != null && !rushItems.isEmpty();
    }
} 