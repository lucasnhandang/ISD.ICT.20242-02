package com.hustict.aims.service.placeOrder;
import com.hustict.aims.dto.deliveryForm.DeliveryFormDTO;


public interface DeliveryFormValidator {
    void validate(DeliveryFormDTO form, String sessionId);
}