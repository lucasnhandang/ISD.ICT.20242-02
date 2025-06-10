package com.hustict.aims.dto.deliveryForm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryFormDTO {
    private String customerName;
    private int phoneNumber;
    private String deliveryProvince;
    private String deliveryAddress;
    private String deliveryInstructions;
    private boolean isRushOrder;
}