package com.hustict.aims.utils.mapper;

import com.hustict.aims.dto.cart.CartRequestDTO;
import com.hustict.aims.dto.cart.CartItemRequestDTO;
import com.hustict.aims.dto.deliveryForm.DeliveryFormDTO;
import com.hustict.aims.dto.invoice.InvoiceDTO;
import com.hustict.aims.dto.order.OrderInformationDTO;

import java.time.LocalDateTime;
import java.util.List;

public class OrderInformationDTOMapper {

    public static OrderInformationDTO toDTO(
            CartRequestDTO cartDTO,
            InvoiceDTO invoiceDTO,
            DeliveryFormDTO deliveryDTO,
            Long savedInvoiceId,
            Long savedDeliveryInfoId
    ) {
        OrderInformationDTO dto = new OrderInformationDTO();

        List<CartItemRequestDTO> items = cartDTO.getProductList();
        dto.setProductList(items);
        dto.setRushOrder(deliveryDTO.isRushOrder());
        dto.setCurrency(cartDTO.getCurrency());
        dto.setOrderDate(LocalDateTime.now());
        dto.setInvoiceId(savedInvoiceId);
        dto.setDeliveryInfoId(savedDeliveryInfoId);

        return dto;
    }
}
