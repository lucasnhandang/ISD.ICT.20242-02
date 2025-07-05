package com.hustict.aims.service.email;

import com.hustict.aims.dto.email.BaseEmailRequest;
import com.hustict.aims.dto.invoice.InvoiceDTO;
import com.hustict.aims.dto.deliveryForm.DeliveryFormDTO;
import com.hustict.aims.dto.order.OrderInformationDTO;
import com.hustict.aims.dto.payment.PaymentTransactionDTO;
import com.hustict.aims.model.invoice.Invoice;
import com.hustict.aims.model.order.Order;
import com.hustict.aims.model.shipping.DeliveryInfo;


import com.hustict.aims.model.payment.PaymentTransaction;
import com.hustict.aims.repository.InvoiceRepository;
import com.hustict.aims.repository.OrderRepository;
import com.hustict.aims.repository.DeliveryInfoRepository;
import com.hustict.aims.repository.PaymentTransactionRepository;

import com.hustict.aims.utils.mapper.OrderMapper;
import com.hustict.aims.utils.mapper.PaymentTransactionMapper;
import com.hustict.aims.utils.mapper.DeliveryInfoMapper;
import com.hustict.aims.utils.mapper.InvoiceMapper;


import com.hustict.aims.dto.order.OrderDTO;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

public abstract class SendEmailServiceImpl<T extends BaseEmailRequest> implements SendEmailService<T> {

    @Autowired
    protected HttpSession session;

    @Autowired
    private JavaMailSender mailSender;
    
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private DeliveryInfoRepository deliveryRepository;

    @Autowired
    private PaymentTransactionRepository paymentRepository;

    @Autowired
    private OrderMapper orderMapper ;

    @Autowired
    private PaymentTransactionMapper paymentTransactionMapper;
    @Autowired
    private DeliveryInfoMapper deliveryInfoMapper;
    @Autowired
    private InvoiceMapper invoiceMapper;
    


    @Value("${spring.mail.username}")
    private String fromAddress;

    @Override
    public void sendEmail(T request, OrderDTO order) {
        int maxRetries = 3;
        int attempt = 0;
        long delayBetweenRetriesMs = 2000;

        while (attempt < maxRetries) {
            try {
                MimeMessage message = mailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

                helper.setFrom(fromAddress);
                helper.setTo(request.getDeliveryInfor().getEmail());
                helper.setSubject(buildSubject(request, order.getOrderId()));
                helper.setText(buildBody(request), true); // HTML enabled

                mailSender.send(message);
                return; // success

            } catch (MessagingException e) {
                attempt++;
                System.err.println("Failed to send email (attempt " + attempt + "): " + e.getMessage());
                if (attempt >= maxRetries) {
                    throw new RuntimeException("Error sending email after " + maxRetries + " attempts: " + e.getMessage(), e);
                }
                try {
                    Thread.sleep(delayBetweenRetriesMs);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException("Retry interrupted", ie);
                }
            }
        }
    }
    
    protected void populateCommonFields(T req, OrderDTO order) {
   
        Order orderEntity = orderRepository.findById(order.getOrderId())
                .orElseThrow(() -> new RuntimeException("Order not found"));

        Invoice invoice = invoiceRepository.findById(order.getInvoiceId())
                .orElseThrow(() -> new RuntimeException("Invoice not found"));

        DeliveryInfo deliveryForm = deliveryRepository.findById(order.getDeliveryId())
                .orElseThrow(() -> new RuntimeException("Delivery information not found"));

        PaymentTransaction paymentTransaction = paymentRepository.findById(order.getTransactionId())
                .orElseThrow(() -> new RuntimeException("Payment transaction not found"));

        // Sử dụng Mapper để chuyển các đối tượng thành DTO
        OrderInformationDTO orderInformationDTO = orderMapper.toDTO(orderEntity);
        DeliveryFormDTO deliveryFormDTO = deliveryInfoMapper.toDTO(deliveryForm);
        InvoiceDTO invoiceDTO = invoiceMapper.toDTO(invoice);
        PaymentTransactionDTO paymentTransactionDTO = paymentTransactionMapper.toDTO(paymentTransaction);

        if (orderInformationDTO.getProductList() == null ){
            
        }

        req.setOrder(orderInformationDTO);
        req.setDeliveryInfor(deliveryFormDTO);
        req.setInvoice(invoiceDTO);
        req.setPayment(paymentTransactionDTO);
    }

    
    protected abstract T instantiateRequest();

    protected abstract String buildSubject(T request,Long id);

    protected abstract String buildBody(T request);
}