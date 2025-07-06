package com.hustict.aims.service.placeOrder;

import com.hustict.aims.dto.cart.CartRequestDTO;
import com.hustict.aims.dto.deliveryForm.DeliveryFormDTO;
import com.hustict.aims.dto.invoice.InvoiceDTO;

public interface InoviceService {
    InvoiceDTO createInvoice(DeliveryFormDTO deliveryForm, CartRequestDTO cart);
}
