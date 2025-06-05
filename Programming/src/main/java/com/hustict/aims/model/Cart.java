package com.hustict.aims.model;

import java.util.HashMap;
import java.util.Map;

public class Cart {
    private Map<Product, Long> productList = new HashMap<>();
    private static Cart instance;

    public Cart() {}

    public static Cart getInstance() {
        if (instance == null) {
            instance = new Cart();
        }
        return instance;
    }

    public void emptyCart() {
        productList.clear();
    }

    public int calculateTotalItem(Map<Product, Long> productList) {
        int total = 0;
        for (Long quantity : productList.values()) {
            total += quantity;
        }
        return total;
    }

    public int calculateTotalPrice(Map<Product, Long> productList) {
        int total = 0;
        for (Map.Entry<Product, Long> entry : productList.entrySet()) {
            total += entry.getKey().getPrice() * entry.getValue();
        }
        return total;
    }

    public void addProduct(Product product, int quantity) {
        productList.put(product, productList.getOrDefault(product, 0L) + quantity);
    }

    public void removeProduct(Product product, int quantity) {
        if (productList.containsKey(product)) {
            long current = productList.get(product) - quantity;
            if (current > 0) {
                productList.put(product, current);
            } else {
                productList.remove(product);
            }
        }
    }

    public Map<Product, Long> getProduct() {
        return productList;
    }
}
