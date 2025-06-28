package com.hustict.aims.utils.mapper;

import com.hustict.aims.dto.payment.PaymentTransactionDTO;
import com.hustict.aims.model.payment.PaymentTransaction;
import org.springframework.stereotype.Component;

@Component
public class PaymentTransactionMapper {

    /**
     * Chuyển từ DTO -> Entity
     */
    public PaymentTransaction toEntity(PaymentTransactionDTO dto) {
        if (dto == null) {
            return null;
        }
        PaymentTransaction entity = new PaymentTransaction();
        entity.setBankTransactionId(dto.getBankTransactionId());
        entity.setContent(dto.getContent());
        entity.setPaymentTime(dto.getPaymentTime());
        entity.setPaymentAmount(dto.getPaymentAmount());
        entity.setCardType(dto.getCardType());
        entity.setCurrency(dto.getCurrency());
        entity.setSystems(dto.getSystems());
        return entity;
    }

    /**
     * Chuyển từ Entity -> DTO
     */
    public PaymentTransactionDTO toDTO(PaymentTransaction entity) {
        if (entity == null) {
            return null;
        }
        PaymentTransactionDTO dto = new PaymentTransactionDTO();
        dto.setBankTransactionId(entity.getBankTransactionId());
        dto.setContent(entity.getContent());
        dto.setPaymentTime(entity.getPaymentTime());
        dto.setPaymentAmount(entity.getPaymentAmount());
        dto.setCardType(entity.getCardType());
        dto.setCurrency(entity.getCurrency());
        dto.setSystems(entity.getSystems());
        return dto;
    }
}
