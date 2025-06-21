package com.hustict.aims.service.placeOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hustict.aims.dto.cart.CartRequestDTO;
import com.hustict.aims.dto.deliveryForm.DeliveryFormDTO;
import com.hustict.aims.dto.invoice.InvoiceDTO;
import com.hustict.aims.service.email.SendEmailService;;
@Service
public class PaymentHandlerService {
    
    public void handlePaymentSuccess(){
    
    }
    // private final SendEmailServiceFactory sendEmailServiceFactory;

    // @Autowired
    // public PaymentHandlerService(SendEmailServiceFactory sendEmailServiceFactory) {
    //     this.sendEmailServiceFactory = sendEmailServiceFactory;
    // }

    // public void handlePaymentSuccess(DeliveryFormDTO deliveryForm, CartRequestDTO cart, InvoiceDTO invoice) {
    //     // Business logic xử lý đơn hàng thành công ở đây
    //     System.out.println("Payment was successful, order placed!");

    //     // Gửi email order thành công
    //     SendEmailService emailService = sendEmailServiceFactory.getService(EmailType.ORDER_SUCCESS);
    //     emailService.sendEmail(deliveryForm.getCustomerEmail()); // hoặc deliveryForm.getRecipient()
    // }
}