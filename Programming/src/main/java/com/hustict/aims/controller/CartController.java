package com.hustict.aims.controller;

import com.hustict.aims.dto.cart.CartRequestDTO;
import com.hustict.aims.exception.ProductOperationException;
import com.hustict.aims.dto.cart.CartItemRequestDTO;
import com.hustict.aims.service.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/v1/cart")
@CrossOrigin(origins = "*")
public class CartController {
    
    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping
    public ResponseEntity<CartRequestDTO> getCart(HttpSession session) {
        CartRequestDTO cart = cartService.getCart(session);
        return ResponseEntity.ok(cart);
    }

    @PostMapping("/add")
    public ResponseEntity<CartRequestDTO> addToCart(
            @RequestBody CartItemRequestDTO item,
            HttpSession session) throws ProductOperationException {
        CartRequestDTO updatedCart = cartService.addToCart(item, session);
        return ResponseEntity.ok(updatedCart);
    }

    @PostMapping("/update")
    public ResponseEntity<CartRequestDTO> updateCartItem(
            @RequestBody CartItemRequestDTO item,
            HttpSession session) throws ProductOperationException {
        CartRequestDTO updatedCart = cartService.updateCartItem(item, session);
        return ResponseEntity.ok(updatedCart);
    }

    @DeleteMapping("/items/{productId}")
    public ResponseEntity<CartRequestDTO> removeFromCart(
            @PathVariable Long productId,
            HttpSession session) {
        CartRequestDTO updatedCart = cartService.removeFromCart(productId, session);
        return ResponseEntity.ok(updatedCart);
    }

    @DeleteMapping("/clear")
    public ResponseEntity<Void> clearCart(HttpSession session) {
        cartService.clearCart(session);
        return ResponseEntity.ok().build();
    }
} 