package com.hustict.aims.service.placeRushOrder;

import com.hustict.aims.dto.cart.CartItemRequestDTO;
import com.hustict.aims.dto.cart.CartRequestDTO;
import com.hustict.aims.model.product.Product;
import com.hustict.aims.repository.product.ProductRepository;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class CartSplitter {
    public Pair<List<CartItemRequestDTO>, List<CartItemRequestDTO>> splitRushAndNormal(CartRequestDTO cart, ProductRepository productRepository) {
        List<CartItemRequestDTO> rushItems = new ArrayList<>();
        List<CartItemRequestDTO> normalItems = new ArrayList<>();
        for (CartItemRequestDTO item : cart.getProductList()) {
            Product product = productRepository.findById(item.getProductID()).orElse(null);
            if (product != null && Boolean.TRUE.equals(product.getRushOrderSupported())) {
                rushItems.add(item);
            } else {
                normalItems.add(item);
            }
        }
        return new Pair<>(rushItems, normalItems);
    }
    public static class Pair<A, B> {
        public final A first;
        public final B second;
        public Pair(A first, B second) {
            this.first = first;
            this.second = second;
        }
    }
} 