package com.hustict.aims.service.shippingFeeCalculator;

import com.hustict.aims.dto.cart.CartItemRequestDTO;
import com.hustict.aims.dto.cart.CartRequestDTO;
import com.hustict.aims.dto.deliveryForm.DeliveryFormDTO;
import org.springframework.stereotype.Service;


import java.util.List;

@Service("standardShippingFee")
public class StandardShippingFee implements ShippingFeeCalculator {

    @Override
    public int calculateShippingFee(String province, List<CartItemRequestDTO> productList, int totalPrice) {
        double shippingFee = 0;
        double totalWeight = 0;
//        List<CartItemRequestDTO> productList= cart.getProductList();
        for (CartItemRequestDTO item : productList) {
            totalWeight += item.getWeight() * item.getQuantity();  
        }
        if (province.equalsIgnoreCase("Hanoi") 
         || province.equalsIgnoreCase("HoChiMinhCity")) {
            shippingFee = (totalWeight <= 3) ? 22000 : 22000 + ((totalWeight - 3) / 0.5) * 2500;
        } else {
            shippingFee = (totalWeight <= 0.5) ? 30000 : 30000 + ((totalWeight - 0.5) / 0.5) * 2500;
        }

        if (totalPrice> 100000) {
            shippingFee = shippingFee - Math.min(shippingFee, 25000);  
        }

        return (int) Math.round(shippingFee);
    }
}

