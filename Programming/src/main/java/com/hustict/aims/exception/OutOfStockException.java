package com.hustict.aims.exception;

import java.util.List;

public class OutOfStockException extends RuntimeException {
    private final List<OutOfStockItem> items;

    public OutOfStockException(List<OutOfStockItem> items) {
        super("Some items are out of stock.");
        this.items = items;
    }

    public List<OutOfStockItem> getItems() {
        return items;
    }

    public static class OutOfStockItem {
        private Long productId;
        private String productName;
        private int requestedQuantity;
        private int availableQuantity;

        public OutOfStockItem(Long productId, String productName, int requestedQuantity, int availableQuantity) {
            this.productId = productId;
            this.productName = productName;
            this.requestedQuantity = requestedQuantity;
            this.availableQuantity = availableQuantity;
        }

    }
}
