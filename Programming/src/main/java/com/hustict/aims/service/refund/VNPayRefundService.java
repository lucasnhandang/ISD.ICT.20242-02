package com.hustict.aims.service.refund;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.hustict.aims.exception.OrderOperationException;
import com.hustict.aims.model.order.Order;
import com.hustict.aims.repository.OrderRepository;
import com.hustict.aims.dto.refund.RefundRequest;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;


@Service("VNPayRefundService")
public class VNPayRefundService implements RefundService {
    
    private final RestTemplate restTemplate;
    private final String vnPayApiUrl;
    private final String secretKey;
    @Autowired
    private OrderRepository orderRepository;

    private final CreateRefundRequestVNP createRefundRequestVNP; 

    public VNPayRefundService(@Value("${vnpay.payUrl}") String vnPayApiUrl,
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
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderOperationException("Order not found with id: " + orderId));
        String createBy = "system";
        String ipAddr = "127.0.0.1"; 
        RefundRequest refundRequest = createRefundRequestVNP.CreateRefundRequest(order, ipAddr, createBy);
        try {
            String response = restTemplate.postForObject(vnPayApiUrl, refundRequest, String.class);
            System.out.println("Response from VNPAY: " + response);
        } catch (Exception e) {
            throw new OrderOperationException("Error processing refund for order id: " + orderId, e);
        }
    }
}