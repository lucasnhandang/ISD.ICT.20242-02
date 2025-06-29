package com.hustict.aims.dto.email;

import com.hustict.aims.dto.deliveryForm.DeliveryFormDTO;
import com.hustict.aims.dto.invoice.InvoiceDTO;
import com.hustict.aims.dto.order.OrderInformationDTO;
import com.hustict.aims.dto.payment.PaymentTransactionDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class CancelOrderEmailRequest extends BaseEmailRequest {
    private OrderInformationDTO order;
    private DeliveryFormDTO deliveryInfor;
    private InvoiceDTO invoice;
    private PaymentTransactionDTO payment;
}