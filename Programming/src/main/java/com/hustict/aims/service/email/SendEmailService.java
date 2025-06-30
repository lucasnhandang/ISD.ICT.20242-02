package com.hustict.aims.service.email;

import com.hustict.aims.dto.email.BaseEmailRequest;
import com.hustict.aims.dto.order.OrderDTO;


public interface SendEmailService<T extends BaseEmailRequest> {
    T buildRequest(OrderDTO order);
    void sendEmail(T request,OrderDTO order);
}