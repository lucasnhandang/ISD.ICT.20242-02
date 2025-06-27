package com.hustict.aims.dto.deliveryForm;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RushDeliveryInfoDTO {
    private LocalDateTime expectedDateTime;
    private String deliveryInstructions;
} 