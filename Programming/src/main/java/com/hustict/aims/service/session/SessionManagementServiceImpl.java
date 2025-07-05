package com.hustict.aims.service.session;
import org.springframework.stereotype.Service;

import com.hustict.aims.dto.cart.CartRequestDTO;
import com.hustict.aims.dto.deliveryForm.DeliveryFormDTO;

import jakarta.servlet.http.HttpSession;

@Service
public class SessionManagementServiceImpl implements SessionManagementService {
    private static final String CART_KEY = "cartRequested";
    private static final String DELIVERY_FORM_KEY = "deliveryForm";
    // private static final String INVOICE_KEY = "invoice";
    // private static final String PAYMENT_KEY = "paymentTransaction";

    @Override
    public void saveCart(HttpSession session, CartRequestDTO cart) {
        session.setAttribute(CART_KEY, cart);
    }

    @Override
    public CartRequestDTO getCart(HttpSession session) {
        return (CartRequestDTO) session.getAttribute(CART_KEY);
    }

    @Override
    public void saveDeliveryForm(HttpSession session, DeliveryFormDTO form) {
        session.setAttribute(DELIVERY_FORM_KEY, form);
    }

    @Override
    public DeliveryFormDTO getDeliveryForm(HttpSession session) {
        return (DeliveryFormDTO) session.getAttribute(DELIVERY_FORM_KEY);
    }
}