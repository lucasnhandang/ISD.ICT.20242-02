package com.hustict.aims.service.email;

import com.hustict.aims.dto.order.OrderDTO;


public interface EmailSenderFactory {
    void process(String type, OrderDTO order);
}