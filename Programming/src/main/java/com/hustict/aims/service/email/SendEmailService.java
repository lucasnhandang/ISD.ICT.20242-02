package com.hustict.aims.service.email;

import com.hustict.aims.dto.email.BaseEmailRequest;
import jakarta.servlet.http.HttpSession;

public interface SendEmailService<T extends BaseEmailRequest> {
    T buildRequest(HttpSession session);
    void sendEmail(T request);
}