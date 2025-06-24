package com.hustict.aims.service.invoice;
import org.springframework.stereotype.Service;

import com.hustict.aims.dto.invoice.InvoiceDTO;

@Service
public class InvoiceCalculationServiceImpl implements InvoiceCalculationService {

    @Override
    public InvoiceDTO calculateInvoice(int totalPriceExVat, int shippingFee) {
        int vatAmount = (int) (totalPriceExVat * 0.1); 
        int totalPriceIncludingVat = totalPriceExVat + vatAmount;
        int totalAmount = totalPriceIncludingVat + shippingFee;
        return new InvoiceDTO(totalPriceExVat, totalPriceIncludingVat, shippingFee, totalAmount);
    }
}
