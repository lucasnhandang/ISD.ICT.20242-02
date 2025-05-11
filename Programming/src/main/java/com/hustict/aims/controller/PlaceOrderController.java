package com.hustict.aims.controller;

import com.hustict.aims.model.*;
import com.hustict.aims.service.*;
import com.hustict.aims.boundary.*;
/*
The RejectOrderController has functional cohesion.
All methods contribute to executing the place order process.
Its only reason to change would be if the process of placing order changes, including add new steps, removal of old step,...
 */
public class PlaceOrderController {

    private final ProductAvailabilityService availabilityService;
    private final DeliveryFormValidator deliveryFormValidator;
    private final ShippingFeeCalculator shippingFeeCalculator;
    private final InvoiceService invoiceService;
    private final OrderService orderService;
    private final SuccessEmailService successEmailService;
    private final DeliveryFormScreen deliveryFormScreen;
    private final InvoiceScreen invoiceScreen;
    private final OrderInfoScreen orderInfoScreen;

    public PlaceOrderController(ProductAvailabilityService availabilityService,
                                DeliveryFormValidator deliveryFormValidator,
                                ShippingFeeCalculator shippingFeeCalculator,
                                InvoiceService invoiceService,
                                OrderService orderService,
                                SuccessEmailService successEmailService,
                                DeliveryFormScreen deliveryFormScreen,
                                InvoiceScreen invoiceScreen,
                                OrderInfoScreen orderInfoScreen) {
        this.availabilityService = availabilityService;
        this.deliveryFormValidator = deliveryFormValidator;
        this.shippingFeeCalculator = shippingFeeCalculator;
        this.invoiceService = invoiceService;
        this.orderService = orderService;
        this.successEmailService = successEmailService;
        this.deliveryFormScreen = deliveryFormScreen;
        this.invoiceScreen = invoiceScreen;
        this.orderInfoScreen = orderInfoScreen;
    }

    public void requestToPlaceOrder(Cart cart) {
        availabilityService.checkCartAvailability(cart);
        Order order = orderService.createOrderFromCart(cart);
        //them createOrderFromCart
        deliveryFormScreen.promptUserInput(order);
    }

    public void submitDeliveryForm(Order order, DeliveryForm form) {
        deliveryFormValidator.validate(form);
        DeliveryInformation deliveryInfo = orderService.createDeliveryInfo(form);
        int shippingFee = shippingFeeCalculator.calculate(deliveryInfo);
        Invoice invoice = invoiceService.createInvoice(order, deliveryInfo, shippingFee);

        orderService.attachDeliveryAndInvoice(order, deliveryInfo, invoice);
        invoiceScreen.display(invoice);
    }

    public void handlePaymentSuccess(Order order, TransactionInfo transaction) {
        orderService.saveTransaction(order, transaction);
        successEmailService.sendConfirmation(order);
        orderInfoScreen.display(order);
    } 
}
