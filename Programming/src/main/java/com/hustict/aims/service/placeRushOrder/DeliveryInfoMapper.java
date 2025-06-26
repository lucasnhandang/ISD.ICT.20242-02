package com.hustict.aims.service.placeRushOrder;

import com.hustict.aims.dto.deliveryForm.DeliveryFormDTO;
import com.hustict.aims.model.shipping.DeliveryInfo;
import org.springframework.stereotype.Service;

@Service
public class DeliveryInfoMapper {
    public DeliveryInfo mapFromDTO(DeliveryFormDTO dto) {
        DeliveryInfo entity = new DeliveryInfo();
        entity.setAddress(dto.getDeliveryAddress());
        entity.setShippingInstruction(dto.getDeliveryInstructions());
        entity.setName(dto.getCustomerName());
        entity.setPhoneNumber(dto.getPhoneNumber());
        entity.setEmail(dto.getEmail());
        entity.setProvince(dto.getDeliveryProvince());
        return entity;
    }
} 