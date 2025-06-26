package com.hustict.aims.utils.mapper;

import com.hustict.aims.dto.deliveryForm.DeliveryFormDTO;
import com.hustict.aims.dto.order.OrderInformationDTO;
import com.hustict.aims.dto.cart.CartItemRequestDTO;
import com.hustict.aims.dto.invoice.InvoiceDTO;
import com.hustict.aims.model.order.Order;
import com.hustict.aims.model.order.OrderItem;
import com.hustict.aims.model.shipping.DeliveryInfo;
import com.hustict.aims.repository.DeliveryInfoRepository;
import com.hustict.aims.utils.mapper.DeliveryInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderMapper {

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Autowired
    private InvoiceMapper invoiceMapper;

    @Autowired
    private DeliveryInfoRepository deliveryInfoRepository;
    
    @Autowired
    private DeliveryInfoMapper deliveryInfoMapper;
    
    public Order toEntity(
        OrderInformationDTO dto,
        DeliveryFormDTO deliveryDto
    ) {
        if (dto == null) {
            throw new IllegalArgumentException("OrderInformationDTO (order) không được null trong mapper");
        }
        if (deliveryDto == null && dto.getDeliveryInfoId() == null) {
            throw new IllegalArgumentException("Phải cung cấp DeliveryFormDTO hoặc deliveryInfoId không được null trong mapper");
        }

        Order order = new Order();
        order.setOrderDate(dto.getOrderDate());
        order.setIsRushOrder(dto.isRushOrder());
        order.setCurrency(dto.getCurrency());

        if (dto.getDeliveryInfoId() != null) {
            DeliveryInfo loaded = deliveryInfoRepository.findById(dto.getDeliveryInfoId())
                .orElseThrow(() -> new IllegalArgumentException(
                    "Không tìm thấy DeliveryInfo với id=" + dto.getDeliveryInfoId()
                ));
            order.setDeliveryInfo(loaded);
        } else {
            order.setDeliveryInfo(deliveryInfoMapper.toEntity(deliveryDto));
        }

        // Set Invoice
        InvoiceDTO invDto = new InvoiceDTO(
            null,
            dto.getTotalPriceExVAT(),
            dto.getTotalPriceInVAT(),
            dto.getShippingFee(),
            dto.getTotalAmount()
        );
        order.setInvoice(invoiceMapper.toEntity(invDto));

        // Các OrderItem
        List<OrderItem> items = dto.getProductList().stream()
            .map(orderItemMapper::toEntity)
            .collect(Collectors.toList());
        items.forEach(order::addOrderItem);

        return order;
    }

    public OrderInformationDTO toDTO(Order entity) {
        if (entity == null) return null;

        List<CartItemRequestDTO> itemDTOs = entity.getOrderItems().stream()
            .map(orderItemMapper::toDTO)
            .collect(Collectors.toList());

        InvoiceDTO invDto = invoiceMapper.toDTO(entity.getInvoice());

        OrderInformationDTO dto = new OrderInformationDTO();
        dto.setOrderId(entity.getId());
        dto.setProductList(itemDTOs);
        dto.setTotalPriceExVAT(invDto.getProductPriceExVAT());
        dto.setTotalPriceInVAT(invDto.getProductPriceIncVAT());
        dto.setShippingFee(invDto.getShippingFee());
        dto.setTotalAmount(invDto.getTotalAmount());
        dto.setCurrency(entity.getCurrency());
        dto.setOrderDate(entity.getOrderDate());
        dto.setRushOrder(entity.getIsRushOrder());
        //dto.setRushIntruction(entity.getRushInstruction());
        return dto;
    }
}
