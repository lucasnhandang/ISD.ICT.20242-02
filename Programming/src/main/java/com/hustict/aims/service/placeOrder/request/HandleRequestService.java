package com.hustict.aims.service.placeOrder.request;

import com.hustict.aims.dto.cart.CartRequestDTO;

public interface HandleRequestService {
    void requestToPlaceOrder(CartRequestDTO cart, String sessionId);
}