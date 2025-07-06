package com.hustict.aims.service.placeOrder.confirm;

import com.hustict.aims.dto.cart.CartItemRequestDTO;
import com.hustict.aims.dto.cart.CartRequestDTO;
import com.hustict.aims.dto.invoice.InvoiceDTO;
import com.hustict.aims.repository.OrderRepository;
import com.hustict.aims.model.order.Order;

import org.springframework.beans.factory.annotation.Autowired;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartCleanupServiceImpl implements CartCleanupService {
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

        List<CartItemRequestDTO> mergedProductList = updatedCart.stream()
            .filter(cart -> cart.getProductList() != null)
            .flatMap(cart -> cart.getProductList().stream())
            .collect(Collectors.toList());

        CartRequestDTO mergedCart = new CartRequestDTO();
        mergedCart.setProductList(mergedProductList);

        // Tính tổng item
        int totalItem = mergedProductList.stream()
            .mapToInt(CartItemRequestDTO::getQuantity)
            .sum();
        mergedCart.setTotalItem(totalItem);

        // Tính tổng giá
        int totalPrice = mergedProductList.stream()
            .mapToInt(item -> item.getQuantity() * item.getPrice())
            .sum();
        mergedCart.setTotalPrice(totalPrice);

        // Tùy chọn: giữ currency/rushOrder/discount nếu muốn
        mergedCart.setCurrency("VND"); // hoặc lấy từ cart đầu tiên

        session.setAttribute("cart", mergedCart);
        
        List<InvoiceDTO> updatedInvoiceList = invoiceList.stream()
            .filter(invoice -> invoice.isRushOrder() != isRushOrder)
            .collect(Collectors.toList());

        session.setAttribute("invoiceList", updatedInvoiceList);
        
      

        // In ra invoiceList
        System.out.println("=======CLEANED UP INVOICE LIST =======");
        if (invoiceList != null) {
            invoiceList.forEach(invoice -> System.out.println(invoice.toString()));
        } 
       
    }
}
