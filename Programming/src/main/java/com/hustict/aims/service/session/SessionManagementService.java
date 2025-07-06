package com.hustict.aims.service.session;

import com.hustict.aims.dto.cart.CartRequestDTO;
import com.hustict.aims.dto.deliveryForm.DeliveryFormDTO;
import com.hustict.aims.dto.invoice.InvoiceDTO;

import java.util.List;

import jakarta.servlet.http.HttpSession;

public interface SessionManagementService {
    void saveCart(HttpSession session, CartRequestDTO cart);
    CartRequestDTO getCart(HttpSession session);

    void saveDeliveryForm(HttpSession session, DeliveryFormDTO form);
    DeliveryFormDTO getDeliveryForm(HttpSession session);
    
    void saveCartList(HttpSession session, List<CartRequestDTO> cartList);
    void saveInvoiceList(HttpSession session, List<InvoiceDTO> invoiceList);

    void addToCartList(HttpSession session, CartRequestDTO cart);
    void addToInvoiceList(HttpSession session, InvoiceDTO invoice);
}
