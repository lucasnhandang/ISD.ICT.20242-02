package com.hustict.aims.service.placeOrder.request;

import com.hustict.aims.dto.cart.CartRequestDTO;

public interface ProductAvailabilityService {
    void checkCartAvailability(CartRequestDTO cart);
}
