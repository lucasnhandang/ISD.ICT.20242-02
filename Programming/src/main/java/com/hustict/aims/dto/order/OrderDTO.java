package com.hustict.aims.dto.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {
    private Long orderId;
    private Long invoiceId;
    private Long deliveryId;
    private Long transactionId;
}
