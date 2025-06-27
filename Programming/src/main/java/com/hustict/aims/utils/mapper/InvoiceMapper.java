package com.hustict.aims.utils.mapper;

import com.hustict.aims.dto.invoice.InvoiceDTO;
import com.hustict.aims.model.invoice.Invoice;
import com.hustict.aims.model.payment.PaymentTransaction;
import com.hustict.aims.repository.PaymentTransactionRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class InvoiceMapper {
    @Autowired
    private PaymentTransactionRepository transactionRepository;
    
    @Autowired
    private PaymentTransactionMapper paymentTransactionMapper;
    

    public Invoice toEntity(InvoiceDTO dto) {
        if (dto == null) return null;
        Invoice entity = new Invoice();
        entity.setProductPriceExVAT(dto.getProductPriceExVAT());
        entity.setProductPriceIncVAT(dto.getProductPriceIncVAT());
        entity.setShippingFee(dto.getShippingFee());
        entity.setTotalAmount(dto.getTotalAmount());
        if (dto.getPaymentTransactionId() != null) {
        PaymentTransaction transaction = transactionRepository
            .findById(dto.getPaymentTransactionId())
            .orElseThrow(() -> new IllegalArgumentException(
                "Không tìm thấy PaymentTransaction với id=" + dto.getPaymentTransactionId()
            ));
        entity.setPaymentTransaction(transaction);
    }
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
