package com.hustict.aims.dto.order;

import com.hustict.aims.dto.invoice.InvoiceDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RushOrderResponseDTO {
    private List<InvoiceDTO> invoiceList;
    private List<com.hustict.aims.dto.cart.CartRequestDTO> cartList;
    private String status;
    private String message;
    private LocalDateTime expectedTime;
} 