package com.hustict.aims.service.placeRushOrder;

import com.hustict.aims.dto.cart.CartRequestDTO;
import com.hustict.aims.dto.deliveryForm.DeliveryFormDTO;
import com.hustict.aims.dto.invoice.InvoiceDTO;
import com.hustict.aims.dto.order.OrderInformationDTO;
import com.hustict.aims.model.order.Order;
import com.hustict.aims.model.order.OrderStatus;
import com.hustict.aims.model.shipping.DeliveryInfo;
import com.hustict.aims.model.invoice.Invoice;
import com.hustict.aims.repository.OrderRepository;
import com.hustict.aims.repository.InvoiceRepository;
import com.hustict.aims.utils.mapper.DeliveryInfoMapper;
import com.hustict.aims.utils.mapper.OrderInformationDTOMapper;
import com.hustict.aims.utils.mapper.OrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class RushOrderSaveService {
    
    private final OrderRepository orderRepository;
    private final InvoiceRepository invoiceRepository;
    private final OrderMapper orderMapper;
    private final DeliveryInfoMapper deliveryInfoMapper;

    @Autowired
    public RushOrderSaveService(OrderRepository orderRepository,
                               InvoiceRepository invoiceRepository,
                               OrderMapper orderMapper,
                               DeliveryInfoMapper deliveryInfoMapper) {
        this.orderRepository = orderRepository;
        this.invoiceRepository = invoiceRepository;
        this.orderMapper = orderMapper;
        this.deliveryInfoMapper = deliveryInfoMapper;
    }

    @Transactional
    public OrderInformationDTO saveRushOrder(CartRequestDTO cart, 
                                            DeliveryFormDTO deliveryInfo,
                                            InvoiceDTO invoiceDTO) {
        
        // Lưu delivery information
        DeliveryInfo deliveryEntity = deliveryInfoMapper.toEntity(deliveryInfo);
        deliveryEntity.setExpectedTime(deliveryInfo.getExpectedDateTime());
        
        // Lưu invoice
        Invoice invoiceEntity = new Invoice();
        invoiceEntity.setProductPriceExVAT(invoiceDTO.getProductPriceExVAT());
        invoiceEntity.setProductPriceIncVAT(invoiceDTO.getProductPriceIncVAT());
        invoiceEntity.setShippingFee(invoiceDTO.getShippingFee());
        invoiceEntity.setTotalAmount(invoiceDTO.getTotalAmount());
        
        invoiceRepository.save(invoiceEntity);
        
        // Tạo OrderInformationDTO
        OrderInformationDTO orderInfoDTO = OrderInformationDTOMapper.toDTO(
            cart, invoiceDTO, deliveryInfo, invoiceEntity.getId(), null
        );
        
        // Lưu order
        Order order = orderMapper.toEntity(orderInfoDTO);
        order.setOrderDate(LocalDateTime.now());
        order.setOrderStatus(OrderStatus.PENDING);
        order.setIsRushOrder(true);
        order.setCurrency(cart.getCurrency());
        order.setDeliveryInfo(deliveryEntity);
        order.setInvoice(invoiceEntity);
        
        orderRepository.save(order);
        
        return orderInfoDTO;
    }
} 