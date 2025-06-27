package com.hustict.aims.dto.order;

import com.hustict.aims.dto.cart.CartRequestDTO;
import com.hustict.aims.dto.deliveryForm.DeliveryFormDTO;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class RushOrderRequestDTO {
    private CartRequestDTO cart;
    private DeliveryFormDTO deliveryInfo;
    private boolean isRushOrder;
    private LocalDateTime expectedTime;
} 