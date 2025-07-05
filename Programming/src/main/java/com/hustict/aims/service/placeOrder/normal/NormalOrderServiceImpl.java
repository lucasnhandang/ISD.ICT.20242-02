package com.hustict.aims.service.placeOrder.normal;
import org.springframework.stereotype.Service;

import com.hustict.aims.service.placeOrder.InoviceService;
import com.hustict.aims.service.placeOrder.SaveTempOrder;

import com.hustict.aims.dto.deliveryForm.DeliveryFormDTO;
import com.hustict.aims.dto.invoice.InvoiceDTO;
import com.hustict.aims.dto.NormalOrderResult;
import com.hustict.aims.dto.cart.CartRequestDTO;

@Service
public class NormalOrderServiceImpl implements NormalOrderService {

    private final InoviceService inoviceService;
    private final SaveTempOrder saveTempOrder;

    public NormalOrderServiceImpl(InoviceService inoviceService, SaveTempOrder saveTempOrder) {
        this.inoviceService = inoviceService;
        this.saveTempOrder = saveTempOrder;
    }

    @Override
    public NormalOrderResult processNormalOrder(CartRequestDTO cart, DeliveryFormDTO deliveryForm) {
        InvoiceDTO invoice = inoviceService.createInvoice(deliveryForm, cart);
        invoice.setRushOrder(false);
        cart.setRushOrder(false);

        Long orderId = saveTempOrder.save(cart, deliveryForm, invoice);

        return new NormalOrderResult(orderId, invoice);
    }
}
