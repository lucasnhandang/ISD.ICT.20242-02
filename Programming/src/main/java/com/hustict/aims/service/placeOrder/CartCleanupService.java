package com.hustict.aims.service.placeOrder;

import com.hustict.aims.dto.cart.CartItemRequestDTO;
import com.hustict.aims.dto.cart.CartRequestDTO;
import com.hustict.aims.dto.invoice.InvoiceDTO;
import com.hustict.aims.repository.OrderRepository;
import com.hustict.aims.model.order.Order;

import org.springframework.beans.factory.annotation.Autowired;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CartCleanupService {
    @Autowired
    OrderRepository orderRepository;

    public void removePurchasedItems(HttpSession session, Long orderid) {
        
        Order order = orderRepository.findById(orderid)
            .orElseThrow(() -> new IllegalArgumentException("Order not found with id: " + orderid));

        boolean isRushOrder = order.getIsRushOrder();

        @SuppressWarnings("unchecked")
        List<CartRequestDTO> cartList = (List<CartRequestDTO>) session.getAttribute("cartList");

        @SuppressWarnings("unchecked")
        List<InvoiceDTO> invoiceList = (List<InvoiceDTO>) session.getAttribute("invoiceList");

        if (cartList == null) return; 

        List<CartRequestDTO> updatedCart = cartList.stream()
            .filter(item -> item.isRushOrder() != isRushOrder) 
            .collect(Collectors.toList());

        session.setAttribute("cartRequested", updatedCart);

        List<InvoiceDTO> updatedInvoiceList = invoiceList.stream()
            .filter(invoice -> invoice.isRushOrder() != isRushOrder)
            .collect(Collectors.toList());

        session.setAttribute("invoiceList", updatedInvoiceList);
        
        System.out.println("=======CLEANED UP CART LIST =======");
        if (cartList != null) {
            cartList.forEach(cart -> System.out.println(cart.toString()));
        }

        // In ra invoiceList
        System.out.println("=======CLEANED UP INVOICE LIST =======");
        if (invoiceList != null) {
            invoiceList.forEach(invoice -> System.out.println(invoice.toString()));
        } 
       
    }
}
