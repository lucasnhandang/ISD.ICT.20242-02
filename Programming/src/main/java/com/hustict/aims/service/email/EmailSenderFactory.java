package com.hustict.aims.service.email;

import com.hustict.aims.dto.email.BaseEmailRequest;

import org.springframework.stereotype.Component;
import com.hustict.aims.dto.order.OrderDTO;

import java.util.Map;

@Component
public class EmailSenderFactory {

    private final Map<String, SendEmailService<? extends BaseEmailRequest>> emailServices;

    public EmailSenderFactory(Map<String, SendEmailService<? extends BaseEmailRequest>> emailServices) {
        this.emailServices = emailServices;
    }

  
    @SuppressWarnings("unchecked")
    public void process(String type, OrderDTO order) {

        SendEmailService<BaseEmailRequest> service =
                (SendEmailService<BaseEmailRequest>) emailServices.get(type);
        if (service == null) {
            throw new IllegalArgumentException("Unsupported email type: " + type);
        }

        BaseEmailRequest request = service.buildRequest(order);
        service.sendEmail(request,order);
    }
}