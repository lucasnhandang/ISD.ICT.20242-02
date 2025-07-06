package com.hustict.aims.service.order;

import com.hustict.aims.dto.order.OrderInformationDTO;
import java.util.List;

public interface OrderQueryService {
    List<OrderInformationDTO> getPendingOrders(int page, int size);
    long getTotalPendingOrders();
    OrderInformationDTO getOrderDetails(Long orderId);
} 