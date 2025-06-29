package com.hustict.aims.service.placeOrder;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;

import com.hustict.aims.dto.cart.CartRequestDTO;
import com.hustict.aims.dto.deliveryForm.DeliveryFormDTO;
import com.hustict.aims.dto.invoice.InvoiceDTO;
import com.hustict.aims.model.cart.Cart;
import com.hustict.aims.model.order.Order;
import com.hustict.aims.model.invoice.Invoice;
import com.hustict.aims.model.order.OrderStatus;
import com.hustict.aims.model.shipping.DeliveryInfo;

import com.hustict.aims.repository.InvoiceRepository;
import com.hustict.aims.repository.OrderRepository;
import com.hustict.aims.repository.DeliveryInfoRepository;

import com.hustict.aims.utils.mapper.InvoiceMapper;
import com.hustict.aims.utils.mapper.DeliveryInfoMapper;
import org.springframework.stereotype.Service;

@Service
public class SaveTempOrder {
    
    @Autowired
    private DeliveryInfoRepository deliveryFormRepository;

    @Autowired
    private DeliveryInfoMapper DeliveryFormMapper;

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private InvoiceMapper invoiceMapper;

    @Autowired
    private OrderRepository orderRepository;


    public Long save(CartRequestDTO cart, DeliveryFormDTO deliverformDTO, InvoiceDTO invoiceDTO){
        DeliveryInfo deliveryForm = DeliveryFormMapper.toEntity(deliverformDTO);
        Invoice invoice = invoiceMapper.toEntity(invoiceDTO);

        deliveryForm = deliveryFormRepository.save(deliveryForm); 
        invoice = invoiceRepository.save(invoice);
        
        Order order = new Order();
        order.setOrderDate(LocalDateTime.now()); 
        order.setOrderStatus(OrderStatus.PENDING); 
        order.setIsRushOrder(cart.isRushOrder());
        order.setCurrency(cart.getCurrency()); 
        order.setDeliveryInfo(deliveryForm); 
        order.setInvoice(invoice); 
        
        order = orderRepository.save(order);
        return order.getId();
    }

}
