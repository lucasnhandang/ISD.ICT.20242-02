package com.hustict.aims.utils.mapper;

import com.hustict.aims.dto.invoice.InvoiceDTO;
import com.hustict.aims.model.invoice.Invoice;
import org.springframework.stereotype.Component;

@Component
public class InvoiceMapper {

    public Invoice toEntity(InvoiceDTO dto) {
        if (dto == null) return null;
        Invoice entity = new Invoice();
        entity.setProductPriceExVAT(dto.getProductPriceExVAT());
        entity.setProductPriceIncVAT(dto.getProductPriceIncVAT());
        entity.setShippingFee(dto.getShippingFee());
        entity.setTotalAmount(dto.getTotalAmount());
        return entity;
    }

    public InvoiceDTO toDTO(Invoice entity) {
        if (entity == null) return null;
        InvoiceDTO dto = new InvoiceDTO();
        dto.setId(entity.getId());
        dto.setProductPriceExVAT(entity.getProductPriceExVAT());
        dto.setProductPriceIncVAT(entity.getProductPriceIncVAT());
        dto.setShippingFee(entity.getShippingFee());
        dto.setTotalAmount(entity.getTotalAmount());
        return dto;
    }
}
