package com.hustict.aims.service.order;

import com.hustict.aims.dto.order.OrderInformationDTO;
import jakarta.servlet.http.HttpSession;
import java.util.List;

public interface OrderService {
    void approveOrder(Long orderId);
    void rejectOrder(Long orderId);
    List<OrderInformationDTO> getPendingOrders();
    void prepareOrderSessionForEmail(Long orderId, HttpSession session); 
} 