package com.hustict.aims.service.placeOrder;
import com.hustict.aims.dto.deliveryForm.DeliveryFormDTO;


public interface DeliveryFormService {
    void submitDeliveryForm(DeliveryFormDTO form, String sessionId);
}