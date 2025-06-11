package com.hustict.aims.service.shippingFeeCalculator;

import org.springframework.stereotype.Service;

import com.hustict.aims.dto.cart.CartItemRequestDTO;
import com.hustict.aims.dto.cart.CartRequestDTO;
import com.hustict.aims.dto.deliveryForm.DeliveryFormDTO;

@Service("rushShippingFee") 
public class RushShippingFee implements ShippingFeeCalculator {

    @Override
    public int calculateShippingFee(DeliveryFormDTO deliveryForm,CartRequestDTO cart) {
        double shippingFee = 0;
        double totalWeight = 0;

        for (CartItemRequestDTO item : cart.getProductList()) {
            totalWeight += item.getWeight() * item.getQuantity();  
        }
        if (deliveryForm.getDeliveryProvince().equals("Hanoi") || deliveryForm.getDeliveryProvince().equals("HoChiMinhCity")){
            shippingFee = (totalWeight <= 3) ? 22000 : 22000 + ((totalWeight - 3) / 0.5) * 2500;
        } else {
            shippingFee = (totalWeight <= 0.5) ? 30000 : 30000 + ((totalWeight - 0.5) / 0.5) * 2500;
        }
        shippingFee = cart.getTotalItem() * 10000;
        return (int) Math.round(shippingFee);
    }
}