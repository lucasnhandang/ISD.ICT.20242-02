package com.hustict.aims.dto.cart;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.Data;

@Data
public class CartRequestDTO {
     private List<CartItemRequestDTO> productList;
     private int totalPrice;
     private int totalItem;
     private String currency;
     private int discount;

     
    // Lấy product list cho ProductAvailabilityService
    public List<Map.Entry<Long, Integer>> getProductQuantity() {
        List<Map.Entry<Long, Integer>> result = new ArrayList<>();
        if (productList != null) {
            for (CartItemRequestDTO item : productList) {
                result.add(new SimpleEntry<>(item.getProductID(), item.getQuantity()));
            }
        }
        return result;
    }
     
}
