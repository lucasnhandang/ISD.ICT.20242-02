package com.hustict.aims.service.reservation;

import com.hustict.aims.dto.cart.CartRequestDTO;
import jakarta.servlet.http.HttpSession;

public interface ReservationService {
    void createReservation(CartRequestDTO cart, String sessionId);
    void releaseReservation(HttpSession session);
    void confirmReservation(String sessionId, Long orderId);
}
