package com.hustict.aims.service.session;
import org.springframework.stereotype.Service;

import com.hustict.aims.dto.cart.CartRequestDTO;
import com.hustict.aims.dto.deliveryForm.DeliveryFormDTO;
import com.hustict.aims.dto.invoice.InvoiceDTO;

import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.http.HttpSession;

@Service
public class SessionManagementServiceImpl implements SessionManagementService {
    private static final String CART_KEY = "cartRequested";
    private static final String DELIVERY_FORM_KEY = "deliveryForm";
    private static final String CART_LIST_KEY = "cartList";
    private static final String INVOICE_LIST_KEY = "invoiceList";
    
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

    @Override
    public void saveCartList(HttpSession session, List<CartRequestDTO> cartList) {
        session.setAttribute(CART_LIST_KEY, cartList);
    }

    @Override
    public void saveInvoiceList(HttpSession session, List<InvoiceDTO> invoiceList) {
        session.setAttribute(INVOICE_LIST_KEY, invoiceList);
    }

    @Override
    public void addToCartList(HttpSession session, CartRequestDTO cart) {
        List<CartRequestDTO> cartList = (List<CartRequestDTO>) session.getAttribute(CART_LIST_KEY);
        if (cartList == null) cartList = new ArrayList<>();
        cartList.add(cart);
        session.setAttribute(CART_LIST_KEY, cartList);
    }

    @Override
    public void addToInvoiceList(HttpSession session, InvoiceDTO invoice) {
        List<InvoiceDTO> invoiceList = (List<InvoiceDTO>) session.getAttribute(INVOICE_LIST_KEY);
        if (invoiceList == null) invoiceList = new ArrayList<>();
        invoiceList.add(invoice);
        session.setAttribute(INVOICE_LIST_KEY, invoiceList);
    }
}