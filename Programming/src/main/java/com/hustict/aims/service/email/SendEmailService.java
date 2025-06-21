package com.hustict.aims.service.email;

public interface SendEmailService {

    void send(String to, String subject, String body);
}