package com.hustict.aims.model;

import java.util.ArrayList;
import java.util.List;

public class Cart {
    private List<CartItem> cartItems = new ArrayList<>();
    private static Cart instance;

    public Cart() {}

    public static Cart getCart() {
        if (instance == null) {
            instance = new Cart();
        }
        return instance;
    }

    public void emptyCart() {
        cartItems.clear();
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public void addCartItem(CartItem item) {
        cartItems.add(item);
    }

    public void removeCartItem(CartItem item) {
        cartItems.remove(item);
    }

    public int getTotalProduct() {
        int total = 0;
        for (CartItem item : cartItems) {
            total += item.getQuantity();
        }
        return total;
    }

    public CartItem checkProductInCart(CartItem item) {
        for (CartItem ci : cartItems) {
            if (ci.getProduct().equals(item.getProduct())) {
                return ci;
            }
        }
        return null;
    }
}
