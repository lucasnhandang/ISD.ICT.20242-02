package com.hustict.aims.service.shippingFeeCalculator;

import org.springframework.stereotype.Service;

import com.hustict.aims.dto.cart.CartItemRequestDTO;
import com.hustict.aims.dto.cart.CartRequestDTO;
import com.hustict.aims.dto.deliveryForm.DeliveryFormDTO;

@Service("rushShippingFee")
public class RushShippingFee implements ShippingFeeCalculator {

    @Override
    public int calculateShippingFee(DeliveryFormDTO deliveryInfo, CartRequestDTO rushCart) {
        double totalWeight = 0;

        // Tính tổng khối lượng đơn hàng
        for (CartItemRequestDTO item : rushCart.getProductList()) {
            totalWeight += item.getWeight() * item.getQuantity();
        }

        double shippingFee;
        // Tính phí ship theo tỉnh và khối lượng
        if (deliveryInfo.getDeliveryProvince().equalsIgnoreCase("Hanoi") 
         || deliveryInfo.getDeliveryProvince().equalsIgnoreCase("HoChiMinhCity")) {
            shippingFee = (totalWeight <= 3)
                ? 22000
                : 22000 + Math.ceil((totalWeight - 3) / 0.5) * 2500;
        } else {
            shippingFee = (totalWeight <= 0.5)
                ? 30000
                : 30000 + Math.ceil((totalWeight - 0.5) / 0.5) * 2500;
        }

        // Phí rush: 10.000 cho mỗi sản phẩm trong giỏ hàng
        int rushFee = 10000 * rushCart.getProductList().size();

        // Tổng phí = phí ship + phí rush
        return (int) Math.round(shippingFee + rushFee);
    }
}