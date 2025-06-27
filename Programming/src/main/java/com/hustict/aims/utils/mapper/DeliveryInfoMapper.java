package com.hustict.aims.utils.mapper;


import com.hustict.aims.dto.deliveryForm.DeliveryFormDTO;
import com.hustict.aims.model.shipping.DeliveryInfo;
import org.springframework.stereotype.Component;

@Component
public class DeliveryInfoMapper {
    public DeliveryInfo toEntity(DeliveryFormDTO dto) {
        if (dto == null) {
            return null;
        }
        DeliveryInfo entity = new DeliveryInfo();
        entity.setName(dto.getCustomerName());
        entity.setPhoneNumber(dto.getPhoneNumber());
        entity.setProvince(dto.getDeliveryProvince());
        entity.setAddress(dto.getDeliveryAddress());
        entity.setShippingInstruction(dto.getDeliveryInstructions());
        entity.setEmail(dto.getEmail());
        entity.setExpectedTime(dto.getExpectedDateTime());
        //entity.setExpectedDate(dto.getExpectedDate());
        //entity.setExpectedDateTime(dto.getExpectedDateTime());
        return entity;
    }

    public DeliveryFormDTO toDTO(DeliveryInfo entity) {
        if (entity == null) {
            return null;
        }
        DeliveryFormDTO dto = new DeliveryFormDTO();
        dto.setCustomerName(entity.getName());
        dto.setPhoneNumber(entity.getPhoneNumber());
        dto.setDeliveryProvince(entity.getProvince());
        dto.setDeliveryAddress(entity.getAddress());
        dto.setDeliveryInstructions(entity.getShippingInstruction());
        dto.setEmail(entity.getEmail());
        dto.setExpectedDateTime(entity.getExpectedTime());
        //dto.setExpectedDate(entity.get());
        //dto.setExpectedDateTime(entity.getExpectedDateTime());
        return dto;
    }
}
