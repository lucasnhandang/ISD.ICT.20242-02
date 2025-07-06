package com.hustict.aims.service.placeOrder;

import com.hustict.aims.dto.cart.CartRequestDTO;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface NormalOrderHandlerService {
    ResponseEntity<Map<String, Object>> handleNormalOrder(HttpSession session, CartRequestDTO cart);
}
