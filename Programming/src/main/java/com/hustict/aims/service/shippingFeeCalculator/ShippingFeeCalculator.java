package com.hustict.aims.service.shippingFeeCalculator;

import com.hustict.aims.dto.cart.CartRequestDTO;
import com.hustict.aims.dto.deliveryForm.DeliveryFormDTO;

public interface ShippingFeeCalculator {
    int calculateShippingFee(DeliveryFormDTO deliveryForm,CartRequestDTO cart);
}