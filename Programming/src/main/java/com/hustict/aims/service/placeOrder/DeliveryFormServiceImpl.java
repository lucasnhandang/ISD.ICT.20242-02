package com.hustict.aims.service.placeOrder;

import com.hustict.aims.dto.deliveryForm.DeliveryFormDTO;
import com.hustict.aims.exception.DeliveryFormValidationException;

import java.util.List;
import java.util.ArrayList;
import org.springframework.stereotype.Service;

@Service
public class DeliveryFormServiceImpl implements DeliveryFormService {

    @Override
    public void submitDeliveryForm(DeliveryFormDTO form, String sessionId) {
        List<String> errors = new ArrayList<>();

        if (form.getCustomerName() == null || form.getCustomerName().isEmpty()) {
            errors.add("Customer name is required");
        }

        if (form.getDeliveryAddress() == null || form.getDeliveryAddress().isEmpty()) {
            errors.add("Delivery address is required");
        }

        if (form.getDeliveryProvince() == null || form.getDeliveryProvince().isEmpty()) {
            errors.add("Delivery province is required");
        }

        if (!errors.isEmpty()) {
            throw new DeliveryFormValidationException(errors);
        }
        System.out.println("Delivery form saved for session: " + sessionId);
        System.out.println("Delivery Info: " + form.getCustomerName() + ", " + form.getDeliveryAddress());
    }
}

