package com.hustict.aims.service.placeOrder;

import org.springframework.stereotype.Service;

import com.hustict.aims.dto.cart.CartRequestDTO;
import com.hustict.aims.dto.deliveryForm.DeliveryFormDTO;
import com.hustict.aims.dto.invoice.InvoiceDTO;
import com.hustict.aims.service.invoice.InvoiceCalculationService;
import com.hustict.aims.service.shippingFeeCalculator.ShippingFeeCalculator;
import org.springframework.beans.factory.annotation.Qualifier;

@Service
public class NormalOrderServiceImpl implements NormalOrderService {
    
    private final InvoiceCalculationService invoiceCalculationService;
    private final ShippingFeeCalculator shippingFeeCalculator;

    public NormalOrderServiceImpl(InvoiceCalculationService invoiceCalculationService, 
                                  @Qualifier("standardShippingFee") ShippingFeeCalculator shippingFeeCalculator) {
        this.invoiceCalculationService = invoiceCalculationService;
        this.shippingFeeCalculator = shippingFeeCalculator;
    }

    public InvoiceDTO handleNormalOrder(DeliveryFormDTO deliveryForm, CartRequestDTO cart) {
        int shippingFee = shippingFeeCalculator.calculateShippingFee(deliveryForm, cart);
        int totalPriceExVat = cart.getTotalPrice();
        InvoiceDTO invoice = invoiceCalculationService.calculateInvoice(totalPriceExVat, shippingFee);

        // System.out.println("Shipping Fee: " + shippingFee);
        // System.out.println("Total Price Excluding VAT: " + totalPriceExVat);
        // System.out.println("Total Price Including VAT: " + invoice.getProductPriceIncVAT());
        // System.out.println("Total Amount (with shipping fee): " + invoice.getTotalAmount());

        return invoice;
    }
}