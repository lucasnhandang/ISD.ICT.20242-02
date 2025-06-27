package com.hustict.aims.dto.order;

import com.hustict.aims.dto.cart.CartItemRequestDTO;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RushOrderEligibilityResponseDTO {
    private boolean eligible;
    private List<CartItemRequestDTO> rushItems;
    private List<CartItemRequestDTO> normalItems;
    private String message;
    private String errorCode;
} 