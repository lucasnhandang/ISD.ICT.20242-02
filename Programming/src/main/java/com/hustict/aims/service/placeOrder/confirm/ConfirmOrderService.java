package com.hustict.aims.service.placeOrder.confirm;

import com.hustict.aims.dto.order.ConfirmOrderRequestDTO;
import jakarta.servlet.http.HttpSession;

public interface ConfirmOrderService {
    void confirmOrder(HttpSession session, ConfirmOrderRequestDTO confirmOrderRequestDTO);
}
