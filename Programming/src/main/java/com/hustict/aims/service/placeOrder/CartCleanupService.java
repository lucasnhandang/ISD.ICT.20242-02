package com.hustict.aims.service.placeOrder;

import com.hustict.aims.dto.cart.CartItemRequestDTO;
import com.hustict.aims.dto.cart.CartRequestDTO;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CartCleanupService {

    public void removePurchasedItems(HttpSession session) {
        CartRequestDTO cart = (CartRequestDTO) session.getAttribute("cart");
        CartRequestDTO cartRequested = (CartRequestDTO) session.getAttribute("cartRequested");

        if (cart == null || cart.getProductList() == null || 
            cartRequested == null || cartRequested.getProductList() == null) {
            return;
        }

        Set<Long> purchasedIds = cartRequested.getProductList().stream()
            .map(CartItemRequestDTO::getProductID)
            .collect(Collectors.toSet());

        // Giữ lại các sản phẩm chưa thanh toán
        List<CartItemRequestDTO> remainingItems = cart.getProductList().stream()
            .filter(item -> !purchasedIds.contains(item.getProductID()))
            .collect(Collectors.toList());

        cart.setProductList(remainingItems);

        cart.setTotalItem(remainingItems.stream().mapToInt(CartItemRequestDTO::getQuantity).sum());
        cart.setTotalPrice(remainingItems.stream()
            .mapToInt(item -> item.getQuantity() * item.getPrice())
            .sum());

        session.setAttribute("cart", cart);
    }
}
