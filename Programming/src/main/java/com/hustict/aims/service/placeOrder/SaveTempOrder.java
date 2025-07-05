package com.hustict.aims.service.placeOrder;

import com.hustict.aims.dto.cart.CartRequestDTO;
import com.hustict.aims.dto.deliveryForm.DeliveryFormDTO;
import com.hustict.aims.dto.invoice.InvoiceDTO;

public interface SaveTempOrder {
    Long save(CartRequestDTO cart, DeliveryFormDTO deliveryFormDTO, InvoiceDTO invoiceDTO);
}
