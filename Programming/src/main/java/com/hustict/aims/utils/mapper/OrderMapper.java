package com.hustict.aims.utils.mapper;

import com.hustict.aims.dto.invoice.InvoiceDTO;
import com.hustict.aims.dto.order.OrderInformationDTO;

import com.hustict.aims.model.order.Order;
import com.hustict.aims.model.order.OrderItem;
import com.hustict.aims.model.shipping.DeliveryInfo;
import com.hustict.aims.model.invoice.Invoice;

import com.hustict.aims.repository.DeliveryInfoRepository;
import com.hustict.aims.repository.InvoiceRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderMapper {

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Autowired
    private DeliveryInfoRepository deliveryInfoRepository;

    @Autowired
    private InvoiceRepository invoiceRepository;

    public Order toEntity(OrderInformationDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("OrderInformationDTO không được null.");
        }

        if (dto.getDeliveryInfoId() == null) {
            throw new IllegalArgumentException("deliveryInfoId không được null khi không có DeliveryFormDTO.");
        }

        Order order = new Order();
        order.setOrderDate(dto.getOrderDate());
        order.setIsRushOrder(dto.isRushOrder());
        order.setCurrency(dto.getCurrency());

        // Load DeliveryInfo từ repository
        DeliveryInfo deliveryInfo = deliveryInfoRepository.findById(dto.getDeliveryInfoId())
            .orElseThrow(() -> new IllegalArgumentException(
                "Không tìm thấy DeliveryInfo với id = " + dto.getDeliveryInfoId()
            ));
        order.setDeliveryInfo(deliveryInfo);

        Invoice invoice = invoiceRepository.findById(dto.getInvoiceId())
            .orElseThrow(() -> new IllegalArgumentException(
                "Không tìm thấy Invoice với id = " + dto.getDeliveryInfoId()
            ));
        order.setInvoice(invoice);

        // Ánh xạ danh sách sản phẩm
        List<OrderItem> orderItems = dto.getProductList().stream()
            .map(orderItemMapper::toEntity)
            .collect(Collectors.toList());
        orderItems.forEach(order::addOrderItem);

        return order;
    }
    // public OrderInformationDTO toDTO(Order entity) {
    //     if (entity == null) return null;

    //     List<CartItemRequestDTO> items = entity.getOrderItems().stream()
    //         .map(orderItemMapper::toDTO)
    //         .collect(Collectors.toList());

    //     InvoiceDTO invoiceDTO = invoiceMapper.toDTO(entity.getInvoice());

    //     OrderInformationDTO dto = new OrderInformationDTO();
    //     dto.setOrderId(entity.getId());
    //     dto.setProductList(items);
    //     dto.setCurrency(entity.getCurrency());
    //     dto.setOrderDate(entity.getOrderDate());
    //     dto.setRushOrder(entity.getIsRushOrder());
        
    //     return dto;
    // }
}