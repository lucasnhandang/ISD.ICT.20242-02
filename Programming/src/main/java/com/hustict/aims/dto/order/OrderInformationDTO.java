package com.hustict.aims.dto.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

import com.hustict.aims.dto.cart.CartItemRequestDTO;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderInformationDTO {
    private Long orderId;
    private List<CartItemRequestDTO> productList;
    private boolean isRushOrder;
    private String currency;
    private Long deliveryInfoId;
    private Long invoiceId;
    private LocalDateTime orderDate;

}
