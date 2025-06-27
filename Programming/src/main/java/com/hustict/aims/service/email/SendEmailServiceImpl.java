package com.hustict.aims.service.email;

import com.hustict.aims.dto.email.BaseEmailRequest;
import com.hustict.aims.dto.deliveryForm.DeliveryFormDTO;
import com.hustict.aims.dto.order.OrderInformationDTO;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

public abstract class SendEmailServiceImpl<T extends BaseEmailRequest> implements SendEmailService<T> {

    @Autowired
    protected HttpSession session;

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromAddress;

    @Override
    public void sendEmail(T request) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromAddress);
        message.setTo(request.getDeliveryInfor().getEmail());
        message.setSubject(buildSubject(request));
        message.setText(buildBody(request));
        mailSender.send(message);
    }

    
    protected void populateCommonFields(T req) {
        OrderInformationDTO info = (OrderInformationDTO) session.getAttribute("orderInformation");
        DeliveryFormDTO delivery = (DeliveryFormDTO) session.getAttribute("deliveryForm");
        req.setOrder(info);
        req.setDeliveryInfor(delivery);
    }

    
    protected abstract T instantiateRequest();

    protected abstract String buildSubject(T request);

    protected abstract String buildBody(T request);
}