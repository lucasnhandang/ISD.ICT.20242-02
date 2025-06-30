package com.hustict.aims.service.email;
 
import com.hustict.aims.model.order.Order;
import com.hustict.aims.model.invoice.Invoice;
import com.hustict.aims.dto.order.OrderDTO;
import com.hustict.aims.repository.OrderRepository;
import com.hustict.aims.repository.InvoiceRepository;
import com.hustict.aims.repository.DeliveryInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OrderInfoService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private DeliveryInfoRepository deliveryInfoRepository;

    public OrderDTO getOrderDTOByOrderId(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found with ID: " + orderId));

        Long invoiceId = order.getInvoice().getId();
        Long deliveryId = order.getDeliveryInfo().getId();

        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new IllegalArgumentException("Invoice not found with ID: " + invoiceId));
        
      
        return new OrderDTO(
                order.getId(),
                invoiceId,
                deliveryId,
                invoice.getPaymentTransaction().getId()
        );
    }
}
