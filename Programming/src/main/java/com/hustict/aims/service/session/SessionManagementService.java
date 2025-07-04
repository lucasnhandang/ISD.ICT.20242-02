package com.hustict.aims.service.session;

import com.hustict.aims.dto.cart.CartRequestDTO;
import com.hustict.aims.dto.deliveryForm.DeliveryFormDTO;

import jakarta.servlet.http.HttpSession;

public interface SessionManagementService {
    void saveCart(HttpSession session, CartRequestDTO cart);
    CartRequestDTO getCart(HttpSession session);

    void saveDeliveryForm(HttpSession session, DeliveryFormDTO form);
    DeliveryFormDTO getDeliveryForm(HttpSession session);

}
