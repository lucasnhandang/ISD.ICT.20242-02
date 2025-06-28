package com.hustict.aims.dto.order;

import lombok.Data;

@Data
public class OrderItemDTO {
    private Long productId;
    private String productName;
    private int quantity;
    private int price;
}
