package com.hustict.aims.service;

import com.hustict.aims.dto.order.RushOrderRequestDTO;
import com.hustict.aims.dto.order.RushOrderResponseDTO;

public interface RushOrderService {
    RushOrderResponseDTO processRushOrder(RushOrderRequestDTO request);
} 