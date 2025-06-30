package com.hustict.aims.utils.mapper;


import com.hustict.aims.dto.cart.CartItemRequestDTO;
import com.hustict.aims.model.order.OrderItem;
import com.hustict.aims.model.order.OrderItemKey;
import com.hustict.aims.model.order.OrderType;
import com.hustict.aims.model.order.Order;

import com.hustict.aims.model.product.Product;
import com.hustict.aims.repository.product.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderItemMapper {

    @Autowired
    private ProductRepository productRepository;

      public static OrderItem toEntity(CartItemRequestDTO dto, Order order, Product product) {
        OrderItem entity = new OrderItem();

        entity.setOrder(order);
        entity.setProduct(product);

        entity.setQuantity(dto.getQuantity());
        entity.setPrice(dto.getPrice());

        return entity;
    }

    public static CartItemRequestDTO toDTO(OrderItem entity) {
        if (entity == null) return null;

        CartItemRequestDTO dto = new CartItemRequestDTO();
        dto.setProductID(entity.getProduct().getId());
        dto.setProductName(entity.getProduct().getTitle());
        dto.setQuantity(entity.getQuantity());
        dto.setPrice(entity.getProduct().getCurrentPrice());

        return dto;
    }
}
