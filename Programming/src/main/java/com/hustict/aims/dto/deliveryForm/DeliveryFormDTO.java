package com.hustict.aims.dto.deliveryForm;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryFormDTO {
    private String customerName;
    private String phoneNumber;
    private String deliveryProvince;
    private String deliveryAddress;
    private String deliveryInstructions;
    private boolean isRushOrder;
    private String email;  
    private String expectedDate;
    private LocalDateTime expectedDateTime;
}