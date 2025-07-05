package com.hustict.aims.service.shippingFeeCalculator;


import com.hustict.aims.dto.cart.CartItemRequestDTO;
import java.util.List;

public interface ShippingFeeCalculator {
    int calculateShippingFee(String province, List<CartItemRequestDTO> productList, int totalPrice);
}