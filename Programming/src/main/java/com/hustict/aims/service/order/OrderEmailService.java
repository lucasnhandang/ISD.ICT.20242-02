package com.hustict.aims.service.order;

import jakarta.servlet.http.HttpSession;

public interface OrderEmailService {
    void prepareOrderSessionForEmail(String type, Long orderId, HttpSession session);
} 