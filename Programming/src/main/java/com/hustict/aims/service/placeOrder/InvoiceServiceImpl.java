package com.hustict.aims.service.placeOrder;

import org.springframework.stereotype.Service;

import com.hustict.aims.dto.cart.CartRequestDTO;
import com.hustict.aims.dto.deliveryForm.DeliveryFormDTO;
import com.hustict.aims.dto.invoice.InvoiceDTO;
import com.hustict.aims.service.invoice.InvoiceCalculationService;
import com.hustict.aims.service.shippingFeeCalculator.ShippingFeeCalculator;
import org.springframework.beans.factory.annotation.Qualifier;

@Service
public class InvoiceServiceImpl implements InoviceService {
    
    private final InvoiceCalculationService invoiceCalculationService;
    private final ShippingFeeCalculator shippingFeeCalculator;

    public InvoiceServiceImpl(InvoiceCalculationService invoiceCalculationService, 
                                  @Qualifier("standardShippingFee") ShippingFeeCalculator shippingFeeCalculator) {
        this.invoiceCalculationService = invoiceCalculationService;
        this.shippingFeeCalculator = shippingFeeCalculator;
    }

    public InvoiceDTO createInvoice(DeliveryFormDTO deliveryForm, CartRequestDTO cart) {
        String province = deliveryForm.getDeliveryProvince();
        int totalPriceExVat = cart.getTotalPrice();

        int shippingFee = shippingFeeCalculator.calculateShippingFee(province, cart.getProductList(), totalPriceExVat);
        InvoiceDTO invoice = invoiceCalculationService.calculateInvoice(totalPriceExVat, shippingFee);

        return invoice;
    }
}