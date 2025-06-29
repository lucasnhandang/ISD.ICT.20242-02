package com.hustict.aims.service.placeOrder;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import jakarta.servlet.http.HttpSession;

import com.hustict.aims.dto.cart.CartItemRequestDTO;
import com.hustict.aims.dto.cart.CartRequestDTO;
import com.hustict.aims.dto.deliveryForm.DeliveryFormDTO;
import com.hustict.aims.dto.invoice.InvoiceDTO;
import com.hustict.aims.model.cart.Cart;
import com.hustict.aims.model.order.Order;
import com.hustict.aims.model.product.Product;

import com.hustict.aims.model.order.OrderItem;
import com.hustict.aims.model.invoice.Invoice;
import com.hustict.aims.model.order.OrderStatus;
import com.hustict.aims.model.shipping.DeliveryInfo;

import com.hustict.aims.repository.InvoiceRepository;
import com.hustict.aims.repository.OrderRepository;
import com.hustict.aims.repository.DeliveryInfoRepository;
import com.hustict.aims.repository.product.ProductRepository;



import com.hustict.aims.utils.mapper.InvoiceMapper;
import com.hustict.aims.utils.mapper.OrderItemMapper;

import java.util.ArrayList;
import java.util.List;

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

    @Autowired
    OrderItemMapper orderItemMapper;


    @Autowired
    ProductRepository productRepository;

    public Long save(CartRequestDTO cart, DeliveryFormDTO deliverformDTO, InvoiceDTO invoiceDTO,HttpSession session){
        DeliveryInfo deliveryForm = DeliveryFormMapper.toEntity(deliverformDTO);
        Invoice invoice = invoiceMapper.toEntity(invoiceDTO);

        deliveryForm = deliveryFormRepository.save(deliveryForm); 
        invoice = invoiceRepository.save(invoice);
        
        Long deliveryFormid = deliveryForm.getId();
        Long invoiceid = invoice.getId();

        deliverformDTO.setId(deliveryFormid);
        invoiceDTO.setId(invoiceid);

        Order temp  = new Order();
        temp.setOrderDate(LocalDateTime.now()); 
        temp.setOrderStatus(OrderStatus.PENDING); 
        temp.setIsRushOrder(cart.isRushOrder());
        temp.setCurrency(cart.getCurrency()); 
        temp.setDeliveryInfo(deliveryForm); 
        temp.setInvoice(invoice); 

        Order savedOrder = orderRepository.save(temp); // đặt tên khác

        List<OrderItem> orderItems = cart.getProductList().stream()
            .map(dto -> {
                Product product = productRepository.getReferenceById(dto.getProductID());
                product.setTitle(dto.getProductName()); 
                product.setCurrentPrice(dto.getPrice());     
                return OrderItemMapper.toEntity(dto, savedOrder, product);
            })
            .collect(Collectors.toList());

        for (OrderItem item : orderItems) {
            savedOrder.addOrderItem(item); // đảm bảo quan hệ 2 chiều
        }

        orderRepository.save(savedOrder);  

        return savedOrder.getId();
    }

}
