package com.hustict.aims.service;

import com.hustict.aims.dto.cart.CartRequestDTO;
import com.hustict.aims.dto.cart.CartItemRequestDTO;
import com.hustict.aims.model.product.Product;
import com.hustict.aims.service.product.ProductService;
import com.hustict.aims.repository.product.ProductRepository;
import com.hustict.aims.exception.ProductOperationException;
import org.springframework.stereotype.Service;
import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.NoSuchElementException;

@Service
public class CartService {
    
    private final ProductService productService;
    private final ProductRepository productRepository;

    public CartService(ProductService productService, ProductRepository productRepository) {
        this.productService = productService;
        this.productRepository = productRepository;
    }

    public CartRequestDTO getCart(HttpSession session) {
        CartRequestDTO cart = (CartRequestDTO) session.getAttribute("cart");
        if (cart == null) {
            cart = new CartRequestDTO();
            cart.setProductList(new ArrayList<>());
            cart.setTotalPrice(0);
            cart.setTotalItem(0);
            cart.setCurrency("VND");
            cart.setDiscount(0);
            session.setAttribute("cart", cart);
        }
        return cart;
    }

    public CartRequestDTO addToCart(CartItemRequestDTO item, HttpSession session) throws ProductOperationException {
        // Kiểm tra sản phẩm tồn tại
        Product product = productRepository.findById(item.getProductID())
            .orElseThrow(() -> new NoSuchElementException("Product not found with ID: " + item.getProductID()));

        // Kiểm tra số lượng có sẵn
        if (!productService.isProductAvailable(item.getProductID(), item.getQuantity())) {
            throw new ProductOperationException("Not enough quantity available");
        }

        CartRequestDTO cart = getCart(session);
        
        // Kiểm tra sản phẩm đã có trong giỏ hàng chưa
        CartItemRequestDTO existingItem = cart.getProductList().stream()
            .filter(i -> i.getProductID().equals(item.getProductID()))
            .findFirst()
            .orElse(null);

        if (existingItem != null) {
            // Cập nhật số lượng nếu sản phẩm đã tồn tại
            int newQuantity = existingItem.getQuantity() + item.getQuantity();
            if (!productService.isProductAvailable(item.getProductID(), newQuantity)) {
                throw new ProductOperationException("Not enough quantity available");
            }
            existingItem.setQuantity(newQuantity);
        } else {
            // Thêm sản phẩm mới vào giỏ hàng
            item.setProductName(product.getTitle());
            item.setPrice(product.getCurrentPrice());
            item.setWeight(product.getWeight());
            cart.getProductList().add(item);
        }

        // Cập nhật tổng
        updateCartTotals(cart);
        session.setAttribute("cart", cart);
        
        return cart;
    }

    public CartRequestDTO updateCartItem(CartItemRequestDTO item, HttpSession session) throws ProductOperationException {
        // Kiểm tra số lượng có sẵn
        if (!productService.isProductAvailable(item.getProductID(), item.getQuantity())) {
            throw new ProductOperationException("Not enough quantity available");
        }

        CartRequestDTO cart = getCart(session);
        
        // Tìm và cập nhật sản phẩm trong giỏ hàng
        CartItemRequestDTO existingItem = cart.getProductList().stream()
            .filter(i -> i.getProductID().equals(item.getProductID()))
            .findFirst()
            .orElseThrow(() -> new NoSuchElementException("Item not found in cart"));

        existingItem.setQuantity(item.getQuantity());
        
        // Cập nhật tổng
        updateCartTotals(cart);
        session.setAttribute("cart", cart);
        
        return cart;
    }

    public CartRequestDTO removeFromCart(Long productId, HttpSession session) {
        CartRequestDTO cart = getCart(session);
        
        cart.getProductList().removeIf(item -> item.getProductID().equals(productId));
        
        // Cập nhật tổng
        updateCartTotals(cart);
        session.setAttribute("cart", cart);
        
        return cart;
    }

    public void clearCart(HttpSession session) {
        CartRequestDTO cart = new CartRequestDTO();
        cart.setProductList(new ArrayList<>());
        cart.setTotalPrice(0);
        cart.setTotalItem(0);
        cart.setCurrency("VND");
        cart.setDiscount(0);
        session.setAttribute("cart", cart);
    }

    private void updateCartTotals(CartRequestDTO cart) {
        cart.setTotalItem(cart.getProductList().stream()
            .mapToInt(CartItemRequestDTO::getQuantity)
            .sum());
            
        cart.setTotalPrice(cart.getProductList().stream()
            .mapToInt(item -> item.getPrice() * item.getQuantity())
            .sum());
    }
} 