package com.hustict.aims.service.refund;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.hustict.aims.exception.OrderOperationException;
import com.hustict.aims.model.order.Order;
import com.hustict.aims.repository.OrderRepository;
import com.hustict.aims.dto.refund.RefundRequest;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
import org.springframework.http.HttpHeaders;

@Service("VNPay")
public class VNPayRefundService implements RefundService {
    
    private final RestTemplate restTemplate;
    private final String vnPayApiUrl;
    private final String secretKey;
    @Autowired
    private OrderRepository orderRepository;

    private final CreateRefundRequestVNP createRefundRequestVNP; 

    public VNPayRefundService(@Value("${vnpayrefund}") String vnPayApiUrl,
                              @Value("${vnpay.hashSecret}") String secretKey,
                              RestTemplate restTemplate,
                              CreateRefundRequestVNP createRefundRequestVNP) {
        this.vnPayApiUrl = vnPayApiUrl;
        this.secretKey = secretKey;
        this.restTemplate = restTemplate;  // Inject RestTemplate
        this.createRefundRequestVNP = createRefundRequestVNP; // Autowire CreateRefundRequestVNP
    }

    @Override
    public void processRefund(Long orderId) {
        System.out.println("VNPay announce: Start refund for order: " + orderId );
        
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderOperationException("Order not found with id: " + orderId));
        String createBy = "system";
        String ipAddr = "127.0.0.1"; 
        RefundRequest refundRequest = createRefundRequestVNP.CreateRefundRequest(order, ipAddr, createBy);
  
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);  // Đặt Content-Type

            HttpEntity<RefundRequest> entity = new HttpEntity<>(refundRequest, headers);
            System.out.println("Type of refundRequest: " + entity.getClass().getName());

            ResponseEntity<String> response = restTemplate.exchange(
                vnPayApiUrl,
                HttpMethod.POST,
                entity,
                String.class
            );

            System.out.println("Response from VNPAY: " + response.getBody());
            System.out.println("Response from VNPAY: " + response);
        } catch (Exception e) {
            throw new OrderOperationException("Error processing refund for order id: " + orderId, e);
        }
    }
}