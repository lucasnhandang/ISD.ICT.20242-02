package com.hustict.aims.service.email;

import com.hustict.aims.dto.email.BaseEmailRequest;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class EmailSenderFactory {

    private final Map<String, SendEmailService<? extends BaseEmailRequest>> emailServices;

    public EmailSenderFactory(Map<String, SendEmailService<? extends BaseEmailRequest>> emailServices) {
        this.emailServices = emailServices;
    }

    /**
     * Look up the email service by key, build its request, then send the email.
     *
     * @param type    identifier of the email service to use
     * @param session HTTP session holding order and delivery data
     */
    @SuppressWarnings("unchecked")
    public void process(String type, HttpSession session) {
        SendEmailService<BaseEmailRequest> service =
                (SendEmailService<BaseEmailRequest>) emailServices.get(type);
        if (service == null) {
            throw new IllegalArgumentException("Unsupported email type: " + type);
        }

        BaseEmailRequest request = service.buildRequest(session);
        service.sendEmail(request);
    }
}