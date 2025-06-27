package com.hustict.aims.utils.mapper;


import com.hustict.aims.dto.cart.CartItemRequestDTO;
import com.hustict.aims.model.order.OrderItem;
import com.hustict.aims.model.product.Product;
import com.hustict.aims.repository.product.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderItemMapper {

    @Autowired
    private ProductRepository productRepository;

    public OrderItem toEntity(CartItemRequestDTO dto) {
        if (dto == null) return null;
        OrderItem item = new OrderItem();
        Product product = productRepository.findById(dto.getProductID())
            .orElseThrow(() -> new IllegalArgumentException("Product not found: " + dto.getProductID()));
        item.setProduct(product);
        item.setQuantity(dto.getQuantity());
        return item;
    }

    public CartItemRequestDTO toDTO(OrderItem entity) {
        if (entity == null) return null;
        CartItemRequestDTO dto = new CartItemRequestDTO();
        dto.setProductID(entity.getProduct().getId());
        dto.setProductName(entity.getProduct().getTitle());   
        dto.setQuantity(entity.getQuantity());
        dto.setPrice(entity.getProduct().getCurrentPrice());        
        return dto;
    }
}
