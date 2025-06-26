package com.hustict.aims.dto.email;

import com.hustict.aims.dto.deliveryForm.DeliveryFormDTO;
import com.hustict.aims.dto.order.OrderInformationDTO;
import lombok.Data;

@Data
public abstract class BaseEmailRequest implements EmailRequest {
    private OrderInformationDTO order;
    private DeliveryFormDTO deliveryInfor;
}
