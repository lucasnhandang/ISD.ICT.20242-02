package com.hustict.aims.service.invoice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hustict.aims.dto.invoice.InvoiceDTO;
import com.hustict.aims.model.invoice.Invoice;
import com.hustict.aims.utils.mapper.InvoiceMapper;

@Service
public class InvoiceCalculationServiceImpl implements InvoiceCalculationService {

    @Autowired
    private InvoiceMapper invoiceMapper;

    @Override
    public InvoiceDTO calculateInvoice(int productPriceExVAT, int shippingFee) {
        int vatAmount = (int) (productPriceExVAT * 0.1);
        int productPriceIncVAT = productPriceExVAT + vatAmount;
        int totalAmount = productPriceIncVAT + shippingFee;

        // Debug log
        System.out.println("🔍 Debug InvoiceCalculationService - Input productPriceExVAT: " + productPriceExVAT);
        System.out.println("🔍 Debug InvoiceCalculationService - Input shippingFee: " + shippingFee);
        System.out.println("🔍 Debug InvoiceCalculationService - Calculated vatAmount: " + vatAmount);
        System.out.println("🔍 Debug InvoiceCalculationService - Calculated productPriceIncVAT: " + productPriceIncVAT);
        System.out.println("🔍 Debug InvoiceCalculationService - Calculated totalAmount: " + totalAmount);

        Invoice invoice = new Invoice();
        invoice.setProductPriceExVAT(productPriceExVAT);
        invoice.setProductPriceIncVAT(productPriceIncVAT);
        invoice.setShippingFee(shippingFee);
        invoice.setTotalAmount(totalAmount);

        return invoiceMapper.toDTO(invoice);
    }
}
