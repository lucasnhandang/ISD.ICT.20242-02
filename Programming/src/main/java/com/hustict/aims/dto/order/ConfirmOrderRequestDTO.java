package com.hustict.aims.dto.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConfirmOrderRequestDTO {
    private Long orderId;
    private boolean success;

}
