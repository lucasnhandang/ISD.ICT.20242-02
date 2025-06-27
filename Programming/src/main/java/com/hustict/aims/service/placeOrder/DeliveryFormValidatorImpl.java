package com.hustict.aims.service.placeOrder;

import com.hustict.aims.dto.deliveryForm.DeliveryFormDTO;
import com.hustict.aims.exception.DeliveryFormValidationException;

import java.util.List;
import java.util.ArrayList;
import org.springframework.stereotype.Service;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class DeliveryFormValidatorImpl implements DeliveryFormValidator {

    @Override
    public void validate(DeliveryFormDTO form, String sessionId) {
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

        if (form.getEmail() == null || form.getEmail().isEmpty()) {
            errors.add("Email is required");
        } else if (!isValidEmail(form.getEmail())) {
            errors.add("Invalid email format");
        }
        if (form.getPhoneNumber() == null || form.getPhoneNumber().isEmpty()) {
            errors.add("Phone number is required");
        } else if (!isValidPhoneNumber(form.getPhoneNumber())) {
            errors.add("Invalid phone number format");
        }

        if (!errors.isEmpty()) {
            throw new DeliveryFormValidationException(errors);
        }

        
        // xóa
        System.out.println("Delivery form saved for session: " + sessionId);
        System.out.println("Delivery Info: " + form.getCustomerName() + ", " + form.getDeliveryProvince());
    }
    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";  // Biểu thức chính quy kiểm tra định dạng email
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
    private boolean isValidPhoneNumber(String phoneNumber) {
        String phoneRegex = "^(0[3|5|7|8|9])+([0-9]{8})$";  // Biểu thức chính quy cho số điện thoại Việt Nam
        Pattern pattern = Pattern.compile(phoneRegex);
        Matcher matcher = pattern.matcher(phoneNumber);
        return matcher.matches();
    }
}

