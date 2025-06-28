package com.hustict.aims.service.email;

import com.hustict.aims.dto.email.BaseEmailRequest;
import com.hustict.aims.dto.invoice.InvoiceDTO;
import com.hustict.aims.dto.deliveryForm.DeliveryFormDTO;
import com.hustict.aims.dto.order.OrderInformationDTO;
import com.hustict.aims.dto.payment.PaymentTransactionDTO;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

public abstract class SendEmailServiceImpl<T extends BaseEmailRequest> implements SendEmailService<T> {

    @Autowired
    protected HttpSession session;

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromAddress;

    @Override
    public void sendEmail(T request) {
    //     SimpleMailMessage message = new SimpleMailMessage();
    //     message.setFrom(fromAddress);
    //     message.setTo(request.getDeliveryInfor().getEmail());
    //     message.setSubject(buildSubject(request));
    //     message.setText(buildBody(request));
    //     mailSender.send(message);
    // }
         try {
            // Tạo một MimeMessage để gửi email
            MimeMessage message = mailSender.createMimeMessage();
            
            // Tạo MimeMessageHelper để cấu hình email
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromAddress);
            helper.setTo(request.getDeliveryInfor().getEmail());
            helper.setSubject(buildSubject(request));
            helper.setText(buildBody(request), true);  // Đặt `true` để gửi email với body HTML

            // Gửi email
            mailSender.send(message);

        } catch (MessagingException e) {
            e.printStackTrace();
            throw new RuntimeException("Error sending email: " + e.getMessage());
        }
    }
    
    protected void populateCommonFields(T req) {
        OrderInformationDTO info = (OrderInformationDTO) session.getAttribute("orderInformation");
        DeliveryFormDTO delivery = (DeliveryFormDTO) session.getAttribute("deliveryForm");
        InvoiceDTO invoice = (InvoiceDTO) session.getAttribute("invoice");
        PaymentTransactionDTO payment = (PaymentTransactionDTO) session.getAttribute("paymentTransaction");
        req.setOrder(info);
        req.setDeliveryInfor(delivery);
        req.setInvoice(invoice);
        req.setPayment(payment);
    }

    
    protected abstract T instantiateRequest();

    protected abstract String buildSubject(T request);

    protected abstract String buildBody(T request);
}