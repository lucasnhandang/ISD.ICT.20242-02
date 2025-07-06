package com.hustict.aims.service.order;
import jakarta.servlet.http.HttpSession;

import com.hustict.aims.dto.order.OrderInformationDTO;
import java.util.List;

public interface OrderService {
    List<OrderInformationDTO> getPendingOrders(int page, int size);
    long getTotalPendingOrders();
    void prepareOrderSessionForEmail(String type,Long orderId, HttpSession session);
    void cancelOrder(Long orderId);
    OrderInformationDTO getOrderDetails(Long orderId);
}

