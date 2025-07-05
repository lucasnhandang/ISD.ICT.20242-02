package com.hustict.aims.service.shippingFeeCalculator;

import com.hustict.aims.dto.cart.CartItemRequestDTO;
import com.hustict.aims.dto.cart.CartRequestDTO;
import com.hustict.aims.dto.deliveryForm.DeliveryFormDTO;
import org.springframework.stereotype.Service;


@Service("standardShippingFee")
public class StandardShippingFee implements ShippingFeeCalculator {

    @Override
    public int calculateShippingFee(DeliveryFormDTO deliveryForm,CartRequestDTO cart) {
        double shippingFee = 0;
        double totalWeight = 0;

        for (CartItemRequestDTO item : cart.getProductList()) {
            totalWeight += item.getWeight() * item.getQuantity();  
        }
        if (deliveryForm.getDeliveryProvince().equals("Hanoi") || 
            deliveryForm.getDeliveryProvince().equals("HoChiMinhCity") || 
            deliveryForm.getDeliveryProvince().equals("Hochiminh")){
            shippingFee = (totalWeight <= 3) ? 22000 : 22000 + ((totalWeight - 3) / 0.5) * 2500;
        } else {
            shippingFee = (totalWeight <= 0.5) ? 30000 : 30000 + ((totalWeight - 0.5) / 0.5) * 2500;
        }

        if (cart.getTotalPrice() > 100000) {
            shippingFee = shippingFee - Math.min(shippingFee, 25000);  
        }

        return (int) Math.round(shippingFee);
    }
}