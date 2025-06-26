package com.hustict.aims.dto.order;

import com.hustict.aims.dto.invoice.InvoiceDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RushOrderResponseDTO {
    private InvoiceDTO invoice;
    private String status;
    private String message;
} 